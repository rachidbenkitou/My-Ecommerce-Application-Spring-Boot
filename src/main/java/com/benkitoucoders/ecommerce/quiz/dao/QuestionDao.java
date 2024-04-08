package com.benkitoucoders.ecommerce.quiz.dao;

import com.benkitoucoders.ecommerce.quiz.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QuestionDao extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {
}
