package com.Acerise.System_api.Service;

import com.Acerise.System_api.Exception.CommonException.CustomCommonNotFoundException;
import com.Acerise.System_api.Model.Rating;
import com.Acerise.System_api.Repository.RatingRepository;
import com.Acerise.System_api.dto.CustomResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RatingService extends ApiHandler<Rating> {

    private final RatingRepository ratingRepository;
    private final ExerciseService exerciseService;

    public RatingService(RatingRepository ratingRepository, ExerciseService exerciseService) {
        this.ratingRepository = ratingRepository;
        this.exerciseService = exerciseService;
    }


    @Override
    public MongoRepository<Rating, String> getRepository() {
        return ratingRepository;
    }


    public CustomResponse<String> create(Rating rating){
        int count = ratingRepository.findByExerciseId(rating.getExerciseId()).size();
        Boolean isSuccess=exerciseService.updateRating(rating.getExerciseId(),rating.getRating(),count);
        if(!isSuccess){
           throw new CustomCommonNotFoundException("Exercise not found");
        }
        return CustomResponse.<String>builder()
                .body("rating created successfully")
                .message(null)
                .status(HttpStatus.OK)
                .dateTime(LocalDateTime.now())
                .build();
    }


}
