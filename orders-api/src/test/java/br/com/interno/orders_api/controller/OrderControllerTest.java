package br.com.interno.orders_api.controller;

import br.com.interno.orders_api.enums.OrderStatus;
import br.com.interno.orders_api.exception.OrderNotFoundException;
import br.com.interno.orders_api.model.Order;
import br.com.interno.orders_api.model.OrderItem;
import br.com.interno.orders_api.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Test
    void shouldReturnOrderWhenFound() throws Exception {
        Order order = createOrder(1L);

        when(orderService.findById(1L))
                .thenReturn(order);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturn404WhenOrderNotFound() throws Exception {
        when(orderService.findById(1L))
                .thenThrow(new OrderNotFoundException(1L));

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListOrdersWithPagination() throws Exception {
        List<Order> orders = List.of(createOrder(1L), createOrder(2L));
        Page<Order> page = new PageImpl<>(orders);

        when(orderService.findAll(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/orders")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void shouldCreateOrderSuccessfully() throws Exception {
        Order order = createOrder(1L);

        when(orderService.create(any(Order.class)))
                .thenReturn(order);

        String requestBody = """
                {
                    "customerName": "João da Silva",
                    "customerEmail": "joao@email.com"
                }
                """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("João da Silva"));
    }

    @Test
    void shouldReturn400WhenCreateOrderWithInvalidData() throws Exception {
        String requestBody = """
                {
                    "customerName": "",
                    "customerEmail": "invalid-email"
                }
                """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateOrderSuccessfully() throws Exception {
        Order order = createOrder(1L);
        order.setStatus(OrderStatus.CONFIRMED);

        when(orderService.update(eq(1L), any(Order.class)))
                .thenReturn(order);

        String requestBody = """
                {
                    "customerName": "João Atualizado",
                    "customerEmail": "joao.novo@email.com",
                    "status": "CONFIRMED"
                }
                """;

        mockMvc.perform(put("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void shouldReturn404WhenUpdateNonExistentOrder() throws Exception {
        when(orderService.update(eq(99L), any(Order.class)))
                .thenThrow(new OrderNotFoundException(99L));

        String requestBody = """
                {
                    "customerName": "João",
                    "customerEmail": "joao@email.com",
                    "status": "PENDING"
                }
                """;

        mockMvc.perform(put("/api/orders/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteOrderSuccessfully() throws Exception {
        doNothing().when(orderService).delete(1L);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());

        verify(orderService).delete(1L);
    }

    @Test
    void shouldReturn404WhenDeleteNonExistentOrder() throws Exception {
        doThrow(new OrderNotFoundException(99L))
                .when(orderService).delete(99L);

        mockMvc.perform(delete("/api/orders/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListOrderItems() throws Exception {
        List<OrderItem> items = List.of(createOrderItem(1L), createOrderItem(2L));

        when(orderService.listItems(1L))
                .thenReturn(items);

        mockMvc.perform(get("/api/orders/1/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldReturn404WhenListItemsOfNonExistentOrder() throws Exception {
        when(orderService.listItems(99L))
                .thenThrow(new OrderNotFoundException(99L));

        mockMvc.perform(get("/api/orders/99/items"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldAddItemToOrder() throws Exception {
        Order order = createOrder(1L);
        order.setTotalAmount(new BigDecimal("99.80"));

        when(orderService.addItem(eq(1L), any(OrderItem.class)))
                .thenReturn(order);

        String requestBody = """
                {
                    "productName": "Camiseta Azul",
                    "quantity": 2,
                    "unitPrice": 49.90
                }
                """;

        mockMvc.perform(post("/api/orders/1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(99.80));
    }

    @Test
    void shouldReturn400WhenAddItemWithInvalidData() throws Exception {
        String requestBody = """
                {
                    "productName": "",
                    "quantity": 0,
                    "unitPrice": -10
                }
                """;

        mockMvc.perform(post("/api/orders/1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
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

