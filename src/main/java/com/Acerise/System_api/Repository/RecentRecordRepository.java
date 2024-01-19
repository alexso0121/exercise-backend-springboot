package com.Acerise.System_api.Repository;

import com.Acerise.System_api.Enum.RecentDocumentEnum;
import com.Acerise.System_api.Model.RecentRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecentRecordRepository extends MongoRepository<RecentRecord, String> {

    List<RecentRecord> findByUserIdAndType(String user_id, RecentDocumentEnum type);
}
