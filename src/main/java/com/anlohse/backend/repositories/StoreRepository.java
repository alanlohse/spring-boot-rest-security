package com.anlohse.backend.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.anlohse.backend.model.entities.Store;

public interface StoreRepository extends  CrudRepository<Store, Long> {

	@Query("from Store s where s.name = ?1")
	Store findStoreByName(String name);

}
