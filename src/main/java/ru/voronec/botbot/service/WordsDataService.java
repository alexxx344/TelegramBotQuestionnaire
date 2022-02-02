package ru.voronec.botbot.service;

import org.springframework.stereotype.Service;
import ru.voronec.botbot.model.MyWord;
import ru.voronec.botbot.repository.WordsMongoRepository;

@Service
public class WordsDataService {
    private WordsMongoRepository dataService;

    public WordsDataService(WordsMongoRepository dataService) {
        this.dataService = dataService;
    }

    public void saveWordInBase(String myWord, String creatorId){
        dataService.save(MyWord.builder().creator(creatorId).word(myWord).build());
    }



}
