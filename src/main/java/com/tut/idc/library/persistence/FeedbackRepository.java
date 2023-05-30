package com.tut.idc.library.persistence;

import com.tut.idc.library.persistence.entity.FeedbackEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends CrudRepository<FeedbackEntity, String> {
    FeedbackEntity findByIDNumber(String IDNumber);

}
