package com.benkitoucoders.ecommerce.dao.quiz;

import com.benkitoucoders.ecommerce.entities.quiz.QuizSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubCategoryDao extends JpaRepository<QuizSubCategory, Long>, JpaSpecificationExecutor<QuizSubCategory> {
}
