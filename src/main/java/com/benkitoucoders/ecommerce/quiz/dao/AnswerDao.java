package com.benkitoucoders.ecommerce.quiz.dao;

import com.benkitoucoders.ecommerce.quiz.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnswerDao extends JpaRepository<Answer, Long>, JpaSpecificationExecutor<Answer> {

}
