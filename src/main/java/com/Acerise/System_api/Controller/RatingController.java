package com.Acerise.System_api.Controller;

import com.Acerise.System_api.Model.Rating;
import com.Acerise.System_api.Service.RatingService;
import com.Acerise.System_api.dto.CustomResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rating")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;

    }

    @PostMapping("/create")
    public CustomResponse<String> createRating(@RequestBody Rating rating){
        return ratingService.create(rating);
    }


}
