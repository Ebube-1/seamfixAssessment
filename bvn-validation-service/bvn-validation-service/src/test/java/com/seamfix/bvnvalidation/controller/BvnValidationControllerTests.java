package com.seamfix.bvnvalidation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.seamfix.bvnvalidation.constants.ResponseCode;
import com.seamfix.bvnvalidation.entities.RequestResponseEntry;
import com.seamfix.bvnvalidation.models.dtos.BvnDetails;
import com.seamfix.bvnvalidation.models.requests.BvnValidationRequest;
import com.seamfix.bvnvalidation.repositories.RequestResponseEntryRepository;
import com.seamfix.bvnvalidation.services.impl.BvnDetailsCacheImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author tchineke
 * @since 01 September 2022
 */
@SpringBootTest
@AutoConfigureMockMvc
public class BvnValidationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RequestResponseEntryRepository requestResponseEntryRepository;

    private final Faker faker = new Faker();

    private final String bvnValidationUrl = "/bv-service/svalidate/wrapper";


    @Test
    public void whenValidateBvn_WithValidBvn_ShouldPass() throws Exception {
        BvnDetails validBvnDetails = getValidBvnDetails();
        BvnValidationRequest request = new BvnValidationRequest();
        request.setBvn(validBvnDetails.getBvn());

        mockMvc.perform(post(bvnValidationUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.bvn").value(validBvnDetails.getBvn()))
                .andExpect(jsonPath("$.imageDetail").value(validBvnDetails.getImageDetail()))
                .andExpect(jsonPath("$.basicDetail").value(validBvnDetails.getBasicDetail()));
    }

    @Test
    public void whenValidateBvn_WithEmptyBvn_ShouldFail() throws Exception {
        BvnValidationRequest request = new BvnValidationRequest();
        request.setBvn("");

        mockMvc.perform(post(bvnValidationUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("One or more of your request parameters failed validation. Please retry"))
                .andExpect(jsonPath("$.code").value(ResponseCode.BAD_REQUEST));
    }

    @Test
    public void whenValidateBvn_WithBvnNotFound_ShouldFail() throws Exception {
        String invalidBvn = faker.number().digits(11);
        BvnValidationRequest request = new BvnValidationRequest();
        request.setBvn(invalidBvn);

        mockMvc.perform(post(bvnValidationUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("The searched BVN does not exist"))
                .andExpect(jsonPath("$.code").value(ResponseCode.NOT_FOUND))
                .andExpect(jsonPath("$.bvn").value(invalidBvn));
    }

    @Test
    public void whenValidateBvn_WithInvalidBvnCharacterLength_ShouldFail() throws Exception {
        int invalidBvnLength = faker.number().numberBetween(1, 10);
        String bvnWithInvalidLength = new Faker().number().digits(invalidBvnLength);
        BvnValidationRequest request = new BvnValidationRequest();
        request.setBvn(bvnWithInvalidLength);

        mockMvc.perform(post(bvnValidationUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The searched BVN is invalid"))
                .andExpect(jsonPath("$.code").value(ResponseCode.INVALID_BVN_DIGITS))
                .andExpect(jsonPath("$.bvn").value(bvnWithInvalidLength));
    }

    @Test
    public void whenValidateBvn_WithNonDigitInBvn_ShouldFail() throws Exception {
        String bvnWithNonDigit = new Faker().number().digits(10) + "A";
        BvnValidationRequest request = new BvnValidationRequest();
        request.setBvn(bvnWithNonDigit);

        mockMvc.perform(post(bvnValidationUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The searched BVN is invalid"))
                .andExpect(jsonPath("$.code").value(ResponseCode.BAD_REQUEST))
                .andExpect(jsonPath("$.bvn").value(bvnWithNonDigit));
    }

    @Test
    public void whenValidateBvn_ShouldLogRequestAndResponseInMongoDb() throws Exception {
        BvnDetails validBvnDetails = getValidBvnDetails();
        BvnValidationRequest request = new BvnValidationRequest();
        request.setBvn(validBvnDetails.getBvn());

        ArgumentCaptor<RequestResponseEntry> logEntryArgumentCaptor = ArgumentCaptor.forClass(RequestResponseEntry.class);
        doReturn(null).when(requestResponseEntryRepository).save(logEntryArgumentCaptor.capture());

        String requestPayload = objectMapper.writeValueAsString(request);

        String responsePayload = mockMvc.perform(post(bvnValidationUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestPayload))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();

        verify(requestResponseEntryRepository, times(1)).save(any(RequestResponseEntry.class));

        RequestResponseEntry logEntry = logEntryArgumentCaptor.getValue();
        Assertions.assertEquals(requestPayload, logEntry.getRequestPayload());
        Assertions.assertEquals(responsePayload, logEntry.getResponsePayload());
        Assertions.assertNotNull(logEntry.getId());
    }

    private BvnDetails getValidBvnDetails() {
        return BvnDetailsCacheImpl.BVN_DETAILS.stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("No BVN details have been configured"));
    }
}
