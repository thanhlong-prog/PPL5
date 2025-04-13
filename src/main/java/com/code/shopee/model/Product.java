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
@Table(name="product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private User seller;

    @Column(name = "title")
    private String title;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "subcategory_id")
    private int subcategoryId;

    @Column(name = "price")
    private double price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "liked")
    private int liked;

    @Column(name = "rated")
    private int rated;

    @Column(name = "rating")
    private double rating;

    @Column(name = "sold")
    private int sold;

    @Column(name = "refund_type")
    private Integer refundType;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private int status;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDate createdDate;

    @Column(name = "modified_date")
    @UpdateTimestamp
    private LocalDate modifiedDate;
}
