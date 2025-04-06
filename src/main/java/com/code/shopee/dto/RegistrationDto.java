package com.code.shopee.dto;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import com.code.shopee.model.Roles;

import jakarta.validation.constraints.NotBlank;
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
public class RegistrationDto {
    @NotBlank(message = "Tên không được để trống")
    private String username;
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @CreationTimestamp
    private LocalDateTime modifiedDate;
    private Set<Roles> roles;
    private Boolean verify;
    private Boolean enable;
    private Boolean status;
}
