package com.mysite.sbb;

import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.questionRepository.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Test
	@DisplayName("데이터 저장")
	void t001() {
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		this.questionRepository.save(q1);

		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		this.questionRepository.save(q2);


	}

	/*
  SQL
  SELECT * FROM question
  */
	@Test
	@DisplayName("findAll")
	void t002() {
		List<Question> all = this.questionRepository.findAll();
		assertEquals(2, all.size());

		Question q = all.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}

	/*
  	SQL
  	SELECT * FROM question
  	WHERE id = 1;
  */
	@Test
	@DisplayName("findById")
	void t003() {
		Optional<Question> oq = questionRepository.findById(1);

		if(oq.isPresent()){
			Question q = oq.get();
			assertEquals("sbb가 무엇인가요?", q.getSubject());
		}
	}


	/*
  	SQL
  	SELECT * FROM question
  	WHERE subject = "sbb가 무엇인가요?"
  	*/
	@Test
	@DisplayName("findBySubject")
	void t004() {
		Question q = questionRepository.findBySubject("sbb가 무엇인가요?");
	}


	/*
  	SQL
  	SELECT * FROM question
  	WHERE subject = "sbb가 무엇인가요?" AND content = "sbb에 대해 알고 싶습니다."
  	*/
	@Test
	@DisplayName("findBySubjectandContent")
	void t005() {
		Question q = questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해 알고 싶습니다.");
		assertEquals(1, q.getId());
	}
	@Test
	@DisplayName("findBySubjectLike")
	void t006() {
		List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
		Question q = qList.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}





}
