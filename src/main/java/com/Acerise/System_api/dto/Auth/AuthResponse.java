package com.Acerise.System_api.dto.Auth;

import com.Acerise.System_api.Enum.*;
import com.Acerise.System_api.Model.Tags;
import com.Acerise.System_api.Model.User;
import lombok.*;

import lombok.Builder;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String id;
    private String username;
    private String email;
    private IdentityEnum identity;
    private String region;
    private LanguageEnum default_language;







    // Getters and Setters


}