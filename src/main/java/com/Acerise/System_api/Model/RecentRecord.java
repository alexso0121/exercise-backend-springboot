package com.Acerise.System_api.Model;

import com.Acerise.System_api.Enum.RecentDocumentEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Recent_Document")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentRecord{

    @Id
    private String id;

    @Field
    private String userId;

    @Field
    private String exercise_id;

    @Field
    private RecentDocumentEnum type;



}
