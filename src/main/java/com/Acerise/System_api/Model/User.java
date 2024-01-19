package com.Acerise.System_api.Model;


import com.Acerise.System_api.Enum.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Data
@Document(collection = "User")
@AllArgsConstructor
@Builder
public class User implements UserDetails {


    @Id
    private String id;

    @Field
    private String encrypted_pwd;

    @Field
    private String username;

    @Field
    private SubscriptionPlanEnum subscription_plan=SubscriptionPlanEnum.FREE;

    @Field
    private String email;

    @Field
    private String icon_file_name;

    @Field
    private String region;

    @Field
    private LanguageEnum default_language=LanguageEnum.zh_HK;

    @Field
    private List<Subject> prefer_subj=List.of();

    @Field
    private List<Grade> prefer_grade=List.of();

    @Field
    private List<String> prefer_tag=List.of();

    @Field
    private RoleEnum role=RoleEnum.USER;

    @Field
    private IdentityEnum identity;

    @Field
    private String otp;

    @Field
    private boolean isEnable=false;

    @Field
    private LocalDateTime otpGeneratedTime;

    @Field
    private Authprovider provider;




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public Boolean havePreference(){
        return !(prefer_grade.isEmpty() && prefer_subj.isEmpty() && prefer_tag.isEmpty());
    }

    @Override
    public String getPassword() {
        return this.encrypted_pwd;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
