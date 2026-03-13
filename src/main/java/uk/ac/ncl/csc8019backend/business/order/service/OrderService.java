package uk.ac.ncl.csc8019backend.business.order.service;

import uk.ac.ncl.csc8019backend.business.order.dto.OrderItemResponseDTO;
import uk.ac.ncl.csc8019backend.business.order.dto.OrderRequestDTO;
import uk.ac.ncl.csc8019backend.business.order.dto.OrderResponseDTO;
import uk.ac.ncl.csc8019backend.business.order.entity.Order;
import uk.ac.ncl.csc8019backend.business.order.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    Order placeNewOrder(OrderRequestDTO dto);
    OrderResponseDTO updateStatus(Long id, OrderStatus newStatus);
    List<OrderResponseDTO> getOrdersByStatus(OrderStatus status);
    List<OrderResponseDTO> getAllOrders();
    List<OrderResponseDTO> getOrdersByCustomerId(Long id);
    List<Order> getActiveOrders();
}
