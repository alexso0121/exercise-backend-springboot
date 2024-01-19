package com.Acerise.System_api.Repository;

import com.Acerise.System_api.Model.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends MongoRepository<Rating, String>{
    List<Rating> findByExerciseId(String exerciseId);
}
