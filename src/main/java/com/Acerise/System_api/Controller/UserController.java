package com.Acerise.System_api.Controller;

import com.Acerise.System_api.Service.UserService;
import com.Acerise.System_api.dto.Auth.AuthResponse;
import com.Acerise.System_api.dto.CustomResponse;
import com.Acerise.System_api.dto.User.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Slf4j
@CrossOrigin
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
            this.userService = userService;
    }

    @GetMapping("/get")
    public CustomResponse<UserResponse> getUserById(@RequestParam String id){
        return userService.getUserById(id);

    }

    @PostMapping("/update")
    public CustomResponse<UserResponse> updateUser(@RequestBody UserUpdateRequest userUpdateRequest){
        return userService.updateUser(userUpdateRequest);
    }

    @PostMapping("/plan")
    public CustomResponse<String> changePlan(@RequestBody ChangeSubRequest changeSubRequest){
        return userService.changePlan(changeSubRequest);
    }

    @GetMapping("/preference")
    public ResponseEntity<Preference> getPreferences(@RequestParam String id){
        return userService.getPreference(id);
    }

    @PostMapping("/preference")
    public ResponseEntity<Preference> updatePreferences(@RequestBody Preference preference,Authentication authentication){
        return userService.updatePreference(preference,authentication);
    }


    @GetMapping("/authData")
    public CustomResponse<AuthResponse> getAuthData(Authentication authentication, Principal principal){
        log.info("principal: " + authentication.getCredentials());
        return userService.getAuthData(authentication);
    }


}
