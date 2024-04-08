package com.benkitoucoders.ecommerce.quiz.mappers;

import com.benkitoucoders.ecommerce.quiz.dto.AnswerDto;
import com.benkitoucoders.ecommerce.quiz.entity.Answer;
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
