package com.Acerise.System_api.Repository;

import com.Acerise.System_api.Model.Exercise;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ExerciseRepository extends MongoRepository<Exercise, String> {
    @Query(value = "{ 'isPreview' : 'true' }")
    List<Exercise> getPreviewExercise();
}
