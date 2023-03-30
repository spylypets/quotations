package com.example.quotation.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.quotation.domain.Customer;

@RepositoryRestResource
public interface CustomerRepository extends CrudRepository<Customer, Long>  {

}
