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
@Table(name="product_option")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "id")
    private Product product;

    @Column(name="version")
    private String version;
    
    @Column(name="size")
    private String size;

    @Column(name="color")
    private String color;

    @Column(name="quantity")
    private int quantity;

    @Column(name="price")
    private int price;

    @Column(name="description")
    private String description;

    @ManyToOne
    @JoinColumn(name="created_by", referencedColumnName = "id")
    private User createdBy;

    @Column(name="status")
    private int status;

    @Column(name="created_date")
    @CreationTimestamp
    private LocalDate createdDate;

    @Column(name="modified_date")
    @UpdateTimestamp
    private LocalDate modifiedDate;
}
