package com.Acerise.System_api.dto.Auth;

import com.Acerise.System_api.Enum.*;
import com.Acerise.System_api.Model.Tags;
import com.Acerise.System_api.Model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest{
        private String id;
        private String username;
        private String encrypted_pwd;

        private String region;

        private IdentityEnum identity;

        LanguageEnum default_language;






}
