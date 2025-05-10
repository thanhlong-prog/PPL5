package com.code.shopee.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDto implements Serializable {
    private String status;
    private String message;
    private String URL;
}
