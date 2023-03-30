package com.example.quotation.domain;

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
@Table(name = "subscriptions")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Subscription {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	LocalDate startDate;
	
	LocalDate validUntil;
	
	@OneToOne
	@JoinColumn(name = "quotation_id")
	@RestResource(path = "quotation", rel="quotation")
	Quotation quotation;


}
