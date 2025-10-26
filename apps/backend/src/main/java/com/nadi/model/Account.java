package com.nadi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("accounts")
public abstract class Account {
    @Id
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private AccountStatus status = AccountStatus.ACTIVE;

    public boolean authenticate() {
        return true;
    }

    public void deactivate() {
        this.status = AccountStatus.SUSPENDED;
    }
}

