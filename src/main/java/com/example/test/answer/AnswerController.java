package com.example.test.answer;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.example.test.question.QuestionDTO;
import com.example.test.question.QuestionService;
import com.example.test.user.SiteUser;
import com.example.test.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {
	private final QuestionService qService;
	private final AnswerService aService;
	private final UserService userService;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String createAnswer(Model model, @PathVariable("id") Integer id , 
			@Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
		QuestionDTO qDTO = this.qService.getQDTO(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		if(bindingResult.hasErrors()) {
			model.addAttribute("q",qDTO);
			return "question_detail";
		}
		AnswerDTO answerDTO = this.aService.create(qDTO, answerForm.getContent(), siteUser);
		return String.format("redirect:/question/detail/%s#answer_%s",answerDTO.getQDTO().getId(), answerDTO.getId());
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String answerModify(AnswerForm answerForm , @PathVariable("id") Integer id, 
			Principal principal) {
		AnswerDTO answerDTO = this.aService.getAnswer(id);
		if(!answerDTO.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "답변 수정권한이 없습니다");
		}
		answerForm.setContent(answerDTO.getContent());
		return "answer_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String answerModify(@Valid AnswerForm answerForm , BindingResult bindingResult, 
			@PathVariable("id") Integer id, Principal principal) {
		if(bindingResult.hasErrors()) {
			return "answer_form";
		}
		AnswerDTO answerDTO = this.aService.getAnswer(id);
		if(!answerDTO.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다");
			
		}
		this.aService.modify(answerDTO, answerForm.getContent());
		return String.format("redirect:/question/detail/%s#answer_%s", answerDTO.getQDTO().getId(),answerDTO.getId());
		
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
		AnswerDTO answerDTO = this.aService.getAnswer(id);
		if(!answerDTO.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다");
		}
		this.aService.delete(answerDTO);
		return String.format("redirect:/question/detail/%s", answerDTO.getQDTO().getId());
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String answerVote(Principal principal , @PathVariable("id") Integer id) {
		AnswerDTO answerDTO = this.aService.getAnswer(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.aService.vote(answerDTO, siteUser);
		return String.format("redirect:/question/detail/%s#answer_%s",answerDTO.getQDTO().getId(),answerDTO.getId());
	}
	
	

}
