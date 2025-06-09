package com.code.shopee.model;

import java.time.LocalDateTime;

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
@Table(name = "ban_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BanInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private User user;

    @Column(name = "reason")
    private String reason;

    @Column(name = "ban_type")
    private String banType;

    @Column(name = "ban_date")
    private LocalDateTime banDate;

    @Column(name = "unban_date")
    private LocalDateTime unbanDate;

    @Column(name = "status")
    private Boolean status;
}
