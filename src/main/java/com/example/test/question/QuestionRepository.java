package com.example.test.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<QuestionDTO, Integer> {
	
	QuestionDTO findBySubject(String subject);
	QuestionDTO findBySubjectAndContent(String subject, String content);
	List<QuestionDTO> findBySubjectLike(String subject);
	Page<QuestionDTO> findAll(Pageable pageable);
	Page<QuestionDTO> findAll(Specification<QuestionDTO> spec, Pageable pageable);
	
}
