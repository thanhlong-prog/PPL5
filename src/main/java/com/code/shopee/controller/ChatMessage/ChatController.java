package com.code.shopee.controller.ChatMessage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.code.shopee.dto.ChatMessageDto;
import com.code.shopee.model.ChatMessage;
import com.code.shopee.model.CustomUserDetails;
import com.code.shopee.model.User;
import com.code.shopee.repository.ChatMessageRepo;
import com.code.shopee.repository.UserRepository;
import com.code.shopee.service.UserService;

@Controller
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageRepo chatMessageRepo;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/create-room")
    public String createRoom(@RequestParam("otherUser") int otherUserId,
            RedirectAttributes redirectAttributes) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = null; 

        if (principal instanceof CustomUserDetails user) {
            currentUser = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            currentUser = userService.findByGmail(oauth2User.getAttribute("email"));
        }

        if (currentUser == null) {
            return "redirect:/login";
        }

        User otherUser = userService.findById(otherUserId);
        if (otherUser == null) {
            return "redirect:/chat";
        }

        String roomId = getRoomId(currentUser.getName(), otherUser.getName());

        if (chatMessageRepo.findByRoomIdOrderByTimestampAsc(roomId).isEmpty()) {
            ChatMessage initialMessage = new ChatMessage();
            initialMessage.setSender(currentUser);
            initialMessage.setRecipient(otherUser);
            initialMessage.setContent("Hello " + otherUser.getName());
            initialMessage.setTimestamp(LocalDateTime.now());
            initialMessage.setRoomId(roomId);

            chatMessageRepo.save(initialMessage);
        } else {
        }

        redirectAttributes.addAttribute("roomId", URLEncoder.encode(roomId, StandardCharsets.UTF_8));
        // ?rommId=" + URLEncoder.encode(roomId, StandardCharsets.UTF_8);
        return "redirect:/chat";
    }

    @GetMapping("")
    public String chatPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        Object principal = auth.getPrincipal();
        User consumer = null;

        if (principal instanceof CustomUserDetails user) {
            consumer = userService.findByUsername(user.getUsername());
        } else if (principal instanceof OAuth2User oauth2User) {
            consumer = userService.findByGmail(oauth2User.getAttribute("email"));
        }

        if (consumer == null) {
            return "redirect:/login";
        }

        String currentUserName = consumer.getName();

        model.addAttribute("currentUser", currentUserName);

        return "chat/chat";
    }

    private String generateRoomId(String user1, String user2) {
        return Stream.of(user1, user2)
                .sorted()
                .collect(Collectors.joining("_"));
    }

    @MessageMapping("/message")
    public void sendMessage(@Payload ChatMessageDto message) {
        User sender = userRepository.findByNameAndStatusTrue(message.getSender());
        User recipient = userRepository.findByNameAndStatusTrue(message.getRecipient());

        String roomId = getRoomId(sender.getName(), recipient.getName());

        ChatMessage entity = new ChatMessage();
        entity.setSender(sender);
        entity.setRecipient(recipient);
        entity.setContent(message.getContent());
        entity.setTimestamp(LocalDateTime.now());
        entity.setRoomId(roomId);

        chatMessageRepo.save(entity);

        messagingTemplate.convertAndSend("/topic/chatroom/" + roomId, message);
    }

    @GetMapping("/history/{recipient}")
    @ResponseBody
    public List<ChatMessageDto> getChatHistory(@PathVariable("recipient") String recipientName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        final String currentUserName;

        if (principal instanceof CustomUserDetails user) {
            User consumer = userService.findByUsername(user.getUsername());
            if (consumer == null)
                return Collections.emptyList();
            currentUserName = consumer.getName(); // lấy getName() như bạn muốn
        } else if (principal instanceof OAuth2User oauth2User) {
            User consumer = userService.findByGmail(oauth2User.getAttribute("email"));
            if (consumer == null)
                return Collections.emptyList();
            currentUserName = consumer.getName(); // lấy getName()
        } else {
            return Collections.emptyList(); // nếu không phải 2 kiểu trên thì trả về rỗng
        }

        String roomId = getRoomId(currentUserName, recipientName);
        List<ChatMessage> messages = chatMessageRepo.findByRoomIdOrderByTimestampAsc(roomId);

        return messages.stream().map(msg -> new ChatMessageDto(
                msg.getSender().getName(),
                msg.getRecipient().getName(),
                msg.getContent())).collect(Collectors.toList());
    }

    private String getRoomId(String user1, String user2) {
        return (user1.compareTo(user2) < 0) ? user1 + "_" + user2 : user2 + "_" + user1;
    }

    @GetMapping("/rooms")
    @ResponseBody
    public List<String> getChatRooms() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final String currentUserName;
        if (principal instanceof CustomUserDetails user) {
            User consumer = userService.findByUsername(user.getUsername());
            if (consumer == null)
                return Collections.emptyList();
            currentUserName = consumer.getName();
        } else if (principal instanceof OAuth2User oauth2User) {
            User consumer = userService.findByGmail(oauth2User.getAttribute("email"));
            if (consumer == null)
                return Collections.emptyList();
            currentUserName = consumer.getName();
        } else {
            return Collections.emptyList();
        }

        List<String> roomIds = chatMessageRepo.findRoomIdsByUser(currentUserName);

        return roomIds.stream()
                .map(roomId -> {
                    String[] users = roomId.split("_");
                    return users[0].equals(currentUserName) ? users[1] : users[0];
                })
                .collect(Collectors.toList());
    }

}
