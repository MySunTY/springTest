package com.example.test.answer;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.test.DataNotFoundException;
import com.example.test.question.QuestionDTO;
import com.example.test.user.SiteUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class AnswerService {
	private final AnswerRepository aRepo;
	
	public AnswerDTO create(QuestionDTO qDTO, String content, SiteUser author) {
		AnswerDTO aDTO = new AnswerDTO();
		aDTO.setContent(content);
		aDTO.setCreateDate(LocalDateTime.now());
		aDTO.setQDTO(qDTO);
		aDTO.setAuthor(author);
		this.aRepo.save(aDTO);
		return aDTO;
		
	
	}
	
	public AnswerDTO getAnswer(Integer id) {
		Optional<AnswerDTO> answerDTO = this.aRepo.findById(id);
		if(answerDTO.isPresent()) {
			return answerDTO.get();
		}else {
			throw new DataNotFoundException("answerDTO not found");
		}
		
	}
	
	public void modify(AnswerDTO answerDTO, String content) {
		answerDTO.setContent(content);
		answerDTO.setModifyDate(LocalDateTime.now());
		this.aRepo.save(answerDTO);
	}
	
	public void delete(AnswerDTO answerDTO) {
		this.aRepo.delete(answerDTO);
	}
	
	public void vote(AnswerDTO answerDTO, SiteUser siteUser){
		answerDTO.getVoter().add(siteUser);
		this.aRepo.save(answerDTO);
	}
	
	

}
