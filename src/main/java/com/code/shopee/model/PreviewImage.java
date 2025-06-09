package com.code.shopee.model;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "preview_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PreviewImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "preview_id", referencedColumnName = "id")
    private ProductPreview productPreview;

    @Column(name = "image_url")
    private String imageUrl; 

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDate createdDate;
}