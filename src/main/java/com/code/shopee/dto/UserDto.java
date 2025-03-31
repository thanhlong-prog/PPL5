package com.code.shopee.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import com.code.shopee.model.Roles;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class UserDto {
    @NotBlank(message = "Tên không được để trống")
    private String username;

    @NotBlank(message= "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;
    private Date birthday;

    private String phone;

    @Email(message = "Email không hợp lệ")
    private String gmail;

    private String facebook;
    private Date becomeSellerDate;
    private Boolean enable;
    private Boolean status;
    @NotEmpty
    private LocalDateTime createdDate;
    @NotEmpty
    private LocalDateTime modifiedDate;
    @NotEmpty
    private Set<Roles> roles;
}
