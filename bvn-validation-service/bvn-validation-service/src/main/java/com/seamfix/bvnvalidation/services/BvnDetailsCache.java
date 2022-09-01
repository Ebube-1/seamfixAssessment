package com.seamfix.bvnvalidation.services;

import com.seamfix.bvnvalidation.models.dtos.BvnDetails;

import java.util.Optional;

public interface BvnDetailsCache {

    Optional<BvnDetails> findByBvn(String bvn);
}