package com.code.shopee.Config;

import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class PagingConfig {
    private int numPage = 4;
}
