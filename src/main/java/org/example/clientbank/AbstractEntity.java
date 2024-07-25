package org.example.clientbank;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.TemporalType.TIMESTAMP;
import static lombok.AccessLevel.PUBLIC;

@MappedSuperclass
@FieldDefaults(level = PUBLIC)
@Data
public abstract class AbstractEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @CreatedDate
    @Temporal(TIMESTAMP)
    @Column(updatable = false, nullable = false)
    LocalDateTime createdDate;

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    @Column(nullable = false)
    LocalDateTime lastModifiedDate;
}
