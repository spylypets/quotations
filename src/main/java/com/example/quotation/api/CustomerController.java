package com.example.quotation.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;

import com.example.quotation.repository.CustomerRepository;

@RepositoryRestController
public class CustomerController {
	
	@Autowired
	CustomerRepository repository;
/*		
	@RequestMapping("/customers")
	public Iterable<Customer> getCars() {
		return repository.findAll();
	}

	@RequestMapping(value="/customers", method=RequestMethod.PUT)
	public void saveCustomer(@RequestBody Customer customer) {
		repository.save(customer);
	}
	*/

}
