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

}
