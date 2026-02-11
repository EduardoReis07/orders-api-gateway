package br.com.interno.orders_api.service;

import br.com.interno.orders_api.enums.OrderStatus;
import br.com.interno.orders_api.exception.OrderNotFoundException;
import br.com.interno.orders_api.model.Order;
import br.com.interno.orders_api.model.OrderItem;
import br.com.interno.orders_api.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldReturnOrderWhenFoundById() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        Order result = orderService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        when(orderRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                OrderNotFoundException.class,
                () -> orderService.findById(99L)
        );
    }

    @Test
    void shouldAddItemAndRecalculateTotal() {
        Order order = new Order();
        order.setId(1L);
        order.setItems(new ArrayList<>());

        OrderItem item = new OrderItem();
        item.setQuantity(2);
        item.setUnitPrice(new BigDecimal("10.00"));

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order updated = orderService.addItem(1L, item);

        assertThat(updated.getItems()).hasSize(1);
        assertThat(updated.getTotalAmount())
                .isEqualByComparingTo("20.00");
    }

    @Test
    void shouldFindAllOrdersWithPagination() {
        List<Order> orders = List.of(createOrder(1L), createOrder(2L));
        Page<Order> page = new PageImpl<>(orders);
        Pageable pageable = PageRequest.of(0, 10);

        when(orderRepository.findAll(pageable))
                .thenReturn(page);

        Page<Order> result = orderService.findAll(pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
    }

    @Test
    void shouldCreateOrderWithPendingStatusAndZeroTotal() {
        Order order = new Order();
        order.setCustomerName("João da Silva");
        order.setCustomerEmail("joao@email.com");

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order saved = invocation.getArgument(0);
                    saved.setId(1L);
                    return saved;
                });

        Order result = orderService.create(order);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(result.getTotalAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.getOrderDate()).isNotNull();
    }

    @Test
    void shouldUpdateOrderSuccessfully() {
        Order existing = createOrder(1L);

        Order updatedData = new Order();
        updatedData.setCustomerName("João Atualizado");
        updatedData.setCustomerEmail("joao.novo@email.com");
        updatedData.setStatus(OrderStatus.CONFIRMED);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.update(1L, updatedData);

        assertThat(result.getCustomerName()).isEqualTo("João Atualizado");
        assertThat(result.getCustomerEmail()).isEqualTo("joao.novo@email.com");
        assertThat(result.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
    }

    @Test
    void shouldThrowExceptionWhenUpdateNonExistentOrder() {
        when(orderRepository.findById(99L))
                .thenReturn(Optional.empty());

        Order updatedData = new Order();
        updatedData.setCustomerName("João");

        assertThrows(
                OrderNotFoundException.class,
                () -> orderService.update(99L, updatedData)
        );
    }

    @Test
    void shouldDeleteOrderSuccessfully() {
        Order order = createOrder(1L);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        doNothing().when(orderRepository).delete(order);

        orderService.delete(1L);

        verify(orderRepository).delete(order);
    }

    @Test
    void shouldThrowExceptionWhenDeleteNonExistentOrder() {
        when(orderRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                OrderNotFoundException.class,
                () -> orderService.delete(99L)
        );
    }

    @Test
    void shouldListItemsFromOrder() {
        Order order = createOrder(1L);
        OrderItem item1 = createOrderItem(1L);
        OrderItem item2 = createOrderItem(2L);
        order.setItems(List.of(item1, item2));

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        List<OrderItem> result = orderService.listItems(1L);

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldThrowExceptionWhenListItemsOfNonExistentOrder() {
        when(orderRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                OrderNotFoundException.class,
                () -> orderService.listItems(99L)
        );
    }

    @Test
    void shouldAddMultipleItemsAndRecalculateTotal() {
        Order order = createOrder(1L);
        order.setItems(new ArrayList<>());

        OrderItem item1 = new OrderItem();
        item1.setQuantity(2);
        item1.setUnitPrice(new BigDecimal("10.00"));

        OrderItem item2 = new OrderItem();
        item2.setQuantity(3);
        item2.setUnitPrice(new BigDecimal("15.00"));

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        orderService.addItem(1L, item1);
        Order result = orderService.addItem(1L, item2);

        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getTotalAmount())
                .isEqualByComparingTo("65.00");
    }

    private Order createOrder(Long id) {
        Order order = new Order();
        order.setId(id);
        order.setCustomerName("João da Silva");
        order.setCustomerEmail("joao@email.com");
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setItems(new ArrayList<>());
        return order;
    }

    private OrderItem createOrderItem(Long id) {
        OrderItem item = new OrderItem();
        item.setId(id);
        item.setProductName("Produto " + id);
        item.setQuantity(1);
        item.setUnitPrice(new BigDecimal("10.00"));
        item.setSubtotal(new BigDecimal("10.00"));
        return item;
    }
}

