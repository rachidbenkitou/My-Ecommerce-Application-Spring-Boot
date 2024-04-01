package com.benkitoucoders.ecommerce.mappers.quiz;

import com.benkitoucoders.ecommerce.dtos.quiz.QuizCategoryDto;
import com.benkitoucoders.ecommerce.entities.quiz.QuizCategory;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
@Component
public interface QuizCategoryMapper {
    QuizCategoryDto modelToDto(QuizCategory category);

    List<QuizCategoryDto> modelsToDtos(List<QuizCategory> quizCategoryList);

    QuizCategory dtoToModel(QuizCategoryDto quizCategoryDto);
}
