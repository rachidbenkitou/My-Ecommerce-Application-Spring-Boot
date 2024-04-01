package com.benkitoucoders.ecommerce.dao.quiz;

import com.benkitoucoders.ecommerce.entities.quiz.Answer;
import com.benkitoucoders.ecommerce.entities.quiz.QuizCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryDao extends JpaRepository<QuizCategory, Long>, JpaSpecificationExecutor<QuizCategory> {
}
