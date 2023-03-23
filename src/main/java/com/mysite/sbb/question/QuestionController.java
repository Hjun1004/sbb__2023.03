package com.mysite.sbb.question;

import com.mysite.sbb.answer.entity.Answer;
import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.questionRepository.QuestionRepository;
import com.mysite.sbb.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Getter
@Setter
@RequestMapping("/question")
// @Validated // 아래의 메서드에 @Valid를 붙이기 위해 클래스에 @Validated를 붙여야 하지만
// 컨트롤러는 생략이 가능하다.
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

    @GetMapping("/create")
    public String questionCreate2(QuestionForm questionForm){
        return "question_form";
    }

    @PostMapping("/create")
    // @Valid QuestionForm questionForm
    // questionForm 값을 바인딩 할 때 유효성 체크를 해라!
    // questionForm 변수와 bindingResult 변수는 model.addAttribute 없이 바로 뷰에서 접근할 수 있다.
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            // question_form.html 실행
            // 다시 작성하라는 의미로 응답에 폼을 실어서 보냄
            return "question_form.html";
        }

        questionService.create(questionForm.getSubject(), questionForm.getContent());

        return "redirect:/question/list"; // 질문 저장 후 질문목록으로 이동
    }



}
