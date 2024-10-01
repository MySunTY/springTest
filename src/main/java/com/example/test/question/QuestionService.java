package com.example.test.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.test.DataNotFoundException;
import com.example.test.answer.AnswerDTO;
import com.example.test.user.SiteUser;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {
	private final QuestionRepository qRepo;
	
	
	public Page<QuestionDTO> getList(int page , String kw){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		PageRequest pageable = PageRequest.of(page, 10 , Sort.by(sorts));
		Specification<QuestionDTO> spec = search(kw);
		return this.qRepo.findAll(spec , pageable);
	}
	
	public QuestionDTO getQDTO(Integer id) {
		Optional<QuestionDTO> oq = this.qRepo.findById(id);
		if(oq.isPresent()) {
			return oq.get();
		}else {
			throw new DataNotFoundException("QuestionDTO not found");
		}
	}
	
	public void create(String subject, String content , SiteUser user) {
		QuestionDTO qDTO = new QuestionDTO();
		qDTO.setSubject(subject);
		qDTO.setContent(content);
		qDTO.setCreateDate(LocalDateTime.now());
		qDTO.setAuthor(user);
		this.qRepo.save(qDTO);
	}
	
	public void modify(QuestionDTO questionDTO, String subject, String content) {
		questionDTO.setSubject(subject);
		questionDTO.setContent(content);
		questionDTO.setModifyDate(LocalDateTime.now());
		this.qRepo.save(questionDTO);
	}
	
	public void delete(QuestionDTO questionDTO) {
		this.qRepo.delete(questionDTO);
	}
	
	public void vote(QuestionDTO questionDTO, SiteUser siteUser) {
		questionDTO.getVoter().add(siteUser);
		this.qRepo.save(questionDTO);
	}
	
	public Specification<QuestionDTO> search(String kw){
		return new Specification<>() {
			private static final long serialVersionUID= 1L;
			@Override
			public Predicate toPredicate(Root<QuestionDTO> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true); // 중복제거
				Join<QuestionDTO, SiteUser> u1 = q.join("author" ,  JoinType.LEFT);
				Join<QuestionDTO, AnswerDTO> a = q.join("answerList" , JoinType.LEFT);
				Join<AnswerDTO, SiteUser> u2 = a.join("author" , JoinType.LEFT);
				return cb.or(cb.like(q.get("subject"), "%"+kw+"%"), //제목
						cb.like(q.get("content"), "%"+kw+"%"), //내용
						cb.like(u1.get("username"), "%"+kw+"%"), //질문 작성자
						cb.like(a.get("content"), "%"+kw+"%"), // 답변 내용
						cb.like(u2.get("username"), "%"+kw+"%"));
				
				
			}
		};
	}

}
