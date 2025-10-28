package com.nadi.dto;

import com.nadi.dto.account.AccountRequestDto;
import com.nadi.dto.account.AccountResponseDto;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class AccountDtoTest {

    @Test
    void testAccountRequestDto_GettersAndSetters() {
        AccountRequestDto request = new AccountRequestDto();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPhone("+1234567890");

        assertThat(request.getName()).isEqualTo("John Doe");
        assertThat(request.getEmail()).isEqualTo("john@example.com");
        assertThat(request.getPhone()).isEqualTo("+1234567890");
    }

    @Test
    void testAccountResponseDto_CompleteData() {
        AccountResponseDto response = new AccountResponseDto();
        UUID testId = UUID.randomUUID();
        response.setId(testId);
        response.setName("Jane Doe");
        response.setEmail("jane@example.com");
        response.setPhone("+1234567890");
        response.setStatus("ACTIVE");
        response.setType("user");

        assertThat(response.getId()).isEqualTo(testId);
        assertThat(response.getName()).isEqualTo("Jane Doe");
        assertThat(response.getEmail()).isEqualTo("jane@example.com");
        assertThat(response.getPhone()).isEqualTo("+1234567890");
        assertThat(response.getStatus()).isEqualTo("ACTIVE");
        assertThat(response.getType()).isEqualTo("user");
    }

    @Test
    void testAccountResponseDto_UserType() {
        AccountResponseDto response = new AccountResponseDto();
        response.setType("user");
        assertThat(response.getType()).isEqualTo("user");
    }

    @Test
    void testAccountResponseDto_VenueType() {
        AccountResponseDto response = new AccountResponseDto();
        response.setType("venue");
        assertThat(response.getType()).isEqualTo("venue");
    }

    @Test
    void testAccountResponseDto_DeveloperType() {
        AccountResponseDto response = new AccountResponseDto();
        response.setType("developer");
        assertThat(response.getType()).isEqualTo("developer");
    }

    @Test
    void testAccountResponseDto_StatusEnum() {
        AccountResponseDto response = new AccountResponseDto();
        response.setStatus("SUSPENDED");
        assertThat(response.getStatus()).isEqualTo("SUSPENDED");

        response.setStatus("ACTIVE");
        assertThat(response.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void testAccountRequestDto_NullValues() {
        AccountRequestDto request = new AccountRequestDto();
        request.setName(null);
        request.setEmail(null);
        request.setPhone(null);

        assertThat(request.getName()).isNull();
        assertThat(request.getEmail()).isNull();
        assertThat(request.getPhone()).isNull();
    }
}
