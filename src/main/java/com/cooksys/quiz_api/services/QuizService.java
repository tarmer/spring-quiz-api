package com.cooksys.quiz_api.services;

import java.util.List;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;

public interface QuizService {

	List<QuizResponseDto> getAllQuizzes();

	QuizResponseDto createQuiz(QuizRequestDto quizRequestDto);

	QuizResponseDto deleteQuiz(Long id);

	QuizResponseDto updateQuiz(Long id, String newName);

	QuestionResponseDto randomQuestionFromQuiz(Long id);

	QuizResponseDto addQuestionToQuiz(QuestionRequestDto questionRequestDto, Long id);

	QuestionResponseDto deleteQuestionFromQuiz(Long questionId, Long id);

}
