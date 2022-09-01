package com.seamfix.bvnvalidation.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "RequestResponseEntries")
@Data
public class RequestResponseEntry {

    @Id
    private UUID id;
    private String requestPayload;
    private String responsePayload;
}
