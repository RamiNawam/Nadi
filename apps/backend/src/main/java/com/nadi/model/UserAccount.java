package com.nadi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.TypeAlias;

@Data
@EqualsAndHashCode(callSuper = true)
@TypeAlias("user")
public class UserAccount extends Account {
    public void search(Object criteria) {
    }

    public void reserve(String courtId, Object slot) {
    }
}

