package com.benkitoucoders.ecommerce.quiz.mappers;

import com.benkitoucoders.ecommerce.quiz.dto.QuestionDto;
import com.benkitoucoders.ecommerce.quiz.entity.Question;
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
