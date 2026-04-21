CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL,
    professional_id BIGINT,
    appointment_time TIMESTAMP NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES users(id),
    CONSTRAINT fk_professional FOREIGN KEY (professional_id) REFERENCES users(id)
);