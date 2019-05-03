package com.anlohse.backend.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.anlohse.backend.model.entities.Order;
import com.anlohse.backend.model.types.OrderStatus;

public interface OrderRepository extends  CrudRepository<Order, Long> {

	@Query("from Order o join o.items i where i.description = ?1 and o.status = ?2")
	List<Order> findOrderByItemAndStatus(String item, OrderStatus status);

	@Modifying
	@Query("update Order o set o.status = ?2, o.confirmationDate = ?3 where o.id = ?1")
	void changeStatusAndConfirmation(Long id, OrderStatus status, Date confirmationDate);

	@Query("select count(1) > 0 from Payment p join p.order o where o.id = ?1")
	boolean hasPayment(Long id);

}
