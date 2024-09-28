package com.example.test.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.test.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {
	private final QuestionRepository qRepo;
	
	public Page<QuestionDTO> getList(int page){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		PageRequest pageable = PageRequest.of(page, 10 , Sort.by(sorts));
		return this.qRepo.findAll(pageable);
	}
	
	public QuestionDTO getQDTO(Integer id) {
		Optional<QuestionDTO> oq = this.qRepo.findById(id);
		if(oq.isPresent()) {
			return oq.get();
		}else {
			throw new DataNotFoundException("QuestionDTO not found");
		}
	}
	
	public void create(String subject, String content) {
		QuestionDTO qDTO = new QuestionDTO();
		qDTO.setSubject(subject);
		qDTO.setContent(content);
		qDTO.setCreateDate(LocalDateTime.now());
		this.qRepo.save(qDTO);
	}

}
