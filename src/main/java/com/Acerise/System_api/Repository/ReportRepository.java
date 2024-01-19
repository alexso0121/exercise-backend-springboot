package com.Acerise.System_api.Repository;

import com.Acerise.System_api.Model.Report;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends MongoRepository<Report, String> {
    List<Report> findByUserId(String userId);

    List<Report> findByExerciseId(String exerciseId);
}
