package com.socialnetwork.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name="refresh_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    private UUID id;
    private UUID userId;
    private String refreshToken;
    private Timestamp expires;
    private boolean isRevoked;
    private Timestamp lastUsedAt;
}
