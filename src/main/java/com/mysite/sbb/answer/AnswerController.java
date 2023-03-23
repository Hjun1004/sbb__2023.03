package com.mysite.sbb.answer;

import com.mysite.sbb.answer.entity.Answer;
import com.mysite.sbb.answer.service.AnswerService;
import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.service.QuestionService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;

@Controller
@RequiredArgsConstructor
@Getter
@Setter
@RequestMapping("/answer")
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;

//    @PostMapping(value = "/create/{id}")
//    public String createAnswer(@PathVariable("id") Integer id, AnswerForm answerForm){
//        // @RequestParam String content는 템플릿에서 답변으로 입력한 내용(content)을 얻기 위해 추가되었다.
//        Question q = this.questionService.getQuestion(id);
////        this.answerService.create(q,answerForm.getContent());
//        return "redirect:/question/detail/%d".formatted(id);
//    }

    @PostMapping("/create/{id}")
    public String createAnswer(@PathVariable("id") Integer id,
                               Model model,
                               @Valid AnswerForm answerForm,
                               BindingResult bindingResult)
    {
        Question q = this.questionService.getQuestion(id);

        if(bindingResult.hasErrors()){
            model.addAttribute("question", q);
            return "question_detail";
        }

        Answer answer = answerService.create(q, answerForm.getContent());

        return "redirect:/question/detail/%d".formatted(id);
    }

}
