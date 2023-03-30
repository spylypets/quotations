package com.example.quotation.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.quotation.domain.Subscription;

@RepositoryRestResource
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

}
