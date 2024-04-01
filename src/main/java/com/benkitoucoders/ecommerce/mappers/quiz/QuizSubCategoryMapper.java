package com.benkitoucoders.ecommerce.mappers.quiz;


import com.benkitoucoders.ecommerce.dtos.quiz.QuizSubCategoryDto;
import com.benkitoucoders.ecommerce.entities.quiz.QuizSubCategory;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
@Component
public interface QuizSubCategoryMapper {
    QuizSubCategoryDto modelToDto(QuizSubCategory quizSubCategory);

    List<QuizSubCategoryDto> modelsToDtos(List<QuizSubCategory> quizSubCategoryList);

    QuizSubCategory dtoToModel(QuizSubCategoryDto quizSubCategoryDto);
}
