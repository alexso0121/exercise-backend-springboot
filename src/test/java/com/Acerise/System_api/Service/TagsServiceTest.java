package com.Acerise.System_api.Service;

import com.Acerise.System_api.Enum.Grade;
import com.Acerise.System_api.Enum.Subject;
import com.Acerise.System_api.Model.Tags;
import com.Acerise.System_api.Repository.TagsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagsServiceTest {
    @Mock
    TagsRepository tagsRepository;

    @InjectMocks
    TagsService tagsService;


    @Test
    void filterTags() {
        List<Tags> tags = List.of(
                Tags.builder().subject(Subject.reading).grades(List.of(Grade.P1)).tagName("tag1").count(1).build(),
                Tags.builder().subject(Subject.combined).grades(List.of(Grade.P1, Grade.S2)).tagName("tag3").count(2).build(),
                Tags.builder().subject(Subject.reading).grades(List.of(Grade.S2)).tagName("tag2").count(3).build(),
                Tags.builder().subject(Subject.reading).grades(List.of(Grade.P6)).tagName("tag4").count(4).build()
        );

        when(tagsRepository.findAll()).thenReturn(tags);

        ResponseEntity<List<String>> res = tagsService.FilterTags("", "S2");
        assertEquals(List.of("tag2", "tag3"), res.getBody());

        ResponseEntity<List<String>> res2 = tagsService.FilterTags("reading", "");
        assertEquals(List.of("tag4", "tag2", "tag1"), res2.getBody());

        ResponseEntity<List<String>> res3 = tagsService.FilterTags("reading", "S2");
        assertEquals(List.of("tag2"), res3.getBody());

        ResponseEntity<List<String>> res4 = tagsService.FilterTags("", "");
        assertEquals(4, res4.getBody().size());


    }
}