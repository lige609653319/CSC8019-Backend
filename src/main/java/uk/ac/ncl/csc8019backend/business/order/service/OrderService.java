package uk.ac.ncl.csc8019backend.business.order.service;

import uk.ac.ncl.csc8019backend.business.order.dto.OrderRequestDTO;
import uk.ac.ncl.csc8019backend.business.order.entity.Order;
import uk.ac.ncl.csc8019backend.business.order.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    Order placeNewOrder(OrderRequestDTO dto);
    Order updateStatus(Long id, OrderStatus newStatus);
    List<Order> getOrdersByStatus(OrderStatus status);
    List<Order> getActiveOrders();
}
