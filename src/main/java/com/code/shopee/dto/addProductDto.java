package com.code.shopee.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class addProductDto {
    private String productName;
    private int subcategoryId;
    private String description;
    private int stock;
    private List<variantDto> variants;
}
