package com.seamfix.bvnvalidation.controllers;

import com.seamfix.bvnvalidation.models.requests.BvnValidationRequest;
import com.seamfix.bvnvalidation.models.responses.BvnValidationResponse;
import com.seamfix.bvnvalidation.services.BvnValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/bv-service")
@RequiredArgsConstructor
public class BvnValidationController {

    private final BvnValidationService bvnValidationService;


    @PostMapping("/svalidate/wrapper")
    public ResponseEntity<BvnValidationResponse> validateBvn(@Valid @RequestBody BvnValidationRequest request) {
        return bvnValidationService.validateBvn(request);
    }
}
