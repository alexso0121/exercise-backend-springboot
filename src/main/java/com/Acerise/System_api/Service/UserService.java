package com.Acerise.System_api.Service;

import com.Acerise.System_api.Exception.CommonException.CustomCommonBadRequestException;
import com.Acerise.System_api.Exception.CommonException.CustomCommonInternalServerException;
import com.Acerise.System_api.Model.Exercise;
import com.Acerise.System_api.Model.User;
import com.Acerise.System_api.dto.Auth.AuthResponse;
import com.Acerise.System_api.dto.CustomResponse;
import com.Acerise.System_api.dto.User.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import com.Acerise.System_api.Repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequestMapping("/api/user")
public class UserService extends ApiHandler<User> {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private final TagsService tagsService;




    public UserService(UserRepository userRepository, ModelMapper modelMapper, TagsService tagsService) {
        this.userRepository = userRepository;
        this.modelMapper =  modelMapper;
        this.tagsService = tagsService;
    }

    @Override
    public MongoRepository<User,String> getRepository() {
        return userRepository;

    }

    public CustomResponse<UserResponse> getUserById(String id){
        User user = findById(id);
        return CustomResponse.<UserResponse>builder()
                .body(modelMapper.map(user,UserResponse.class))
                .build();
    }

    @Transactional
    public CustomResponse<UserResponse> updateUser(UserUpdateRequest userUpdateRequest){
        User existUser = findById(userUpdateRequest.id());
        modelMapper.map(userUpdateRequest, existUser);
        User updatedUser = ImplementSave(existUser);
        UserResponse updatedUserResponse = modelMapper.map(updatedUser, UserResponse.class);
        return CustomResponse.<UserResponse>builder()
                .body(updatedUserResponse)
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.OK)
                .build();
    }




    public CustomResponse<AuthResponse> getAuthData(Authentication authentication){

        User user;
        Jwt jwt =(Jwt) authentication.getCredentials();
        String id=jwt.getClaim("id");

        if(id!=null){
            user=findById(id);
        } else{
            throw new CustomCommonInternalServerException("User not found");

        }


        return CustomResponse.<AuthResponse>builder()
                .body(modelMapper.map(user,AuthResponse.class))
                .status(HttpStatus.OK)
                .message(null)
                .build();

    }


    @Transactional
    public CustomResponse<String> changePlan(ChangeSubRequest changeSubRequest) {
        User existUser = findById(changeSubRequest.id());
        existUser.setSubscription_plan(changeSubRequest.subscription_plan());
        ImplementSave(existUser);
        return CustomResponse.<String>builder()
                .body("The User with id: "+changeSubRequest.id()+" has changed the subscription plan to "+changeSubRequest.subscription_plan().toString() )
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.OK)
                .build();
    }

    public Preference _getPreference(String id){
        User user = findById(id);
        if(!user.havePreference()) return null;
       return Preference.builder()
                .prefer_subj(user.getPrefer_subj())
                .prefer_grade(user.getPrefer_grade())
                .prefer_tag(user.getPrefer_tag())
                .build();

    }

    public ResponseEntity<Preference> getPreference(String id){
        Preference preference = _getPreference(id);
        return ResponseEntity.ok(preference);
    }




    public ResponseEntity<Preference> updatePreference(Preference preference,Authentication authentication) {
        Jwt jwt =(Jwt) authentication.getCredentials();
        String id=jwt.getClaim("id");
        
        User user = findById(id);
        user.setPrefer_subj(preference.getPrefer_subj());
        user.setPrefer_grade(preference.getPrefer_grade());
        for (String tag:preference.getPrefer_tag()) {
            if(!tagsService.isTagExist(tag)){
                throw new CustomCommonBadRequestException("Tag "+tag+" not found");
            }
        }
        user.setPrefer_tag(preference.getPrefer_tag());
        ImplementSave(user);
        return ResponseEntity.ok(preference);

    }
}
