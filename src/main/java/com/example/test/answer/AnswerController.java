package com.example.test.answer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.test.question.QuestionDTO;
import com.example.test.question.QuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {
	private final QuestionService qService;
	private final AnswerService aService;
	
	@PostMapping("/create/{id}")
	public String createAnswer(Model model, @PathVariable("id") Integer id , 
			@Valid AnswerForm answerForm, BindingResult bindingResult) {
		QuestionDTO qDTO = this.qService.getQDTO(id);
		if(bindingResult.hasErrors()) {
			model.addAttribute("q",qDTO);
			return "question_detail";
		}
		this.aService.create(qDTO, answerForm.getContent());
		return String.format("redirect:/question/detail/%s",id);
	}
	

}
