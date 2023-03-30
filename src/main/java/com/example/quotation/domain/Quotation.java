package com.example.quotation.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.rest.core.annotation.RestResource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quotations")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Quotation {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	LocalDate beginingOfInsurance;
	
	BigDecimal insuredAmount;
	
	LocalDate dateOfSigningMortgage;
	
	@OneToOne
	@JoinColumn(name = "customer_id")
	@RestResource(path = "customer", rel="customer")
	Customer customer;

}
