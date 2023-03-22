package com.mysite.sbb.question;

import com.mysite.sbb.answer.entity.Answer;
import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.questionRepository.QuestionRepository;
import com.mysite.sbb.question.service.QuestionService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Getter
@Setter
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("list")
    public String list(Model model){
        List<Question> questionsList = this.questionService.getlist();
        model.addAttribute("questionList", questionsList);
        return "question_list.html";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id){
        Question q = this.questionService.getQuestion(id);
        model.addAttribute("question", q);
        return "question_detail.html";
    }



}
