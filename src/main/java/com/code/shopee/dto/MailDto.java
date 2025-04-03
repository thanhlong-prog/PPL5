package com.code.shopee.dto;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailDto {
    @Email(message = "Email không hợp lệ")
    private String to;
    @NonNull
    private String subject;
    @NonNull
    private String content;
}
