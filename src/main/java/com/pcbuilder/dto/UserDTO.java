package com.pcbuilder.dto;


import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor

public class UserDTO {
    private Long id;
    private String username;
    private String email;

    // Constructor without password for security
    public UserDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
