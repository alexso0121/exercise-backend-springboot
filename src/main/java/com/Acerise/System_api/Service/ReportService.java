package com.Acerise.System_api.Service;

import com.Acerise.System_api.Model.Report;
import com.Acerise.System_api.Repository.ReportRepository;
import com.Acerise.System_api.dto.CustomResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService extends ApiHandler<Report> {
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }


    @Override
    public MongoRepository<Report, String> getRepository() {
        return reportRepository;
    }

    public CustomResponse<String> createReport(Report report){
        ImplementSave(report);
        return CustomResponse.<String>builder()
                .body("Report created successfully")
                .message(null)
                .status(HttpStatus.OK)
                .dateTime(LocalDateTime.now())
                .build();
    }

    public CustomResponse<String> updateReport(Report report){
        Report existingReport = findById(report.getId());
        existingReport.setReport(report.getReport());
        ImplementSave(existingReport);
        return CustomResponse.<String>builder()
                .body("Report updated successfully")
                .message(null)
                .status(HttpStatus.OK)
                .dateTime(LocalDateTime.now())
                .build();
    }

    public CustomResponse<String> deleteReport(String id){
        ImplementDelete(id);
        return CustomResponse.<String>builder()
                .body("Report deleted successfully")
                .message(null)
                .status(HttpStatus.OK)
                .dateTime(LocalDateTime.now())
                .build();
    }

    public CustomResponse<List<Report>> getByUserId(String userId){
        return CustomResponse.<List<Report>>builder()
                .body(reportRepository.findByUserId(userId))
                .message(null)
                .status(HttpStatus.OK)
                .dateTime(LocalDateTime.now())
                .build();
    }

    public CustomResponse<List<Report>> getByExerciseId(String exerciseId){
        return CustomResponse.<List<Report>>builder()
                .body(reportRepository.findByExerciseId(exerciseId))
                .message(null)
                .status(HttpStatus.OK)
                .dateTime(LocalDateTime.now())
                .build();
    }


}
