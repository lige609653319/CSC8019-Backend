package uk.ac.ncl.csc8019backend.business.order.service.ServiceImplementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ncl.csc8019backend.business.menu.entity.MenuSku;
import uk.ac.ncl.csc8019backend.business.menu.repository.MenuSkuRepository;
import uk.ac.ncl.csc8019backend.business.order.dto.OrderItemRequestDTO;
import uk.ac.ncl.csc8019backend.business.order.dto.OrderItemResponseDTO;
import uk.ac.ncl.csc8019backend.business.order.dto.OrderRequestDTO;
import uk.ac.ncl.csc8019backend.business.order.dto.OrderResponseDTO;
import uk.ac.ncl.csc8019backend.business.order.entity.Order;
import uk.ac.ncl.csc8019backend.business.order.entity.OrderItem;
import uk.ac.ncl.csc8019backend.business.order.enums.OrderStatus;
import uk.ac.ncl.csc8019backend.business.order.repository.OrderItemRepository;
import uk.ac.ncl.csc8019backend.business.order.repository.OrderRepository;
import uk.ac.ncl.csc8019backend.business.order.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuSkuRepository menuSkuRepository;

    @Override
    @Transactional
    public List<OrderResponseDTO> getAllOrders()
    {
        List<Order> orders = orderRepository.findAll();

        List<OrderResponseDTO> responseList = orders.stream().map(order -> {

            List<OrderItemResponseDTO> itemDTOs = order.getItems().stream()
                    .map(item -> OrderItemResponseDTO.builder()
                            .menuName(item.getMenuSku().getMenu().getName())
                            .quantity(item.getQuantity())
                            .priceAtPurchase(item.getPriceAtPurchase())
                            .build())
                    .collect(Collectors.toList());

            return OrderResponseDTO.builder()
                    .id(order.getId())
                    .customerId(order.getCustomerId())
                    .totalPrice(order.getTotalPrice())
                    .orderDate(order.getOrderDate() != null ? order.getOrderDate().toString() : null)
                    .status(order.getStatus().name())
                    .orderType(order.getOrderType().name())
                    .items(itemDTOs)
                    .build();
        }).collect(Collectors.toList());
        return responseList;
    }

    @Override
    @Transactional
    public List<OrderResponseDTO> getOrdersByCustomerId(Long customerId)
    {
        List<Order> orders = orderRepository.findByCustomerId(customerId);

        List<OrderResponseDTO> responseList = orders.stream().map(order -> {

            List<OrderItemResponseDTO> itemDTOs = order.getItems().stream()
                    .map(item -> OrderItemResponseDTO.builder()
                            .menuName(item.getMenuSku().getMenu().getName())
                            .quantity(item.getQuantity())
                            .priceAtPurchase(item.getPriceAtPurchase())
                            .build())
                    .collect(Collectors.toList());

            return OrderResponseDTO.builder()
                    .id(order.getId())
                    .customerId(order.getCustomerId())
                    .totalPrice(order.getTotalPrice())
                    .orderDate(order.getOrderDate() != null ? order.getOrderDate().toString() : null)
                    .status(order.getStatus().name())
                    .orderType(order.getOrderType().name())
                    .items(itemDTOs)
                    .build();
        }).collect(Collectors.toList());
        return responseList;
    }

    @Override
    @Transactional
    public Order placeNewOrder(OrderRequestDTO dto) {
        Order order = new Order();
        order.setCustomerId(dto.getCustomerId());
        order.setTotalPrice(dto.getTotalPrice());
        order.setOrderType(dto.getOrderType());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setItems(new ArrayList<>());

        Order savedOrder = orderRepository.save(order);
        if (dto.getItems() != null) {
            for (OrderItemRequestDTO itemDto : dto.getItems()) {
                Long id = itemDto.getId();
                MenuSku sku = menuSkuRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("SKU not found"));

                OrderItem item = new OrderItem();
                item.setOrder(savedOrder);
                item.setMenuSku(sku);
                item.setQuantity(itemDto.getQuantity()); // Use dynamic quantity
                item.setPriceAtPurchase(BigDecimal.valueOf(sku.getPrice()).multiply(BigDecimal.valueOf(itemDto.getQuantity())));

                OrderItem savedItem = orderItemRepository.save(item);
                savedOrder.getItems().add(savedItem);
            }
        }
        return savedOrder;
    }

    @Override
    @Transactional
    public OrderResponseDTO updateStatus(Long orderId, OrderStatus newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        order.setStatus(newStatus);

        Order updatedOrder = orderRepository.save(order);

        List<OrderItemResponseDTO> itemDTOs = updatedOrder.getItems().stream()
                .map(item -> OrderItemResponseDTO.builder()
                        .menuName(item.getMenuSku().getMenu().getName())
                        .quantity(item.getQuantity())
                        .priceAtPurchase(item.getPriceAtPurchase())
                        .build())
                .collect(Collectors.toList());

        OrderResponseDTO response = OrderResponseDTO.builder()
                .id(updatedOrder.getId())
                .customerId(updatedOrder.getCustomerId())
                .totalPrice(updatedOrder.getTotalPrice())
                .status(updatedOrder.getStatus().name())
                .orderType(updatedOrder.getOrderType().name())
                .orderDate(updatedOrder.getOrderDate() != null ? updatedOrder.getOrderDate().toString() : null)
                .items(itemDTOs)
                .build();

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByStatus(OrderStatus status) {

        List<Order> orders = orderRepository.findByStatus(status);

        List<OrderResponseDTO> responseList = orders.stream().map(order -> {

            List<OrderItemResponseDTO> itemDTOs = order.getItems().stream()
                    .map(item -> OrderItemResponseDTO.builder()
                            .menuName(item.getMenuSku().getMenu().getName())
                            .quantity(item.getQuantity())
                            .priceAtPurchase(item.getPriceAtPurchase())
                            .build())
                    .collect(Collectors.toList());

            return OrderResponseDTO.builder()
                    .id(order.getId())
                    .customerId(order.getCustomerId())
                    .totalPrice(order.getTotalPrice())
                    .orderDate(order.getOrderDate() != null ? order.getOrderDate().toString() : null)
                    .status(order.getStatus().name())
                    .orderType(order.getOrderType().name())
                    .items(itemDTOs)
                    .build();
        }).collect(Collectors.toList());

        return responseList;
    }

    @Override
    public List<Order> getActiveOrders() {

        return orderRepository.findByStatus(OrderStatus.PENDING);
    }
}