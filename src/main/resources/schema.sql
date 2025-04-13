CREATE TABLE IF NOT EXISTS prescription (
    id INT PRIMARY KEY AUTO_INCREMENT,
    description TEXT,
    file_data LONGBLOB,
    appointment_id INT UNIQUE,
    CONSTRAINT fk_appointment
        FOREIGN KEY (appointment_id)
        REFERENCES appointments(id)
        ON DELETE CASCADE
);
