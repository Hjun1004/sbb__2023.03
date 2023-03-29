package com.mysite.sbb.question.questionRepository;

import com.mysite.sbb.question.entity.Question;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    @Transactional
    // @Modifying // 만약 아래 쿼리가 SELECT가 아니라면 이걸 붙여야 한다.
    @Modifying
    // nativeQuery = true 여야 MySQL 쿼리문법 사용 가능
    @Query(value = "ALTER TABLE question AUTO_INCREMENT = 1", nativeQuery = true)
    void clearAutoIncrement();

    Question findBySubject(String s);

    Question findBySubjectAndContent(String s, String s1);

    List<Question> findBySubjectLike(String s);

    Page<Question> findAll(Specification<Question> spec, Pageable pageable);
    // 검색어의 조건을 만족하는 질문들(질문에는 답변, 작성자, 답변 작성자, 질문 제목, 질문 내용 등 다양한 요소를 만족하는)
    // 의 리스트를 spec에 저장하고, 그걸 pageable로 10개씩 표현하도록 한다. 정도?
}
