package ru.voronec.botbot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.voronec.botbot.model.MyWord;

@Repository
public interface WordsMongoRepository extends MongoRepository<MyWord, String> {



}
