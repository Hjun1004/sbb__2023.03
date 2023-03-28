package com.mysite.sbb.question;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.answer.entity.Answer;
import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.questionRepository.QuestionRepository;
import com.mysite.sbb.question.service.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
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
    private final UserService userService;

    @GetMapping("list")
    public String list(Model model, @RequestParam(defaultValue = "0") int page){ // int page 가 곧 name = page와 같다.
        Page<Question> paging = questionService.getlist(page);
        model.addAttribute("paging", paging);
        return "question_list.html";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm){
        Question q = this.questionService.getQuestion(id);
        model.addAttribute("question", q);
        return "question_detail.html";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    // @Valid를 붙여야 QuestionForm.java내의 NotBlank나 Size가 동작한다.
    public String questionCreate2(QuestionForm questionForm){
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    // @Valid QuestionForm questionForm
    // questionForm 값을 바인딩 할 때 유효성 체크를 해라!
    // questionForm 변수와 bindingResult 변수는 model.addAttribute 없이 바로 뷰에서 접근할 수 있다.
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            // question_form.html 실행
            // 다시 작성하라는 의미로 응답에 폼을 실어서 보냄
            return "question_form.html";
        }
        SiteUser siteUser = userService.getUser(principal.getName());
        questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);

        return "redirect:/question/list"; // 질문 저장 후 질문목록으로 이동
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal){
        Question question = questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        // questionForm은 question_form.html에서 단순히 보여지는 값들을 담기 위한 객체이고 실질적으로
        // 저장되는 값들은 question 엔티티내이기 때문에 modify/{id}}의 questionForm에 원래 저장되어있던
        // subject와 content를 입력하여 question_form을 실행하면 원래 값들이 들어간 questionForm이 보인다.
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, @PathVariable("id") Integer id, Principal principal){
        if(bindingResult.hasErrors()){
            return "question_form";
        }
        Question question = questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionService.modify(question, questionForm.getSubject(), questionForm.getContent());

        return "redirect:/question/detail/%d".formatted(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id){
        Question question = questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        questionService.delete(question);
        return "redirect:/";
    }


}
