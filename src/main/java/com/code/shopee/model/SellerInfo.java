package com.code.shopee.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "seller_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellerInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "ward")
    private String ward;

    @Column(name = "full_address")
    private String fullAddress;

    @Column(name = "district")
    private String district;

    @Column(name = "province")
    private String province;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "company")
    private String company;

    @Column(name = "email_receive_bill")
    private String emailReceiveBill;

    @Column(name = "address")
    private String address;

    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "status")
    private int status;

    @Column(name = "is_verify")
    private boolean isVerify;

    @Column(name = "business_type")
    private String businessType;

    @Column(name = "identification_number")
    private String identificationNumber;

    @Column(name = "identification_name")
    private String identificationName;

    @Column(name = "identification_front")
    private String identificationFront;

    @Column(name = "identification_back")
    private String identificationBack;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    @UpdateTimestamp
    private LocalDateTime modifiedDate;

}
