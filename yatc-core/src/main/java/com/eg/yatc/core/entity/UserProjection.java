package com.eg.yatc.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


@Entity
public class UserProjection extends BaseEntity {

    @Id
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
