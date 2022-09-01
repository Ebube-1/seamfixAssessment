package com.seamfix.bvnvalidation.services;

import com.seamfix.bvnvalidation.models.requests.BvnValidationRequest;
import com.seamfix.bvnvalidation.models.responses.BvnValidationResponse;
import org.springframework.http.ResponseEntity;

public interface BvnValidationService {

    ResponseEntity<BvnValidationResponse> validateBvn(BvnValidationRequest request);
}
