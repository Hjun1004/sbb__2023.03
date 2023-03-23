package com.mysite.sbb.answer.service;

import com.mysite.sbb.answer.answerRepository.AnswerRepository;
import com.mysite.sbb.answer.entity.Answer;
import com.mysite.sbb.question.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public Answer create(Question question, String content){
        Answer answer = new Answer();
        answer.setQuestion(question);
        answer.setContent(content);
        this.answerRepository.save(answer);

        return answer;
    }

}
