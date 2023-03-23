package com.mysite.sbb.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class QuestionForm {
    @NotBlank(message = "제목은 필수항목입니다.") // 아래의 변수는 비어있으면 안된다.
    @Size(max=65, message = "제목을 200자 이하로 설정해주세요.") // 최대 200까지 가능하다.
    private String subject;

    @NotBlank(message = "내용은 필수항목입니다.")
    @Size(max=20000, message = "제목을 20000자 이하로 설정해주세요.")
    private String content;
}
