# BVN VALIDATION SERVICE

## DESCRIPTION
This is a service to validate BVNs, preconfigured in memory.

Valid BVNs can be found in the BvnDetailsCacheImpl class.
It is stored in the BVN_DETAILS field.

Request and response objects are stored in a MongoDb NoSql store,
based on the configured properties.

## PREREQUISITES
1. MongoDb must be running locally