package com.mysite.sbb;

import com.mysite.sbb.answer.answerRepository.AnswerRepository;
import com.mysite.sbb.answer.entity.Answer;
import com.mysite.sbb.question.entity.Question;
import com.mysite.sbb.question.questionRepository.QuestionRepository;
import com.mysite.sbb.question.service.QuestionService;
import com.mysite.sbb.user.UserRepository;
import com.mysite.sbb.user.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionService questionService;

	@Autowired
	private UserService userService;

	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
		// 아래 메서드는 각 테스트케이스가 실행되기 전에 실행된다.
	void beforeEach() {
		// 모든 데이터 삭제
		answerRepository.deleteAll();
		answerRepository.clearAutoIncrement();

		// 모든 데이터 삭제
		questionRepository.deleteAll();

		// 모든 데이터 삭제
		userRepository.deleteAll();
		userRepository.clearAutoIncrement();

		// 흔적삭제(다음번 INSERT 때 id가 1번으로 설정되도록)
		questionRepository.clearAutoIncrement();

		// 회원 2명 생성
		userService.create("user1", "user1@test.com", "1234");
		userService.create("user2", "user2@test.com", "1234");

		// 질문 1개 생성
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		questionRepository.save(q1);  // 첫번째 질문 저장

		// 질문 1개 생성
		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		questionRepository.save(q2);  // 두번째 질문 저장

		// 답변 1개 생성
		Answer a1 = new Answer();
		a1.setContent("네 자동으로 생성됩니다.");
		q2.addAnswer(a1);
		answerRepository.save(a1);
	}

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
		//assertEquals(2, all.size());

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

	@Test
	@DisplayName("dataModify")
	void t007() {
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		q.setSubject("수정된 제목");
		this.questionRepository.save(q);
	}

	@Test
	@DisplayName("dataDelete")
	void t008() {
		assertEquals(2,this.questionRepository.count());
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		this.questionRepository.delete(q);
		assertEquals(1,this.questionRepository.count());
	}

	@Test
	@DisplayName("CreateAnswer")
	void t009() {
		Optional<Question> oq = this.questionRepository.findById(2); //  Id가 2인 qusetion return받음
		assertTrue(oq.isPresent());
		Question q = oq.get(); // q에는 Id가 2인 question리모컨이 저장됨

		Answer answer = new Answer();
		answer.setContent("네 자동으로 생성됩니다.");
		answer.setQuestion(q); // 답변 객체 내의 question변수에 2번 질문의 답이란는걸 식별하기 위해 2번 질문인 q를 넣어줌
		this.answerRepository.save(answer);
	}

	@Test
	@DisplayName("SelectAnswer")
	void t010() {
		Optional<Answer> oa = this.answerRepository.findById(1);
		assertTrue(oa.isPresent());
		Answer a = oa.get();
		//assertEquals("네 자동으로 생성됩니다.", a.getContent());
		assertEquals(2, a.getQuestion().getId() );
	}

	@Test
	@DisplayName("findAnswerFromQuestion")
	void t011() {
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		List<Answer> la = q.getAnswerList();

		assertEquals(1, la.size());
		//Answer a = la.get(0);
		//assertEquals(a.getQuestion().getId(), q.getId());
		assertEquals("네 자동으로 생성됩니다.", la.get(0).getContent());

		// 이건 오류가 발생하는 테스트 케이스이다.
		// 왜냐하면 Question 리포지터리가 findById를 호출하여 Question 객체를 조회하고 나면 DB세션이 끊어지기 때문이다.
		// 그 이후에 실행되는 q.getAnswerList() 메서드는 세션이 종료되어 오류가 발생한다.
		// 답변 데이터 리스트는 q 객체를 조회할때 가져오지 않고 q.getAnswerList() 메서드를 호출하는 시점에 가져오기 때문이다.
		// Lazy방식 : 필요한 시점에 데이터를 가져오는것 (이 테스트 케이스에 해당하는것)
		// Eager방식 : q 객체를 조회할 때 답변 리스트를 모두 가져오는 방식
		// 이러한 경우는 테스트 케이스에서만 발생하는 문제다.
		// 테스트 케이스에서 이러한 문제를 해결하기 위해서는 @Transactional 어노테이션을 이용하면된다.
	}

	@Test
	@Transactional
	@DisplayName("findAnswerFromQuestion")
	void t012() {
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		List<Answer> la = q.getAnswerList();

		assertEquals(1, la.size());
		Answer a = la.get(0);
		assertEquals(a.getQuestion().getId(), q.getId());
		assertEquals("네 자동으로 생성됩니다.", la.get(0).getContent());

		// 이렇게 @Transactional 어노테이션을 추가하면 오류가 발생하지 않는다.
	}

	@Test
	@DisplayName("질문에 달린 답변 찾기")
	void t013() {
		IntStream.rangeClosed(3,300).forEach(no -> questionService.create("테스트 제목입니다. %d".formatted(no), "테스트 내용입니다. %d".formatted(no)));
	}

}