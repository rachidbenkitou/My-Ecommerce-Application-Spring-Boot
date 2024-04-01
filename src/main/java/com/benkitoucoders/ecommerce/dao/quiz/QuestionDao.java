package com.benkitoucoders.ecommerce.dao.quiz;

import com.benkitoucoders.ecommerce.entities.quiz.Answer;
import com.benkitoucoders.ecommerce.entities.quiz.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QuestionDao extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {
}
