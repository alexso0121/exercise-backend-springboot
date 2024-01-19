package com.Acerise.System_api.Controller;

import com.Acerise.System_api.Model.Tags;
import com.Acerise.System_api.Service.TagsService;
import com.Acerise.System_api.dto.CustomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/tags")
public class TagsController {
    private final TagsService tagsService;

    public TagsController(TagsService tagsService) {
        this.tagsService = tagsService;
    }

    @GetMapping("/get")
    public ResponseEntity<List<Tags>> getTags(){
        return tagsService.AllTags();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTags(@RequestBody List<Tags> tags){
        return tagsService.createTags(tags);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<String>> filterTags(@RequestParam String subjects, @RequestParam String grades){
        return tagsService.FilterTags(subjects,grades);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateTags(@RequestBody Tags tags){
        return tagsService.updateTags(tags);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Tags>> searchTags(@RequestParam String tagStr, @RequestParam String subject){
        return tagsService.SearchTagsWithCategory(tagStr,subject);
    }
}
