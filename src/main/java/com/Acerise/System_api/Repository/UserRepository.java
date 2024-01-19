package com.Acerise.System_api.Repository;


import com.Acerise.System_api.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByEmailAndProvider(String username, String provider);
}


