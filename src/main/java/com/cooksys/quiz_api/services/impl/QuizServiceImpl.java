package com.cooksys.quiz_api.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Answer;
import com.cooksys.quiz_api.entities.Question;
import com.cooksys.quiz_api.entities.Quiz;
import com.cooksys.quiz_api.mappers.QuestionMapper;
import com.cooksys.quiz_api.mappers.QuizMapper;
import com.cooksys.quiz_api.repositories.AnswerRepository;
import com.cooksys.quiz_api.repositories.QuestionRepository;
import com.cooksys.quiz_api.repositories.QuizRepository;
import com.cooksys.quiz_api.services.QuizService;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

	private final QuizRepository quizRepository;
	private final QuizMapper quizMapper;
	private final QuestionMapper questionMapper;
	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;

	private Quiz getQuiz(Long id) {
		Optional<Quiz> optionalQuiz = quizRepository.findById(id);
		if (optionalQuiz.isEmpty()) {
			System.out.println("Invalid Quiz ID!!!");
		}
		return optionalQuiz.get();
	}

	@Override
	public List<QuizResponseDto> getAllQuizzes() {
		return quizMapper.entitiesToDtos(quizRepository.findAll());
	}

	@Override
	public QuizResponseDto createQuiz(QuizRequestDto quizRequestDto) {
		Quiz quizToBeSaved = quizMapper.requestDtoToEntity(quizRequestDto);
		Quiz x = quizRepository.save(quizToBeSaved);
		List<Question> questions = quizToBeSaved.getQuestions();

		for (Question q : questions) {
			Question qq;
			if (q != null) {
				q.setQuiz(x);
				qq = questionRepository.saveAndFlush(q);
				List<Answer> aList = q.getAnswers();
				for (Answer a : aList) {
					if (a != null) {
						a.setQuestion(qq);
						answerRepository.saveAndFlush(a);
					}
				}

			}
		}

		return quizMapper.entityToDto(quizRepository.saveAndFlush(quizToBeSaved));
	}

	@Override
	@Transactional
	public QuizResponseDto deleteQuiz(Long id) {
		Quiz quizToBeDeleted = getQuiz(id);
		List<Question> questions = quizToBeDeleted.getQuestions();
		for (Question eachQuestion : questions) {
			List<Answer> answers = eachQuestion.getAnswers();
			for (Answer eachAnswer : answers) {
				answerRepository.delete(eachAnswer);
			}
			questionRepository.delete(eachQuestion);
		}
		quizRepository.delete(quizToBeDeleted);
		return quizMapper.entityToDto(quizToBeDeleted);
	}

	@Override
	public QuizResponseDto updateQuiz(Long id, String newName) {
		Quiz quizToBeUpdated = getQuiz(id);
		if (newName != null)
			quizToBeUpdated.setName(newName);
		return quizMapper.entityToDto(quizRepository.saveAndFlush(quizToBeUpdated));
	}

	@Override
	public QuestionResponseDto randomQuestionFromQuiz(Long id) {
		Quiz selectedQuiz = getQuiz(id);
		Random rand = new Random();
		List<Question> questions = selectedQuiz.getQuestions();
		Question selectedQuestion = questions.get(rand.nextInt(questions.size()));

		return questionMapper.entityToDto(selectedQuestion);
	}

	@Override
	public QuizResponseDto addQuestionToQuiz(QuestionRequestDto questionRequestDto, Long id) {

		Quiz selectedQuiz = getQuiz(id);

		Question questionToBeAdded = questionMapper.requestDtoToEntity(questionRequestDto);
		questionToBeAdded.setQuiz(selectedQuiz);
		questionRepository.saveAndFlush(questionToBeAdded);
		return quizMapper.entityToDto(selectedQuiz);

	}

	@Override
	@Transactional
	public QuestionResponseDto deleteQuestionFromQuiz(Long questionId, Long id) {

		Quiz selectedQuiz = getQuiz(id);
		// check first if question is in the specified quiz,if exist delete,else return
		// empty object
		List<Question> questions = selectedQuiz.getQuestions();
		Question questionToBeDeleted = new Question();
		for (Question eachQuestionInQuiz : questions) {
			List<Answer> answers = eachQuestionInQuiz.getAnswers();

			if (eachQuestionInQuiz.getId() == questionId) {
				for (Answer eachAnswer : answers) {
					answerRepository.delete(eachAnswer);
				}
				questionToBeDeleted = eachQuestionInQuiz;
				questionRepository.delete(eachQuestionInQuiz);
				break;
			}
		}

		return questionMapper.entityToDto(questionToBeDeleted);
	}

}
