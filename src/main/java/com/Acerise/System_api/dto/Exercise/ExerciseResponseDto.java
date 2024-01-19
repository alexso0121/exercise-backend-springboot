package com.Acerise.System_api.dto.Exercise;

import com.Acerise.System_api.Enum.Grade;
import com.Acerise.System_api.Enum.Subject;
import com.Acerise.System_api.Model.Tags;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseResponseDto{

        private  String id;

        private String grade;

        private Subject subject;

        private List<Tags>tags;

        private String location;

        private Double avg_rating;

        private String language;

        private LocalDateTime upload_time;

       private byte[] thumbnail;

}
