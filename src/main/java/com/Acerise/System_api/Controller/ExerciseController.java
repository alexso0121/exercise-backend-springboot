package com.Acerise.System_api.Controller;

import com.Acerise.System_api.Enum.Subject;
import com.Acerise.System_api.Model.Exercise;
import com.Acerise.System_api.dto.CustomResponse;
import com.Acerise.System_api.dto.Exercise.AccessFileDto;
import com.Acerise.System_api.dto.Exercise.ExerciseResponseDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Acerise.System_api.Service.ExerciseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService){
        this.exerciseService=exerciseService;
    }




    @GetMapping("/all/time")
    public ResponseEntity<List<Exercise>> getAllByTime(){
        return exerciseService.getAllSortByTime();
    }

    @GetMapping("/all/rating")
    public CustomResponse<List<Exercise>> getAllByRating(){
        return exerciseService.getAllSortByRating();
    }

    @GetMapping("/all/filter")
    public CustomResponse<List<Exercise>> getAllByFilter(@RequestParam String subjects, @RequestParam String grades, @RequestParam String tags){

        return exerciseService.GetAllByFilter(subjects,grades,tags);
    }

    @GetMapping("/access")
    public ResponseEntity<AccessFileDto> getExercise(@RequestParam  String exerciseId){
        return exerciseService.access(exerciseId);
    }

    @GetMapping("/solution")
    public ResponseEntity<Resource> getSolution(@RequestParam String exerciseId){
        return exerciseService.solution(exerciseId);
    }

    //1.put the resource files in a directory named as 'uploadFiles' directory first
    //2.scp it to the server under the  resources directory under springboot project
    //3.login as admin and call the api in postman
    @PostMapping("/uploadFiles")
    public CustomResponse<String> uploadFiles(@RequestParam("file") MultipartFile[] files ){
        return exerciseService.uploadFiles(files);
    }
    @PostMapping("/uploadExercise")
    public CustomResponse<String> uploadExercise(@RequestBody List<Exercise> exercises){
        return exerciseService.uploadExercise(exercises);
    }

    @GetMapping("/preview")
    public ResponseEntity<List<Exercise>> getPreviewExercise(){
        return exerciseService.getPreviewExercise();
    }




}
