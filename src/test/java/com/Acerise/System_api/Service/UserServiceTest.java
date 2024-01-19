package com.Acerise.System_api.Service;

import com.Acerise.System_api.Enum.Grade;
import com.Acerise.System_api.Enum.Subject;
import com.Acerise.System_api.Model.User;
import com.Acerise.System_api.Repository.UserRepository;
import com.Acerise.System_api.dto.User.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    ModelMapper modelMapper;


//    @Test
//    void updateUser() {
//        UserResponse userRequest=UserResponse.builder()
//                .id("123")
//                        .username("test")
//                                .email("sws")
//                                        .icon_file_name("sws")
//                                                .prefer_subj(List.of(Subject.COMPUTER_SCIENCE,Subject.ENGLISH))
//                                                        .prefer_grade(List.of(Grade.LOW_PRIMARY))
//                                                                .build();
//        User existUser=User.builder().id("123").email("cdcw").build();
//
//        when(userRepository.findById("123")).thenReturn(Optional.ofNullable(existUser));
//        User result=userService.updateTemp(userRequest);
//
//        assertEquals("test",result.getUsername());
//        assertEquals("sws",result.getEmail());
//
//    }

}