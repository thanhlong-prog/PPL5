package com.code.shopee.request;

import java.util.Map;

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
public class FacebookUserRequest {
    private String id;
    private String name;
    private String email;
    private Map<String, Object> picture;

    public Map<String, Object> getAttributes() {
        return Map.of("id", id, "name", name, "email", email, "picture", picture);
    }
}
