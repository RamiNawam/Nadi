package com.nadi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.TypeAlias;

@Data
@EqualsAndHashCode(callSuper = true)
@TypeAlias("developer")
public class DeveloperAccount extends Account {
    public void reviewApplication() {
        /* admin-only logic */
    }
}

