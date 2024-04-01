package com.benkitoucoders.ecommerce.mappers.quiz;

import com.benkitoucoders.ecommerce.dtos.quiz.QuestionDto;
import com.benkitoucoders.ecommerce.entities.quiz.Question;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
@Component
public interface QuestionMapper {
    QuestionDto modelToDto(Question question);

    List<QuestionDto> modelsToDtos(List<Question> questionList);

    Question dtoToModel(QuestionDto questionDto);
}
