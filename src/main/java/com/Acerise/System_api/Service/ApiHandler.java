package com.Acerise.System_api.Service;

import com.Acerise.System_api.Exception.CommonException.CustomCommonBadRequestException;
import com.Acerise.System_api.Exception.CommonException.CustomCommonInternalServerException;
import com.Acerise.System_api.Exception.CommonException.CustomCommonNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

@Slf4j
public abstract  class ApiHandler<Type> {
    public abstract MongoRepository<Type,String> getRepository();


    public Type ImplementSave( Type object){
        try{;
            //logging data and throw error for messaging query
            Type savedObject =  getRepository().save(object);

            log.info("new data saved"+savedObject.toString());

            return savedObject;

        }catch(Exception e){
            throw new CustomCommonBadRequestException(e.getMessage());
        }
    }

    public List<Type> findAllBySort(Sort sort){
        try{
            return getRepository().findAll(sort);
        }catch(Exception e){
            throw new CustomCommonBadRequestException(e.getMessage());
        }
    }

    public Type findById(String id){
        try{
            Type existData= getRepository().findById(id).orElse(null);
            if (existData==null){
                throw new CustomCommonNotFoundException("Data not found with id: "+id);
            }
            return existData;
        }catch(Exception e){
            throw new CustomCommonInternalServerException(e.getMessage());
        }
    }


    public void ImplementDelete(String id){
        try{
            getRepository().deleteById(id);
        }catch(Exception e){
            throw new CustomCommonInternalServerException(e.getMessage());
        }
    }


}




