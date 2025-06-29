package org.example.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class User {

    String userId;
    String name;
    String cardId;
    Boolean isActive;
    String qrCode;
    LocalDateTime updatedAt;
    LocalDateTime createdAt;

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", cardId='" + cardId + '\'' +
                ", isActive=" + isActive +
                ", qrCode='" + qrCode + '\'' +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
