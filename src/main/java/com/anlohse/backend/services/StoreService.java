package com.anlohse.backend.services;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anlohse.backend.model.entities.Store;
import com.anlohse.backend.repositories.StoreRepository;

@RestController
@RequestMapping(path = "/store")
public class StoreService {

	@Autowired
	private StoreRepository storeRepository;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(readOnly = true)
	public @ResponseBody ResponseEntity<?> recuperar(@RequestParam(value = "name", required = true) String name) {
		Store store = storeRepository.findStoreByName(name);
		if (store == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(store);
		}
	}
	
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> create(@RequestBody @Valid Store store) {
		storeRepository.save(store);
		return ResponseEntity.status(201).build();
	}
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<?> update(@RequestBody @Valid Store store) {
		storeRepository.save(store);
		return ResponseEntity.status(201).build();
	}
	
}
