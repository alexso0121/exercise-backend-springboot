package com.Acerise.System_api.Controller;

import com.Acerise.System_api.Model.Report;
import com.Acerise.System_api.Service.ReportService;
import com.Acerise.System_api.dto.CustomResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/getByExercise")
    public CustomResponse<List<Report>> getReportByExerciseId(@RequestParam String exerciseId){
        return reportService.getByExerciseId(exerciseId);
    }

    @GetMapping("/getByUser")
    public CustomResponse<List<Report>> getReportByUserId(@RequestParam String userId){
        return reportService.getByUserId(userId);
    }

    @PostMapping("/create")
    public CustomResponse<String> createReport(@RequestBody Report report){
        return reportService.createReport(report);
    }

    @PutMapping("/update")
    public CustomResponse<String> updateReport(@RequestBody Report report){
        return reportService.updateReport(report);
    }

    @DeleteMapping("/delete")
    public CustomResponse<String> deleteReport(@RequestParam String id){
        return reportService.deleteReport(id);
    }


}
