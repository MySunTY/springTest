package com.example.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.test.answer.AnswerDTO;
import com.example.test.question.QuestionDTO;
import com.example.test.question.QuestionRepository;
import com.example.test.question.QuestionService;

import jakarta.transaction.Transactional;




@SpringBootTest
class SbbApplicationTests {
	
	@Autowired
	private QuestionService questionService;
	
	@Test
	void testJpa() {
		for(int i = 0 ; i<=300 ; i++) {
			String subject = String.format("테스트 데이터입니다:[%03d]",i);
			String content = "내용 없음";
			this.questionService.create(subject, content);
		}
		
		
		
	}

}
