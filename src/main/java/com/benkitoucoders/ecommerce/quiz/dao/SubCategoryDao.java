package com.benkitoucoders.ecommerce.quiz.dao;

import com.benkitoucoders.ecommerce.quiz.entity.QuizSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubCategoryDao extends JpaRepository<QuizSubCategory, Long>, JpaSpecificationExecutor<QuizSubCategory> {
}
