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
@Table(name="follow")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Folow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "following_user_id", referencedColumnName = "id")
    private User followingUser;

    @ManyToOne
    @JoinColumn(name = "followed_user_id", referencedColumnName = "id")
    private User followedUser;

    @Column(name = "status")
    private int status;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDate createdDate;
}
