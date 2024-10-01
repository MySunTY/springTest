package com.example.test.question;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
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

import com.example.test.answer.AnswerForm;
import com.example.test.user.SiteUser;
import com.example.test.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RequestMapping("/question")
@RequiredArgsConstructor //final이붙은애 자동으로 생성자 생
@Controller
public class QuestionController {
	
	private final QuestionService qService;
	private final UserService userService;
	
	
	@GetMapping("/list")
	
	public String list(Model model  ,@RequestParam(value= "page" , defaultValue="0") int page, 
			@RequestParam(value="kw" , defaultValue="") String kw) {
		Page<QuestionDTO> qList = this.qService.getList(page , kw);
		model.addAttribute("qList",qList);
		model.addAttribute("kw", kw);
		return "question_list";
	}
	
	@GetMapping(value= "/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id ,AnswerForm answerForm) {
		QuestionDTO q = this.qService.getQDTO(id);
		model.addAttribute("q",q);
		return "question_detail";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String QuestionCreate(QuestionForm questionForm) {
		return "question_form";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String QuestionCreate(@Valid QuestionForm qForm, BindingResult bResult , Principal principal) {
		if(bResult.hasErrors()) {
			return "question_form";
		}
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.qService.create(qForm.getSubject(), qForm.getContent(),siteUser);
		return "redirect:/question/list";
		
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String questionModify(QuestionForm questionForm , @PathVariable("id") Integer id, Principal principal) {
		QuestionDTO qDTO = this.qService.getQDTO(id);
		if(!qDTO.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다");
		}
		questionForm.setSubject(qDTO.getSubject());
		questionForm.setContent(qDTO.getContent());
		return "question_form";
	}
	
	@PostMapping("/modify/{id}")
	public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, 
			Principal principal, @PathVariable("id") Integer id) {
		if(bindingResult.hasErrors()) {
			return "question_form";
		}
		QuestionDTO questionDTO = this.qService.getQDTO(id);
		if(!questionDTO.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다");
		}
		this.qService.modify(questionDTO, questionForm.getSubject(), questionForm.getContent());
		return String.format("redirect:/question/detail/%s", id);
		
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String questionDelete(Principal principal , @PathVariable("id") Integer id) {
		QuestionDTO questionDTO = this.qService.getQDTO(id);
		if(!questionDTO.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이없습니다.");
			
		}
		this.qService.delete(questionDTO);
		return "redirect:/";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String questionVote(Principal principal , @PathVariable("id") Integer id) {
		QuestionDTO questionDTO = this.qService.getQDTO(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.qService.vote(questionDTO, siteUser);
		return String.format("redirect:/question/detail/%s", id);
	}
	
	

}
