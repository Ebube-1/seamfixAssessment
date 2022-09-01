package com.seamfix.bvnvalidation.services.impl;

import com.seamfix.bvnvalidation.constants.ResponseCode;
import com.seamfix.bvnvalidation.models.dtos.BvnDetails;
import com.seamfix.bvnvalidation.models.requests.BvnValidationRequest;
import com.seamfix.bvnvalidation.models.responses.BvnValidationResponse;
import com.seamfix.bvnvalidation.services.BvnDetailsCache;
import com.seamfix.bvnvalidation.services.BvnValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BvnValidationServiceImpl implements BvnValidationService {

    private final BvnDetailsCache bvnDetailsCache;

    @Override
    public ResponseEntity<BvnValidationResponse> validateBvn(BvnValidationRequest request) {
        String bvn = request.getBvn();
        if (!hasValidDigitCount(bvn)) {
            return new ResponseEntity<>(getInvalidBvnDigitsCountResponse(bvn), HttpStatus.BAD_REQUEST);
        }
        if (!containsOnlyDigits(bvn)) {
            return new ResponseEntity<>(getInvalidBvnWithNonDigitsResponse(bvn), HttpStatus.BAD_REQUEST);
        }

        Optional<BvnDetails> optionalBvnDetails = bvnDetailsCache.findByBvn(bvn);
        if (optionalBvnDetails.isEmpty()) {
            return new ResponseEntity<>(getBvnNotFoundResponse(bvn), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(getValidBvnResponse(optionalBvnDetails.get()), HttpStatus.OK);
    }

    private boolean hasValidDigitCount(String bvn) {
        int validDigitCount = 11;
        return bvn.length() == validDigitCount;
    }

    private BvnValidationResponse getInvalidBvnDigitsCountResponse(String bvn) {
        BvnValidationResponse response = new BvnValidationResponse();
        response.setMessage("The searched BVN is invalid");
        response.setCode(ResponseCode.INVALID_BVN_DIGITS);
        response.setBvn(bvn);
        return response;
    }

    private boolean containsOnlyDigits(String bvn) {
        for (int i = 0; i < bvn.length(); i++) {
            char currentChar = bvn.charAt(i);
            boolean isNumber = currentChar >= '0' && currentChar <= '9';
            if (!isNumber)
                return false;
        }
        return true;
    }

    private BvnValidationResponse getInvalidBvnWithNonDigitsResponse(String bvn) {
        BvnValidationResponse response = new BvnValidationResponse();
        response.setMessage("The searched BVN is invalid");
        response.setCode(ResponseCode.BAD_REQUEST);
        response.setBvn(bvn);
        return response;
    }

    private BvnValidationResponse getBvnNotFoundResponse(String bvn) {
        BvnValidationResponse response = new BvnValidationResponse();
        response.setMessage("The searched BVN does not exist");
        response.setCode(ResponseCode.NOT_FOUND);
        response.setBvn(bvn);
        return response;
    }

    private BvnValidationResponse getValidBvnResponse(BvnDetails bvnDetails) {
        BvnValidationResponse response = new BvnValidationResponse();
        response.setMessage("SUCCESS");
        response.setCode(ResponseCode.SUCCESS);
        response.setBvn(bvnDetails.getBvn());
        response.setBasicDetail(bvnDetails.getBasicDetail());
        response.setImageDetail(bvnDetails.getImageDetail());
        return response;
    }
}
