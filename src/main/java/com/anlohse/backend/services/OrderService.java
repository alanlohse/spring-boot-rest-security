package com.anlohse.backend.services;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anlohse.backend.model.entities.Order;
import com.anlohse.backend.model.entities.OrderItem;
import com.anlohse.backend.model.entities.User;
import com.anlohse.backend.model.types.OrderStatus;
import com.anlohse.backend.repositories.OrderRepository;

@RestController
@RequestMapping(path = "/order")
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(readOnly = true)
	public @ResponseBody ResponseEntity<?> recuperar(@RequestParam(value = "item", required = true) String item,
			@RequestParam(value = "status", required = true) String status) {
		List<Order> order = orderRepository.findOrderByItemAndStatus(item, OrderStatus.valueOf(status.toUpperCase()));
		if (order == null || order.isEmpty()) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(order.get(0));
		}
	}
	
	@Transactional
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> create(@RequestBody @Valid Order order) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		order.setStore(user.getStore());
		if (order.getStatus() == null) {
			order.setStatus(OrderStatus.NEW);
		}
		order.getItems().forEach(o -> o.setOrder(order));
		orderRepository.save(order);
		return ResponseEntity.status(201).build();
	}
	
	@PostMapping(path = "/refund/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> refund(@PathVariable("id") Long id) {
		Order order = orderRepository.findById(id).orElseGet(null);
		ResponseEntity<Object> res = validateRefunding(id, order);
		if (res != null) return res;
		// Do the magic of refunding and save
		orderRepository.save(order);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping(path = "/refundItem/{id}/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> refundItem(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId) {
		Order order = orderRepository.findById(id).orElseGet(null);
		ResponseEntity<Object> res = validateRefunding(id, order);
		if (res != null) return res;
		OrderItem item = order.getItems().stream()
				.filter(i -> i.getId() == itemId)
				.findFirst().orElse(null);
		if (item == null) {
			return ResponseEntity.notFound().build();
		}
		// Do the magic of refunding and save
		orderRepository.save(order);
		return ResponseEntity.ok().build();
	}

	private ResponseEntity<Object> validateRefunding(Long id, Order order) {
		if (order == null) {
			return ResponseEntity.notFound().build();
		}
		LocalDate date = LocalDate.now();
		LocalDate confirmDatePlus10 = LocalDate.from(order.getConfirmationDate().toInstant()).plusDays(10);
		if (order.getConfirmationDate() != null && date.isAfter(confirmDatePlus10)) {
			return ResponseEntity.badRequest().body("Should be refunded until ten days after confirmation");
		}
		if (!orderRepository.hasPayment(id)) {
			return ResponseEntity.badRequest().body("Order must be paid to refund");
		}
		return null;
	}
	
}
