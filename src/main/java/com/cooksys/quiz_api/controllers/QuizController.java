package com.cooksys.quiz_api.controllers;

import java.util.List;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.services.QuizService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

  private final QuizService quizService;

  @GetMapping
  public List<QuizResponseDto> getAllQuizzes() {
    return quizService.getAllQuizzes();
  }
  
  // TODO: Implement the remaining 6 endpoints from the documentation.
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public QuizResponseDto createQuiz(@RequestBody QuizRequestDto quizRequestDto) {
	  return quizService.createQuiz(quizRequestDto);
  }
  
  @DeleteMapping("/{id}")
  public QuizResponseDto deleteQuiz(@PathVariable(name ="id") Long id) {
	 return quizService.deleteQuiz(id);
  }
  
  @PatchMapping("/{id}/rename/{newName}")
  public QuizResponseDto updateQuiz(@PathVariable(name ="id") Long id, @PathVariable(name ="newName") String newName) {
	  return quizService.updateQuiz(id,newName);
  }
  
  @GetMapping("/{id}/random")
  public QuestionResponseDto randomQuestion(@PathVariable(name ="id") Long id) {
	  return quizService.randomQuestionFromQuiz(id);
  }
  
  @PatchMapping("/{id}/add")
  public QuizResponseDto addQuestion(@PathVariable(name ="id") Long id, @RequestBody QuestionRequestDto questionRequestDto) {
	 return quizService.addQuestionToQuiz(questionRequestDto,id);
  }
  
  @DeleteMapping("/{id}/delete/{questionID}")
  public QuestionResponseDto deleteQuestion(@PathVariable(name ="id") Long id, @PathVariable(name ="questionID") Long questionId) {
	  return quizService.deleteQuestionFromQuiz(questionId,id);
  }
  

}
