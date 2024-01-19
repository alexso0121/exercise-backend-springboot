package com.Acerise.System_api.Service;


import com.Acerise.System_api.Exception.CommonException.CustomCommonNotFoundException;
import com.Acerise.System_api.Model.Tags;
import com.Acerise.System_api.Repository.TagsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class TagsService extends ApiHandler<Tags> {

    private final TagsRepository tagsRepository;
    private final ModelMapper modelMapper;

    public TagsService(TagsRepository tagsRepository, ModelMapper modelMapper) {
        this.tagsRepository = tagsRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public MongoRepository<Tags, String> getRepository() {
        return tagsRepository;
    }

    public ResponseEntity<List<String>> FilterTags(String subjects, String grades) {

        List<String> subjectList = Objects.equals(subjects, "") ? List.of() : Arrays.stream(subjects.split(",")).toList();
        List<String> gradeList = Objects.equals(grades, "") ? List.of() : Arrays.stream(grades.split(",")).toList();
        List<Tags> tagsList = tagsRepository.findAll();
        List<Tags> result;

        if (subjectList.isEmpty() && gradeList.isEmpty()) {
            result = tagsList;
        } else if (subjectList.isEmpty()) {
            result = tagsList.stream().filter(tags -> tags.getGrades().stream().anyMatch(grade -> gradeList.contains(grade.toString()))).toList();
        } else if (gradeList.isEmpty()) {
            result = tagsList.stream().filter(tags -> subjectList.contains(tags.getSubject().toString())).toList();

        } else {
            result = tagsList.stream().filter(tags -> subjectList.contains(tags.getSubject().toString()) && tags.getGrades().stream().anyMatch(grade -> gradeList.contains(grade.toString()))).toList();
        }


        List<Tags> filterTags = result.stream().sorted((o1, o2) -> o2.getCount() - o1.getCount()).toList();

        return ResponseEntity.ok(filterTags.stream().map(Tags::getTagName).toList());

    }


    public ResponseEntity<List<Tags>> SearchTagsWithCategory(String tagStr, String subject) {

        if (Objects.equals(subject, "")) {
            return ResponseEntity.ok(tagsRepository.findByTagNameContaining(tagStr));
        }

        return ResponseEntity.ok(tagsRepository.findByTagNameContainingAndSubject(tagStr, subject));
    }

    public Boolean isTagExist(String tagName) {
        return tagsRepository.findByTagName(tagName).orElse(null) != null;
    }

    public ResponseEntity<String> createTags(List<Tags> tags) {
        tags.forEach(tag -> {
            if (isTagExist(tag.getTagName())) {
                throw new CustomCommonNotFoundException("Tag Already Exist");
            }
            ImplementSave(tag);
        });

        return ResponseEntity.ok("Tags Created with tag name " + tags.stream().map(Tags::getTagName).toList());
    }

    public ResponseEntity<List<Tags>> AllTags() {
        Sort sort = Sort.by(Sort.Direction.DESC, "count");
        return ResponseEntity.ok(tagsRepository.findAll(sort));
    }


    public ResponseEntity<String> updateTags(Tags tag) {
        Tags tags = tagsRepository.findByTagName(tag.getTagName()).orElseThrow(() -> new CustomCommonNotFoundException("Tag Not Found"));
        Tags newTags = Tags.builder().id(tags.getId()).subject(tag.getSubject()).grades(tag.getGrades()).tagName(tags.getTagName()).count(tag.getCount()).build();

        ImplementSave(newTags);
        return ResponseEntity.ok("Tags with name " + tags.getTagName() + " Updated");
    }


}
