package com.example.quotation.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
public class CustomerApiTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Test
	public void testUpdateCustomer() throws Exception {
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
		//String customerId = customerUrl.substring(customerUrl.lastIndexOf('/') + 1);
        //Check the new Customer data
        this.mockMvc.perform(get(customerUrl).accept(MediaType.APPLICATION_JSON))
         		.andDo(print())
        		.andExpect(status().isOk())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(content().json("{\"firstName\": \"Dana\",\n"
        				+ "    \"middleName\": \"M1\",\n"
        				+ "    \"lastName\": \"Czechova\",\n"
        				+ "    \"phoneNumber\": \"123456789\",\n"
        				+ "    \"email\": \"Dana@excalibur.cz\",\n"
        				+ "    \"birthDate\": \"1995-12-12\"}", false));
      //Update/remove the Customer attributes
        this.mockMvc.perform(put(customerUrl)
        		.content("{\"firstName\": \"Dana\",\n"
        				+ "    \"middleName\": \"M2\",\n"
        				+ "    \"lastName\": \"Czechova\",\n"
         				+ "    \"email\": \"Dana@seznam.cz\",\n"
        				+ "    \"birthDate\": \"2000-12-12\"}")
        		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
        		.andDo(print())
        		.andExpect(redirectedUrl(customerUrl));
        //Check the updated/removed attributes
        this.mockMvc.perform(get(customerUrl).accept(MediaType.APPLICATION_JSON))
 		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(content().json("{\"firstName\": \"Dana\",\n"
				+ "    \"middleName\": \"M2\",\n"
				+ "    \"lastName\": \"Czechova\",\n"
				+ "    \"phoneNumber\": null,\n"
				+ "    \"email\": \"Dana@seznam.cz\",\n"
				+ "    \"birthDate\": \"2000-12-12\"}", false));
	}

}
