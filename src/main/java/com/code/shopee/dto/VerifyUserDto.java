package com.code.shopee.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class VerifyUserDto {
    private String username;

    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Gmail không được để trống")
    @Email(message = "Email không hợp lệ")
    private String gmail;

    @NotBlank(message = "Code xác nhận gmail không được để trống")
    @Size(min = 6, max = 6, message = "Code xác nhận gmail không hợp lệ")
    private String codeMail;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Size(min = 10, max = 11, message = "Số điện thoại không hợp lệ")
    private String phone;

    @NotBlank(message = "Code xác nhận điện thoại không được để trống")
    @Size(min = 6, max = 6, message = "Code xác nhận điện thoại không hợp lệ")
    private String codePhone;

    private LocalDateTime modifiedDate;
    private Boolean verify;
}
