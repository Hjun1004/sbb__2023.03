package com.mysite.sbb.answer;

import com.mysite.sbb.answer.entity.Answer;
import com.mysite.sbb.answer.service.AnswerService;
import com.mysite.sbb.question.QuestionForm;
import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.service.QuestionService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Member;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Getter
@Setter
@RequestMapping("/answer")
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

//    @PostMapping(value = "/create/{id}")
//    public String createAnswer(@PathVariable("id") Integer id, AnswerForm answerForm){
//        // @RequestParam String content는 템플릿에서 답변으로 입력한 내용(content)을 얻기 위해 추가되었다.
//        Question q = this.questionService.getQuestion(id);
////        this.answerService.create(q,answerForm.getContent());
//        return "redirect:/question/detail/%d".formatted(id);
//    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(@PathVariable("id") Integer id,
                               Model model,
                               @Valid AnswerForm answerForm,
                               BindingResult bindingResult,
                               Principal principal)
    {
        Question q = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());

        if(bindingResult.hasErrors()){
            model.addAttribute("question", q);
            return "question_detail";
        }

        Answer answer = answerService.create(q, answerForm.getContent(), siteUser);

        return "redirect:/question/detail/%d#answer_%d".formatted(id,answer.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal){
        Answer answer = answerService.getAnswer(id);
        if(!answer.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        answerForm.setContent(answer.getContent());
        return "answer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid AnswerForm answerForm, BindingResult bindingResult, @PathVariable("id") Integer id, Principal principal){
        if(bindingResult.hasErrors()){
            return "answer_form";
        }

        Answer answer = answerService.getAnswer(id);

        if(!answer.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        answerService.modify(answer, answerForm.getContent());

        return "redirect:/question/detail/%d#answer_%d".formatted(answer.getQuestion().getId(),id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id){
        Answer answer = answerService.getAnswer(id);
        if(!answer.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        answerService.delete(answer);

        return "redirect:/question/detail/%d".formatted(answer.getQuestion().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String voteDelete(Principal principal, @PathVariable("id") Integer id){
        Answer answer = answerService.getAnswer(id);
        SiteUser siteUser = userService.getUser(principal.getName());

        answerService.vote(answer, siteUser);
        return "redirect:/question/detail/%d#answer_%d".formatted(answer.getQuestion().getId(),id);
    }


}
