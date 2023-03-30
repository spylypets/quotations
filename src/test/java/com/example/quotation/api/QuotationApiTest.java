package com.example.quotation.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
public class QuotationApiTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Test
	public void testCreateQuotation() throws Exception {
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
        		.content("{\"beginingOfInsurance\": \""+LocalDate.now().plusMonths(1)+"\",\n"
        				+ "    \"insuredAmount\": \"1000\",\n"
        				+ "    \"dateOfSigningMortgage\": \""+LocalDate.now()+"\"}")
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
        //Check attributes of the associated Customer
        this.mockMvc.perform(get(quotationCustomerUrl).accept(MediaType.APPLICATION_JSON))
         		.andDo(print())
        		.andExpect(status().isOk())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(content().json("{\"firstName\": \"Dana\",\n"
        				+ "    \"middleName\": \"M1\",\n"
        				+ "    \"lastName\": \"Czechova\",\n"
        				+ "    \"phoneNumber\": \"123456789\",\n"
        				+ "    \"email\": \"Dana@excalibur.cz\",\n"
        				+ "    \"birthDate\": \"1995-12-12\"}", false));
	}

}
