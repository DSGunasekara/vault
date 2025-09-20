package com.dilain.vault.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue()
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn()
    private User user;

    @Column()
    private String name;

    @Column()
    private Instant createdAt = Instant.now();

    @Column()
    private Instant updatedAt = Instant.now();

}
