package com.Acerise.System_api.Controller;

import com.Acerise.System_api.Model.RecentRecord;
import com.Acerise.System_api.Service.RecentDocumentService;
import com.Acerise.System_api.dto.CustomResponse;
import com.Acerise.System_api.dto.RecentDocument.RecentDocumentResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document")
public class RecentRecordController {

    private final RecentDocumentService recentRecordService;

    public RecentRecordController(RecentDocumentService recentRecordService) {
        this.recentRecordService = recentRecordService;
    }

    @PostMapping("/create")
    public CustomResponse<String> createRecord(@RequestBody RecentRecord recentRecord){
        return recentRecordService.createRecentDocument(recentRecord);
    }

    @DeleteMapping("/delete")
    public CustomResponse<String> deleteRecord(@RequestParam String id){
        return recentRecordService.deleteRecentDocument(id);
    }

    @GetMapping("/get")
    public CustomResponse<List<RecentDocumentResponse>> getRecord(@RequestParam String user_id, @RequestParam String type){
        return recentRecordService.findByUserIdAndType(user_id, type);
    }
}
