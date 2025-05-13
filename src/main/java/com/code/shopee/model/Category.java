package com.code.shopee.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "category")
    private List<Subcategory> subcategories;


    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_date")
    private LocalDate modifiedDate;
}
