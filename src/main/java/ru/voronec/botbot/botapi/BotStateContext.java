package ru.voronec.botbot.botapi;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class BotStateContext {
    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    //Получить все обработчик
    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    //Обработать полученным обработчиком
    public SendMessage processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }

    //Обработать полученным обработчиком
    public SendMessage processInputMessage(BotState currentState, CallbackQuery message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }

    //найти обработчик по состоянию
    private InputMessageHandler findMessageHandler(BotState currentState) {
        if (isFillingProfileState(currentState)) {
            return messageHandlers.get(BotState.FILLING_PROFILE);
        } else if (isAskWordState(currentState)) {
            return messageHandlers.get(BotState.ASK_WORD);
        }else if (isQuestionState(currentState)) {
            return messageHandlers.get(BotState.GET_QUESTION);
        }

        return messageHandlers.get(currentState);

    }




    //проверка на обработчики заполнения
    private boolean isFillingProfileState(BotState currentState) {
        switch (currentState) {
            case ASK_NAME:
            case FILLING_PROFILE:
            case PROFILE_FILLED:
            case SHOW_ALL_PROFILES_MY_STATE:
            case ASK_START:
                return true;
            default:
                return false;
        }
    }

    //проверка на обработчики поиска слов
    private boolean isAskWordState(BotState currentState) {
        switch (currentState) {
            case ASK_WORD:
            case ASK_WORD_CONTINUE:
                return true;
            default:
                return false;
        }
    }

    //проверка на обработчики поиска слов
    private boolean isQuestionState(BotState currentState) {
        switch (currentState) {
            case GET_QUESTION:
            case CREATE_QUESTION:
            case GET_QUESTION_CONTINUE:
                return true;
            default:
                return false;
        }
    }


}





