package com.Acerise.System_api.Repository;

import com.Acerise.System_api.Model.Tags;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagsRepository extends MongoRepository<Tags,String> {
    Optional<Tags> findByTagName(String tagName);

    List<Tags> findByTagNameContainingAndSubject(String tagName, String subject);

    List<Tags> findByTagNameContaining(String tagName);

}
