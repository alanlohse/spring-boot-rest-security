package com.anlohse.backend.services;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anlohse.backend.model.entities.Payment;
import com.anlohse.backend.model.types.OrderStatus;
import com.anlohse.backend.repositories.OrderRepository;
import com.anlohse.backend.repositories.PaymentRepository;

@RestController
@RequestMapping(path = "/payment")
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Transactional
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> create(@RequestBody @Valid Payment payment) {
		paymentRepository.save(payment);
		orderRepository.changeStatusAndConfirmation(payment.getOrder().getId(), OrderStatus.PAID, new Date());
		return ResponseEntity.status(201).build();
	}
	
}
