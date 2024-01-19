package com.Acerise.System_api.Service;

import com.Acerise.System_api.Enum.RecentDocumentEnum;
import com.Acerise.System_api.Exception.CommonException.CustomCommonBadRequestException;
import com.Acerise.System_api.Exception.CommonException.CustomCommonInternalServerException;
import com.Acerise.System_api.Model.RecentRecord;
import com.Acerise.System_api.Repository.RecentRecordRepository;
import com.Acerise.System_api.dto.CustomResponse;
import com.Acerise.System_api.dto.RecentDocument.RecentDocumentResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecentDocumentService extends ApiHandler<RecentRecord> {
    private final RecentRecordRepository recentRecordRepository;

    private final ExerciseService exerciseService;

    public RecentDocumentService(RecentRecordRepository recentRecordRepository, ExerciseService exerciseService) {
        this.recentRecordRepository = recentRecordRepository;
        this.exerciseService = exerciseService;
    }


    @Override
    public MongoRepository<RecentRecord, String> getRepository() {
        return recentRecordRepository;
    }


    public CustomResponse<List<RecentDocumentResponse>> findByUserIdAndType(String user_id, String type){
        try{
            RecentDocumentEnum recentDocumentEnum=RecentDocumentEnum.valueOf(type);

            return CustomResponse.<List<RecentDocumentResponse>>builder()
                    .body(
                        recentRecordRepository.findByUserIdAndType(user_id, recentDocumentEnum).stream().map(
                                recentRecord ->new RecentDocumentResponse(recentRecord.getId(),
                        exerciseService.findById(recentRecord.getExercise_id()))
                    ).collect(Collectors.toList()))
                    .status(HttpStatus.OK)
                    .dateTime(LocalDateTime.now())
                    .message(null).build();
        }catch(Exception e){
            throw new CustomCommonInternalServerException(e.getMessage());
        }
    }

    public CustomResponse<String> createRecentDocument(RecentRecord recentRecord){
        ImplementSave(recentRecord);
        return CustomResponse.<String>builder()
                .body("Create "+recentRecord.getType().toString()+ " record on user with id: " +recentRecord.getUserId()+"Success")
                .status(HttpStatus.OK)
                .dateTime(LocalDateTime.now())
                .message(null).build();
    }

    public CustomResponse<String> deleteRecentDocument(String id){
        RecentRecord record=findById(id);
        if(record.getType()!=RecentDocumentEnum.STORE){
            throw new CustomCommonBadRequestException("only store record can be deleted");
        }
        ImplementDelete(id);
        return CustomResponse.<String>builder()
                .body("Delete "+id+" Success")
                .status(HttpStatus.OK)
                .dateTime(LocalDateTime.now())
                .message(null).build();
    }



}
