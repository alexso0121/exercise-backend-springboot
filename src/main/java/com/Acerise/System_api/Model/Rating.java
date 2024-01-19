package com.Acerise.System_api.Model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.annotation.Collation;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "Rating")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rating {

    @Id
    private String id;

    @Field
    private String user_id;

    @Field
    private String exerciseId;

    @Field
    private double rating;
}
