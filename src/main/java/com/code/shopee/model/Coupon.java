package com.code.shopee.model;

import java.time.LocalDate;

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
@Table(name="coupon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "type_coupon")
    private int typeCoupon;

    @Column(name = "type_discount")
    private int typeDiscount;

    @Column(name = "min_price")
    private int minPrice;

    @Column(name = "discount")
    private int discount;

    @Column(name = "max_discount")
    private int maxDiscount;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private User created_by;

    @Column(name = "status")
    private int status;

    @Column(name = "expired_date")
    private LocalDate expiedDate;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDate createdDate;

    @Column(name = "modified_date")
    @UpdateTimestamp
    private LocalDate modifiedDate;
}
