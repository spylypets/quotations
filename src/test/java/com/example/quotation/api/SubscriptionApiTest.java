package com.example.quotation.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class SubscriptionApiTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Test
	public void testCreateSubscription() throws Exception {
		
		LocalDate beginingOfInsuranceDate = LocalDate.now().plusMonths(1);
		LocalDate dateOfSigningMortgage = LocalDate.now();
		
		//Create a new Customer
        MvcResult result = this.mockMvc.perform(post("/api/customers")
        		.content("{\"firstName\": \"Dana\",\n"
        				+ "    \"middleName\": \"M1\",\n"
        				+ "    \"lastName\": \"Czechova\",\n"
        				+ "    \"phoneNumber\": \"123456789\",\n"
        				+ "    \"email\": \"Dana@excalibur.cz\",\n"
        				+ "    \"birthDate\": \"1995-12-12\"}")
        		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
        		.andDo(print())
        		.andExpect(status().isCreated())
        		.andReturn();
        String customerUrl = result.getResponse().getHeader("Location");
		assertNotNull(customerUrl);
        //Create a new Quotation
        result = this.mockMvc.perform(post("/api/quotations")
        		.content("{\"beginingOfInsurance\": \"" + beginingOfInsuranceDate + "\",\n"
        				+ "    \"insuredAmount\": 1000.0,\n"
        				+ "    \"dateOfSigningMortgage\": \"" + dateOfSigningMortgage + "\"}")
        		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
        		.andDo(print())
        		.andExpect(status().isCreated())
        		.andReturn();
        String quotationUrl = result.getResponse().getHeader("Location");
		assertNotNull(quotationUrl);
		String quotationCustomerUrl = quotationUrl + "/customer";
        //Create association between the Quotation and the Customer
        this.mockMvc.perform(put(quotationCustomerUrl)
        		.content(customerUrl)
        		.header(HttpHeaders.CONTENT_TYPE, "text/uri-list"))
        		.andDo(print())
        		.andExpect(status().isNoContent());
        //Create a new Subscription
        result = this.mockMvc.perform(post("/api/subscriptions")
        		.content("{\"startDate\": \"" + dateOfSigningMortgage + "\",\n"
         				+ "    \"validUntil\": \"" + beginingOfInsuranceDate + "\"}")
        		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
        		.andDo(print())
        		.andExpect(status().isCreated())
        		.andReturn();
        String subscriptionUrl = result.getResponse().getHeader("Location");
		assertNotNull(subscriptionUrl);
		String subscriptionQuotationUrl = subscriptionUrl + "/quotation";
        //Create association between the new Subscription and the Quotation
        this.mockMvc.perform(put(subscriptionQuotationUrl)
        		.content(quotationUrl)
        		.header(HttpHeaders.CONTENT_TYPE, "text/uri-list"))
        		.andDo(print())
        		.andExpect(status().isNoContent());
        //Check the object hierarchy
        this.mockMvc.perform(get(subscriptionQuotationUrl).accept(MediaType.APPLICATION_JSON))
 		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(content().json("{\"beginingOfInsurance\": \"" + beginingOfInsuranceDate + "\",\n"
						+ "    \"insuredAmount\": 1000.0,\n"
						+ "    \"dateOfSigningMortgage\": \"" + dateOfSigningMortgage + "\"}", false))
		.andExpect(jsonPath("$._links.customer.href").value(quotationCustomerUrl));
	}
}
