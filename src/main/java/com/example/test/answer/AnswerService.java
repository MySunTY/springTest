package com.example.test.answer;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.test.question.QuestionDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class AnswerService {
	private final AnswerRepository aRepo;
	
	public void create(QuestionDTO qDTO, String content) {
		AnswerDTO aDTO = new AnswerDTO();
		aDTO.setContent(content);
		aDTO.setCreateDate(LocalDateTime.now());
		aDTO.setQDTO(qDTO);
		this.aRepo.save(aDTO);
		
	
	}
	
	
	
	

}
