package com.Acerise.System_api.Model;

import com.Acerise.System_api.Enum.Grade;
import com.Acerise.System_api.Enum.Subject;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.lang.NonNullFields;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "Exercise")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {

    @Id
    private String id;

    @Field
    @NotNull(message = "User's first name must not be null")
    private String title;

    @Field
    @NotNull(message = "grade must not be null")
    private List<Grade> grade;

    @Field
    @NotNull(message = "subject must not be null")
    private Subject subject;

    @Field
    @NotNull(message = "tags must not be null")
    private List<String> tags;

    @Field
    private String location;

    @Field
    private double avg_rating;

    @Field
    private String language;

    @Field
    private LocalDateTime upload_time;

    @Field
    @NotNull(message = "question file key must not be null")
    private String question_file_Key;

    @Field
    @NotNull(message = "solution file key must not be null")
    private String solution_file_Key;

    @Field
    private String audio_file_Key;

    @Field
    private byte[] thumbnail;

    @Field
    @NotNull(message = "isPreview must not be null")
    private String isPreview;


}
