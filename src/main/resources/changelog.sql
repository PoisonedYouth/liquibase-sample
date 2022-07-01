-- liquibase formatted sql

-- changeset liquibase:2
CREATE TABLE address(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    city VARCHAR(255) NOT NULL,
    zip_code VARCHAR(255) NOT NULL,
    street VARCHAR(255) NOT NULL,
    street_number INT
);
ALTER TABLE customer DROP COLUMN city;
ALTER TABLE customer ADD COLUMN address_id BIGINT NOT NULL;
ALTER TABLE customer ADD CONSTRAINT fk FOREIGN KEY(address_id) REFERENCES address(id);

