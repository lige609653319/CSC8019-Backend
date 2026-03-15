package uk.ac.ncl.csc8019backend.business.onlinepayment.service;

import uk.ac.ncl.csc8019backend.business.onlinepayment.config.HorsePayConfig;
import uk.ac.ncl.csc8019backend.business.onlinepayment.DTO.request.HorsePayRequest;
import uk.ac.ncl.csc8019backend.business.onlinepayment.DTO.response.HorsePayResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * HorsePay API service - handles external payment gateway calls
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HorsePayService {

    private final HorsePayConfig config;
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Process payment via HorsePay API
     *
     * @param request Payment request parameters
     * @return HorsePay result
     */
    public HorsePayResult processPayment(HorsePayRequest request) {
        try {
            // Build HorsePay request
            Map<String, Object> horsePayRequest = new HashMap<>();
            horsePayRequest.put("storeID", config.getStoreId());
            horsePayRequest.put("customerID", request.getCustomerId());
            horsePayRequest.put("date", formatDate(LocalDateTime.now()));
            horsePayRequest.put("time", formatTime(LocalDateTime.now()));
            horsePayRequest.put("timeZone", "GMT");
            horsePayRequest.put("transactionAmount", request.getAmount().doubleValue());
            horsePayRequest.put("currencyCode", "GBP");

            if (request.getForceStatus() != null) {
                horsePayRequest.put("forcePaymentSatusReturnType", request.getForceStatus());
            }

            log.info("Calling HorsePay API: {}", horsePayRequest);

            // Send request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(horsePayRequest, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    config.getApiUrl(),
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            // Parse response
            Map<String, Object> body = response.getBody();
            log.info("HorsePay response: {}", body);

            return parseResponse(body);

        } catch (Exception e) {
            log.error("HorsePay API call failed", e);
            // Fallback: return failure
            return HorsePayResult.builder()
                    .success(false)
                    .message("Payment service unavailable: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Parse HorsePay response
     */
    private HorsePayResult parseResponse(Map<String, Object> body) {
        HorsePayResult result = new HorsePayResult();
        result.setStoreId((String) body.get("storeID"));
        result.setCustomerId((String) body.get("customerID"));
        result.setTransactionAmount(new BigDecimal(body.get("transactionAmount").toString()));

        Map<String, Object> paymentSuccess = (Map<String, Object>) body.get("paymetSuccess");
        if (paymentSuccess != null) {
            result.setSuccess((Boolean) paymentSuccess.get("Status"));
            result.setMessage((String) paymentSuccess.get("reason"));
        }

        return result;
    }

    /**
     * Format date as dd/MM/yyyy
     */
    private String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * Format time as HH:mm
     */
    private String formatTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
