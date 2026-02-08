package br.com.interno.orders_api.service;

import br.com.interno.orders_api.enums.OrderStatus;
import br.com.interno.orders_api.exception.OrderNotFoundException;
import br.com.interno.orders_api.model.Order;
import br.com.interno.orders_api.model.OrderItem;
import br.com.interno.orders_api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    public Page<Order> findAll(Pageable pageable) {
        log.info("Listing orders - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        return orderRepository.findAll(pageable);
    }

    public Order findById(Long id) {
        log.info("Finding order by id: {}", id);

        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    public Order create(Order order) {
        log.info("Creating order for customer: {}", order.getCustomerEmail());

        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.ZERO);

        return orderRepository.save(order);
    }

    public Order update(Long id, Order updatedOrder) {
        log.info("Updating order id: {}", id);

        Order existing = findById(id);

        existing.setCustomerName(updatedOrder.getCustomerName());
        existing.setCustomerEmail(updatedOrder.getCustomerEmail());
        existing.setStatus(updatedOrder.getStatus());

        return orderRepository.save(existing);
    }

    public void delete(Long id) {
        log.info("Deleting order id: {}", id);

        Order order = findById(id);
        orderRepository.delete(order);
    }

    public List<OrderItem> listItems(Long orderId) {
        log.info("Listing items for order id: {}", orderId);

        Order order = findById(orderId);
        return order.getItems();
    }

    public Order addItem(Long orderId, OrderItem item) {
        log.info("Adding item to order id: {}", orderId);

        Order order = findById(orderId);

        item.setSubtotal(
                item.getUnitPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity()))
        );

        order.addItem(item);
        order.recalcularTotal();

        return orderRepository.save(order);
    }

}

