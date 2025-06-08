package com.code.shopee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.code.shopee.model.ChatMessage;
import com.code.shopee.model.User;
import com.google.protobuf.Int64ValueOrBuilder;

public interface ChatMessageRepo extends JpaRepository<ChatMessage, Int64ValueOrBuilder> {
    List<ChatMessage> findBySenderAndRecipient(User sender, User recipient);

    List<ChatMessage> findByRoomIdOrderByTimestampAsc(String roomId);

    List<ChatMessage> findByRoomId(String roomId);

    @Query("SELECT DISTINCT c.roomId FROM ChatMessage c WHERE c.sender.name = :username OR c.recipient.name = :username")
    List<String> findRoomIdsByUser(@Param("username") String username);

}
