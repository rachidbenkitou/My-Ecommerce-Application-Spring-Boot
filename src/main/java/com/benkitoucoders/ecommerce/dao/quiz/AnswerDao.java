package com.benkitoucoders.ecommerce.dao.quiz;

import com.benkitoucoders.ecommerce.entities.quiz.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnswerDao extends JpaRepository<Answer, Long>, JpaSpecificationExecutor<Answer> {

}
