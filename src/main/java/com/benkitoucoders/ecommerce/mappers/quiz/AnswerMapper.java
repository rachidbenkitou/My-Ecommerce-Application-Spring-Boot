package com.benkitoucoders.ecommerce.mappers.quiz;

import com.benkitoucoders.ecommerce.dtos.quiz.AnswerDto;
import com.benkitoucoders.ecommerce.entities.quiz.Answer;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
@Component
public interface AnswerMapper {

    AnswerDto modelToDto(Answer answer);

    List<AnswerDto> modelsToDtos(List<Answer> answerList);

    Answer dtoToModel(AnswerDto answerDto);

}
