package com.code.shopee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatRoomDto {
    private String user;
    private String lastMessage;
    private String avatarUrl;
}