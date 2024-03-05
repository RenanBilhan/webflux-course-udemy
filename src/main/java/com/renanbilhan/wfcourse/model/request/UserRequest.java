package com.renanbilhan.wfcourse.model.request;

import com.renanbilhan.wfcourse.validator.TrimString;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @TrimString
        @Size(min = 3, max = 50, message = "Must contain between 3 and 50 characters")
        @NotBlank(message = "Must not be null or empty")
        String name,

        @Email(message = "Invalid email")
        String email,

        @TrimString
        @Size(min = 3, max = 50, message = "Must contain between 3 and 50 characters")
        @NotBlank(message = "Must not be null or empty")
        String password
) {
}
