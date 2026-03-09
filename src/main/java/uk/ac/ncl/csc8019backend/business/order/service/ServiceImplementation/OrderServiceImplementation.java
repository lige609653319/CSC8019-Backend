package uk.ac.ncl.csc8019backend.business.order.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ncl.csc8019backend.business.menu.entity.MenuSku;
import uk.ac.ncl.csc8019backend.business.menu.repository.MenuSkuRepository;
import uk.ac.ncl.csc8019backend.business.order.dto.OrderRequestDTO;
import uk.ac.ncl.csc8019backend.business.order.entity.Order;
import uk.ac.ncl.csc8019backend.business.order.entity.OrderItem;
import uk.ac.ncl.csc8019backend.business.order.enums.OrderStatus;
import uk.ac.ncl.csc8019backend.business.order.repository.OrderItemRepository;
import uk.ac.ncl.csc8019backend.business.order.repository.OrderRepository;
import uk.ac.ncl.csc8019backend.business.order.service.OrderService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor // Standard for your project: replaces @Autowired on fields
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuSkuRepository menuSkuRepository;

    @Override
    @Transactional
    public Order placeNewOrder(OrderRequestDTO dto) {
        Order order = new Order();
        order.setCustomerId(dto.getCustomerId());
        order.setTotalPrice(dto.getTotalPrice());
        order.setOrderType(dto.getOrderType());
        order.setStatus(OrderStatus.PENDING);
        order.setItems(new ArrayList<>()); // Initialize list to prevent NullPointerExceptions

        // Save order first to get the generated ID
        Order savedOrder = orderRepository.save(order);

        // Process items and link them to the order
        if (dto.getCoffeeIds() != null) {
            for (Long skuId : dto.getCoffeeIds()) {
                MenuSku sku = menuSkuRepository.findById(skuId)
                        .orElseThrow(() -> new RuntimeException("SKU not found with ID: " + skuId));

                OrderItem item = new OrderItem();
                item.setOrder(savedOrder);
                item.setMenuSku(sku);
                item.setQuantity(1); // Default quantity
                item.setPriceAtPurchase(BigDecimal.valueOf(sku.getPrice()));

                OrderItem savedItem = orderItemRepository.save(item);
                savedOrder.getItems().add(savedItem);
            }
        }

        return savedOrder;
    }

    @Override
    public Order updateStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order #" + id + " not found"));
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public List<Order> getActiveOrders() {
        return orderRepository.findByStatus(OrderStatus.PENDING);
    }
}