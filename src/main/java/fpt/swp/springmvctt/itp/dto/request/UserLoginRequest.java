package fpt.swp.springmvctt.itp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginRequest {
    
    @NotBlank(message = "Username or email is required")
    private String usernameOrEmail;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    private boolean rememberMe;
}
