package com.code.shopee.enums;

public enum Role {
    ADMIN(1),
    BUYER(2),
    SELLER(3);

    private final int code;

    Role(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
