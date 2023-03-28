package com.mysite.sbb.answer.entity;

import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
@ToString
public class Answer {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    @ManyToOne // 다른 엔티티 클래스 리모콘을 저장할 때는 꼭 관계를 적어준다.
    @ToString.Exclude
    private Question question;

    @ManyToOne
    private SiteUser author;
}
