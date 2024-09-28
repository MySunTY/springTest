package com.example.test.answer;

import java.time.LocalDateTime;

import com.example.test.question.QuestionDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity

public class AnswerDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(columnDefinition ="TEXT")
	private String content;
	
	private LocalDateTime createDate;
	
	@ManyToOne
	private QuestionDTO qDTO;

	
	
}
