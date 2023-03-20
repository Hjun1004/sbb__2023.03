package com.mysite.sbb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@AllArgsConstructor
@Getter
@Setter
public class QuestionController {
    @GetMapping("/question/list")
    @ResponseBody
    public String list(){
        return "question list";
    }
}
