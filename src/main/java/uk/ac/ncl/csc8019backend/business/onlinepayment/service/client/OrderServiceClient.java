package uk.ac.ncl.csc8019backend.business.onlinepayment.service.client;

import uk.ac.ncl.csc8019backend.business.onlinepayment.DTO.response.OrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Order service client - calls order module API
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${order.service.url:http://localhost:8080}")
    private String orderServiceUrl;

    /**
     * Get order details by ID
     *
     * @param orderId Order ID
     * @return Order DTO or null if not found
     */
    public OrderDTO getOrder(Long orderId) {
        try {
            String url = orderServiceUrl + "/api/orders/" + orderId;
            ResponseEntity<OrderDTO> response = restTemplate.getForEntity(url, OrderDTO.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to get order: {}", orderId, e);
            return null;
        }
    }

    /**
     * Update order payment status
     *
     * @param orderId Order ID
     * @param status Payment status: 1=success, 2=failed
     * @param paymentNo Payment serial number
     * @return Whether update succeeded
     */
    public boolean updatePaymentStatus(Long orderId, Integer status, String paymentNo) {
        try {
            String url = orderServiceUrl + "/api/orders/" + orderId + "/payment-status";
            Map<String, Object> request = new HashMap<>();
            request.put("paymentStatus", status);
            request.put("paymentNo", paymentNo);
            request.put("timestamp", System.currentTimeMillis());

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Failed to update payment status: {}", orderId, e);
            return false;
        }
    }

    /**
     * Apply discount to order (for 10th order free)
     *
     * @param orderId Order ID
     * @param discountAmount Discount amount
     * @return Whether application succeeded
     */
    public boolean applyDiscount(Long orderId, BigDecimal discountAmount) {
        try {
            String url = orderServiceUrl + "/api/orders/" + orderId + "/discount";
            Map<String, Object> request = new HashMap<>();
            request.put("discountAmount", discountAmount);
            request.put("reason", "LOYALTY_10TH_FREE");

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Failed to apply discount: {}", orderId, e);
            return false;
        }
    }
}