package com.seamfix.bvnvalidation.models.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BvnDetails {

    private long id;

    private String bvn;

    private String imageDetail;

    private String basicDetail;
}
