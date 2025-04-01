package com.code.shopee.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacebookUser {
    private String id;
    private String name;
    private String email;
    private Map<String, Object> picture;

    public Map<String, Object> getAttributes() {
        return Map.of("id", id, "name", name, "email", email, "picture", picture);
    }
}
