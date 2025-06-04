package com.code.shopee.dto;

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
public class SellerBecomeDto {
    @NotBlank(message = "Tên cửa hàng không được để trống")
    private String shopName;
    @NotBlank(message = "Họ và tên không được để trống")
    private String fullname;
    @NotBlank(message = "Phường/xã không được để trống")
    private String ward;
    @NotBlank(message = "Địa chỉ đầy đủ không được để trống")
    private String fullAddress;
    @NotBlank(message = "Quận/huyện không được để trống")
    private String district;
    @NotBlank(message = "Tỉnh/Thành phố không được để trống")
    private String province;
    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;
    private String company;
    @NotBlank(message = "Email không được để trống")
    private String email;
    @NotBlank(message = "Email nhận hóa đơn không được để trống")
    private String emailReceiveBill;
    @NotBlank(message = "Địa chỉ nhận hóa đơn không được để trống")
    private String address;
    @NotBlank(message = "Mã số thuế không được để trống")
    private String taxCode;
    @NotBlank(message = "Loại hình kinh doanh không được để trống")
    private String businessType;
    @NotBlank(message = "Số CMND/CCCD không được để trống")
    private String identificationNumber;
    @NotBlank(message = "Tên trên CMND/CCCD không được để trống")
    private String identificationName;
    private String identificationFront;
    private String identificationBack;
}
