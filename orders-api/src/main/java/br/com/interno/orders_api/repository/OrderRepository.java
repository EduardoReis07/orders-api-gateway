package br.com.interno.orders_api.repository;

import br.com.interno.orders_api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
