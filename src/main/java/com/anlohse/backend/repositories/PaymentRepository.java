package com.anlohse.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.anlohse.backend.model.entities.Payment;

public interface PaymentRepository extends  CrudRepository<Payment, Long> {

}
