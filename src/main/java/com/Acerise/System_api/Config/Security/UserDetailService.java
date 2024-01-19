package com.Acerise.System_api.Config.Security;

import com.Acerise.System_api.Exception.CommonException.CustomCommonBadRequestException;
import com.Acerise.System_api.Model.User;
import com.Acerise.System_api.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user= userRepository
                .findUserByEmailAndProvider(email,"self")
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + email));
        System.out.println("user: " + user.toString());

        if(!user.isEnable()) throw new CustomCommonBadRequestException("User not activated: " + email);

        return user;
    }
}
