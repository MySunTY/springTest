package com.example.test.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.test.answer.AnswerForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RequestMapping("/question")
@RequiredArgsConstructor //final이붙은애 자동으로 생성자 생
@Controller
public class QuestionController {
	
	private final QuestionService qService;
	
	
	@GetMapping("/list")
	
	public String list(Model model  ,@RequestParam(value= "page" , defaultValue="0") int page) {
		Page<QuestionDTO> qList = this.qService.getList(page);
		model.addAttribute("qList",qList);
		
		return "question_list";
	}
	
	@GetMapping(value= "/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id ,AnswerForm answerForm) {
		QuestionDTO q = this.qService.getQDTO(id);
		model.addAttribute("q",q);
		return "question_detail";
	}
	
	@GetMapping("/create")
	public String QuestionCreate(QuestionForm questionForm) {
		return "question_form";
	}
	@PostMapping("/create")
	public String QuestionCreate(@Valid QuestionForm qForm, BindingResult bResult) {
		if(bResult.hasErrors()) {
			return "question_form";
		}
		
		this.qService.create(qForm.getSubject(), qForm.getContent());
		return "redirect:/question/list";
		
	}
	

}
