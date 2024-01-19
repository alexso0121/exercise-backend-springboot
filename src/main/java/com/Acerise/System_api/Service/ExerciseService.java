package com.Acerise.System_api.Service;

import com.Acerise.System_api.Exception.CommonException.CustomCommonBadRequestException;
import com.Acerise.System_api.Exception.CommonException.CustomCommonInternalServerException;
import com.Acerise.System_api.Exception.CommonException.CustomCommonNotFoundException;
import com.Acerise.System_api.Model.Exercise;
import com.Acerise.System_api.Model.User;
import com.Acerise.System_api.Repository.ExerciseRepository;
import com.Acerise.System_api.dto.CustomResponse;
import com.Acerise.System_api.dto.Exercise.AccessFileDto;
import com.Acerise.System_api.dto.User.Preference;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ExerciseService  extends ApiHandler<Exercise> {
    private final ExerciseRepository exerciseRepository;

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final AmazonS3 s3Client;

    private final TagsService tagsService;



    public ExerciseService(ExerciseRepository exerciseRepository, UserService userService, ModelMapper modelMapper, AmazonS3 s3client, TagsService tagsService) {
        this.exerciseRepository = exerciseRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.s3Client = s3client;
        this.tagsService = tagsService;
    }

    @Value("${aws.s3.bucket}")
    private String bucketName;


    @Override
    public MongoRepository<Exercise,String> getRepository() {
        return exerciseRepository;
    }


    public ResponseEntity<List<Exercise>> getAllSortByTime() {
        return ResponseEntity.ok(findAllBySort(Sort.by(Sort.Direction.DESC,"upload_time")));
    }

    @Transactional
    public Boolean updateRating(String exerciseId, Double newRating,int Ratingcount){
        Exercise exercise = findById(exerciseId);
        if(Objects.nonNull(exercise)){
            double avgRating = (exercise.getAvg_rating()*Ratingcount+newRating)/(Ratingcount+1);
            exercise.setAvg_rating(avgRating);
            ImplementSave(exercise);
            return true;
        }
        return false;
    }

    public List<Exercise> _getRecommendExercise(Preference preference){
        List<Exercise> allExercises = findAllBySort(Sort.by(Sort.Direction.DESC,"avg_rating"));
        List<Exercise> filteredExercise= allExercises.stream().filter(exercise ->
               preference.getPrefer_subj().contains(exercise.getSubject())
                        || preference.getPrefer_grade().stream().anyMatch(grade -> exercise.getGrade().contains(grade))
                        || preference.getPrefer_tag().stream().anyMatch(tag -> exercise.getTags().contains(tag))).toList();

        if(filteredExercise.size()<5) return allExercises.subList(0,10);

        return filteredExercise.size()>10?filteredExercise.subList(0,10):filteredExercise;
    }

//    public ResponseEntity<List<Exercise>> searchExercise(String searchStr,String subjects,String grades,String tags){
//        List<Exercise> exercises=findAllBySort(Sort.by(Sort.Direction.DESC,"avg_rating"));
//        List<Exercise> filteredExercise=exercises.stream().filter(
//                exercise -> exercise.getTitle().toLowerCase().contains(searchStr.toLowerCase())
//                        && (subjects.contains(exercise.getSubject().toString())
//                        || (grades.contains(exercise.getGrade().toString()))
//                        || (tags.contains(exercise.getTags().toString())))).toList();
//         return ResponseEntity.ok(filteredExercise);
//        )
//
//    }

    public ResponseEntity<List<Exercise>> recommendExercise (String id){
        Preference preference = userService._getPreference(id);
        List<Exercise> recommendationExercise=_getRecommendExercise(preference);
        return ResponseEntity.ok(recommendationExercise);

    }




    private byte[] getByte(String fileKey){
        try {
            // Check if the file exists
            boolean fileExists = s3Client.doesObjectExist(bucketName, fileKey);

            if (fileExists) {
                // Retrieve the file content
                S3Object s3Object = s3Client.getObject(bucketName, fileKey);
                S3ObjectInputStream inputStream = s3Object.getObjectContent();
                byte[] fileContent = inputStream.readAllBytes();
                inputStream.close();
                return fileContent;
            } else {
                // File does not exist
                throw new CustomCommonNotFoundException("File not found with key: " + fileKey);
            }
        } catch (Exception e) {
           throw new CustomCommonInternalServerException("Error occurred while parsing file with key +"+fileKey+": " + e.getMessage());

        }
    }

    public CustomResponse<List<Exercise>> getAllSortByRating(){
        return CustomResponse.<List<Exercise>>builder()
                .body(findAllBySort(Sort.by(Sort.Direction.DESC,"avg_rating")))
                .status(HttpStatus.OK)
                .message(null)
                .dateTime(LocalDateTime.now())
                .build();
    }

    public CustomResponse<List<Exercise>> GetAllByFilter(String subjects, String grades, String tags){
        List<Exercise> allExercises = findAllBySort(Sort.by(Sort.Direction.DESC,"upload_time"));
        List<String> tagsList = List.of(tags.split(","));
        List<Exercise> filterExcerise=allExercises.stream().filter(exercise ->
                Objects.nonNull(exercise.getSubject()) && subjects.contains(exercise.getSubject().toString())
                || (Objects.nonNull(exercise.getGrade()) && grades.contains(exercise.getGrade().toString()))
                || (Objects.nonNull(exercise.getTags()) &&
                tagsList.stream().anyMatch(tags1 -> Objects.nonNull(tags1) &&
                        exercise.getTags().contains(tags1)))).toList();

        return CustomResponse.<List<Exercise>>builder()
                .body(filterExcerise)
                .status(HttpStatus.OK)
                .dateTime(LocalDateTime.now())
                .build();
    }
    @Transactional(rollbackFor = Exception.class)
    public CustomResponse<String> uploadFiles(MultipartFile[] files) {
        try {
            for (MultipartFile file : files) {
                if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".pdf"))
                    throw new CustomCommonInternalServerException("File is empty");

                String fileKey = file.getOriginalFilename();
                s3Client.putObject(bucketName, fileKey, file.getInputStream(), null);
            }
            return CustomResponse.<String>builder()
                    .body(files.length+" Files uploaded successfully")
                    .status(HttpStatus.OK)
                    .message(null)
                    .dateTime(LocalDateTime.now())
                    .build();
        } catch (Exception e) {
            throw new CustomCommonInternalServerException(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public CustomResponse<String> uploadExercise(List<Exercise> exercises){
        try {
            System.out.println(exercises);

            exercises.forEach(exercise -> {
                if(checkFileExist(exercise.getQuestion_file_Key()))
                    throw new CustomCommonNotFoundException("File not found with key: " + exercise.getQuestion_file_Key());
                if(checkFileExist(exercise.getSolution_file_Key()))
                    throw new CustomCommonNotFoundException("File not found with key: " + exercise.getSolution_file_Key());

                if(exercise.getAudio_file_Key()!=null){
                    if(checkFileExist(exercise.getAudio_file_Key()))
                        throw new CustomCommonNotFoundException("File not found with key: " + exercise.getAudio_file_Key());
                }

                for(String tag:exercise.getTags()){
                    if(!tagsService.isTagExist(tag)){
                        throw new CustomCommonBadRequestException("Tag "+tag+" not exist");
                    }
                }

                try {
                    byte[] thumbnail= generateThumbnailFromPDF(exercise.getQuestion_file_Key());
                    exercise.setThumbnail(thumbnail);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                ImplementSave(exercise);
            });


            return CustomResponse.<String>builder()
                    .body("Exercise uploaded successfully with "+exercises.size()+" exercises")
                    .status(HttpStatus.OK)
                    .message(null)
                    .dateTime(LocalDateTime.now())
                    .build();
        }catch (Exception e){
               throw new CustomCommonInternalServerException(e.getMessage());
        }
    }
    private byte[] generateThumbnailFromPDF(String exercise_pdf_key) throws IOException, SQLException {
        byte[] pdfBytes = getByte(exercise_pdf_key);
        try (PDDocument document = PDDocument.load(pdfBytes)) {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImage(0, 0.3f); // Render the first page with a scaling factor

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", baos);
            byte[] imageBytes = baos.toByteArray();
            log.info(Arrays.toString(imageBytes));

            return imageBytes;
        }
    }



    public ResponseEntity<List<Exercise>> getPreviewExercise(){
        List<Exercise> allExercises = exerciseRepository.getPreviewExercise();
        return ResponseEntity.ok(allExercises);
    }

    private boolean checkFileExist(String fileKey){
        if (fileKey==null){
            return true;
        }
        return !s3Client.doesObjectExist(bucketName, fileKey);
    }

    public ResponseEntity<Resource> solution(String exerciseId) {
        Exercise exercise = findById(exerciseId);
        S3Object s3Object = s3Client.getObject(bucketName, exercise.getSolution_file_Key());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + exercise.getSolution_file_Key());

        // Set the media type of the response
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        InputStreamResource resource = new InputStreamResource(s3Object.getObjectContent());

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }



    public ResponseEntity<AccessFileDto> access(String exerciseId) {
        Exercise exercise = findById(exerciseId);
        S3Object s3Object = s3Client.getObject(bucketName, exercise.getQuestion_file_Key());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + exercise.getQuestion_file_Key());


        InputStreamResource exerciseResource = new InputStreamResource(s3Object.getObjectContent());
        if (exercise.getAudio_file_Key()==null){

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(AccessFileDto.builder()
                        .exerciseFile(exerciseResource)
                        .audioFile(null)
                        .build());
        }
        S3Object s3Object2= s3Client.getObject(bucketName, exercise.getAudio_file_Key());
        InputStreamResource audioResource = new InputStreamResource(s3Object2.getObjectContent());

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + exercise.getAudio_file_Key());

        // Set the media type of the response
        headers.setContentType(MediaType.TEXT_PLAIN);
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(AccessFileDto.builder()
                        .exerciseFile(exerciseResource)
                        .audioFile(audioResource)
                        .build());


    }



}
