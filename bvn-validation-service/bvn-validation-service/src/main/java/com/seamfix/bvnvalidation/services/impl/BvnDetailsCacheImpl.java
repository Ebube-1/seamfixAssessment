package com.seamfix.bvnvalidation.services.impl;

import com.seamfix.bvnvalidation.models.dtos.BvnDetails;
import com.seamfix.bvnvalidation.services.BvnDetailsCache;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BvnDetailsCacheImpl implements BvnDetailsCache {

    public static final List<BvnDetails> BVN_DETAILS = List.of(
            composeBvnDetails("11111111111"),
            composeBvnDetails("22222222222"),
            composeBvnDetails("33333333333"),
            composeBvnDetails("44444444444"),
            composeBvnDetails("55555555555"),
            composeBvnDetails("66666666666"),
            composeBvnDetails("77777777777"),
            composeBvnDetails("88888888888"),
            composeBvnDetails("99999999999")
    );

    private static BvnDetails composeBvnDetails(String bvn) {
        return BvnDetails.builder()
                .bvn(bvn)
                .imageDetail(getDefaultBase64Image())
                .basicDetail(getDefaultBase64Image())
                .build();
    }

    private static String getDefaultBase64Image() {
        return "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";
    }

    @Override
    public Optional<BvnDetails> findByBvn(String bvn) {
        return BVN_DETAILS.stream()
                .filter(bvnDetail -> bvnDetail.getBvn().equals(bvn))
                .findFirst();
    }
}
