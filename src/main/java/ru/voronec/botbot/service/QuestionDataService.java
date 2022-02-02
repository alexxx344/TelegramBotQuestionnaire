package ru.voronec.botbot.service;

import org.springframework.stereotype.Service;
import ru.voronec.botbot.model.Question;
import ru.voronec.botbot.repository.QuestionMongoRepository;

import javax.annotation.PostConstruct;

@Service
public class QuestionDataService {
    private QuestionMongoRepository dataService;

    public QuestionDataService(QuestionMongoRepository dataService) {
        this.dataService = dataService;
    }

    public Question getQuestionByNumber(String number){
        Question question= dataService.findByNumberOfQuestion(number);

        return question;
    }



}
