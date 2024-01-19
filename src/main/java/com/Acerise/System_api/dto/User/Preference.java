package com.Acerise.System_api.dto.User;

import com.Acerise.System_api.Enum.Grade;
import com.Acerise.System_api.Enum.Subject;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Preference {


    @NotNull(message = "Subject is required")
    private List<Subject> prefer_subj;

    @NotNull(message = "Grade is required")
    private List<Grade> prefer_grade;

    @NotNull(message = "Tag name is required")
    private List<String> prefer_tag;
}
