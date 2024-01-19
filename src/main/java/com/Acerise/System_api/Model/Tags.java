package com.Acerise.System_api.Model;

import com.Acerise.System_api.Enum.Grade;
import com.Acerise.System_api.Enum.Subject;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "Tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tags {
    @Id
    private String id;

    @Field
    @NotNull(message = "Subject is required")
    private Subject subject;

    @Field
    @NotNull(message = "Grade is required")
    private List<Grade> grades;

    @Field
    @NotNull(message = "Tag name is required")
    private String tagName;

    @Field
    private int count;
}
