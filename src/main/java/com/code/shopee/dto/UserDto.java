package com.code.shopee.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import com.code.shopee.model.Roles;

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
    private String username;
    private String password;
    private Date birthday;
    private String phone;
    private String gmail;
    private String facebook;
    private Date becomeSellerDate;
    private Boolean enable;
    private Boolean status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Set<Roles> roles;
}
