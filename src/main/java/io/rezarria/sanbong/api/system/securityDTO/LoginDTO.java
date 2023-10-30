package io.rezarria.sanbong.api.system.securityDTO;

import lombok.Data;

@Data
public class LoginDTO {
    private String username;
    private String password;
}