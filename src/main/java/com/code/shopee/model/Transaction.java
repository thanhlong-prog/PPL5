package com.code.shopee.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "trace_id")
    private String traceId;

    @Column(name = "vnp_order_info")
    private String vnpOrderInfo;

    @Column(name = "vnp_transaction_no")
    private String vnpTransactionNo;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private User order;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    private List<Cart> carts;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "status")
    private int status;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_date")
    private LocalDate modifiedDate;
}
