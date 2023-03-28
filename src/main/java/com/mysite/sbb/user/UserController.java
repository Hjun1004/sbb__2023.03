package com.mysite.sbb.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm){
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "signup_form";
        }

        if(!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())){
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            // bindingResult.rejectValue의 각 파라미터는 bindingResult.rejectValue(필드명, 오류코드, 에러메시지)를
            // 의미하며 여기서 오류코드는 일단 "passwordInCorrect"로 정의했다.
            return "signup_form";
        }

        try{
            userService.create(
                    userCreateForm.getUsername(),
                    userCreateForm.getEmail(),
                    userCreateForm.getPassword1());
        }catch(DataIntegrityViolationException e){
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        }catch(Exception e){
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(){
        return "login_form";
    }
    // 로그인 폼 처리를 위한 PostMapping은 스프링 시큐리티가 알아서 처리하기 때문에
    // 따로 안만들어도 된다.



}
