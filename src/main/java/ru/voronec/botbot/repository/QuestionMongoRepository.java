package ru.voronec.botbot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.voronec.botbot.model.Question;

@Repository
public interface QuestionMongoRepository extends MongoRepository<Question,String> {
    Question findByNumberOfQuestion(String number);
}
