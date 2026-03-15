package uk.ac.ncl.csc8019backend.business.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ncl.csc8019backend.business.order.dto.OrderRequestDTO;
import uk.ac.ncl.csc8019backend.business.order.dto.OrderResponseDTO;
import uk.ac.ncl.csc8019backend.business.order.entity.Order;
import uk.ac.ncl.csc8019backend.business.order.enums.OrderStatus;
import uk.ac.ncl.csc8019backend.business.order.service.OrderService;
import uk.ac.ncl.csc8019backend.system.common.Result;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public Result<List<OrderResponseDTO>> getAllOrders() {
        return Result.success(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public Result<List<OrderResponseDTO>> getOrdersByCustomerId(@PathVariable Long id) {
        return Result.success(orderService.getOrdersByCustomerId(id));
    }

    @PostMapping("/create")
    public Result<Order> createOrder(@RequestBody OrderRequestDTO orderRequest) {
        return Result.success(orderService.placeNewOrder(orderRequest));
    }

    @GetMapping("/status/{status}")
    public Result<List<OrderResponseDTO>> getOrdersByStatus(@PathVariable OrderStatus status) {
        return Result.success(orderService.getOrdersByStatus(status));
    }

    @PatchMapping("/{id}/update-status")
    public Result<OrderResponseDTO> updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return Result.success(orderService.updateStatus(id, status));
    }
}
