package com.mysite.sbb.answer.service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.answer.answerRepository.AnswerRepository;
import com.mysite.sbb.answer.entity.Answer;
import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.user.SiteUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public Answer create(Question question, String content, SiteUser author){
        Answer answer = new Answer();
        //answer.setQuestion(question);
        question.addAnswer(answer);

        answer.setContent(content);
        answer.setAuthor(author);
        answerRepository.save(answer);

        return answer;
    }

    public Answer getAnswer(Integer id){
        Optional<Answer> oa = answerRepository.findById(id);
        if(oa.isPresent()){
            return oa.get();
        }
        else{
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(Answer answer, String content){
        answer.setContent(content);
        answerRepository.save(answer);
    }

    public void delete(Answer answer){
        answerRepository.delete(answer);
    }

}
