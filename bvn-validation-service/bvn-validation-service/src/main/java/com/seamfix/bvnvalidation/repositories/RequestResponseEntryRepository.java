package com.seamfix.bvnvalidation.repositories;

import com.seamfix.bvnvalidation.entities.RequestResponseEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface RequestResponseEntryRepository extends MongoRepository<RequestResponseEntry, UUID> {
}
