package ru.voronec.botbot.botapi.handlers.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.voronec.botbot.botapi.BotState;
import ru.voronec.botbot.botapi.InputMessageHandler;
import ru.voronec.botbot.cache.UserDataCache;
import ru.voronec.botbot.model.Question;
import ru.voronec.botbot.service.QuestionDataService;
import ru.voronec.botbot.service.ReplyMessagesService;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class GetQuestionHandler implements InputMessageHandler {
    private ReplyMessagesService messagesService;
    private UserDataCache userDataCache;
    private QuestionDataService dataService;

    public GetQuestionHandler(ReplyMessagesService messagesService, UserDataCache userDataCache, QuestionDataService dataService) {
        this.messagesService = messagesService;
        this.userDataCache = userDataCache;
        this.dataService = dataService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.GET_QUESTION;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();
        long userId = inputMsg.getFrom().getId();
        if (userDataCache.getUsersCurrentBotState(inputMsg.getFrom().getId()).equals(BotState.GET_QUESTION_CONTINUE)) {

            String currentNumberOfQuestion = null;
            if (userDataCache.getCurrentNumberOfQuestion(userId) == null) {
                userDataCache.setDefaultCurrentNumberOfQuestion(userId);
                currentNumberOfQuestion = userDataCache.getCurrentNumberOfQuestion(userId);
            } else {
                currentNumberOfQuestion = userDataCache.getCurrentNumberOfQuestion(userId);
                Integer curNumbQuest = Integer.parseInt(userDataCache.getCurrentNumberOfQuestion(userId));
                userDataCache.setCurrentNumberOfQuestion(userId, String.valueOf(curNumbQuest + 1));
            }

            Question myQuestion = dataService.getQuestionByNumber(currentNumberOfQuestion);

            String chekAnswerButton = myQuestion.getRightAnswer();

            SendMessage replyToUser =
                    SendMessage.builder()
                            .chatId(String.valueOf(chatId))
                            .text(myQuestion.getQuestionText())
                            .replyMarkup(getVariantOfAnswersMessageButtons(
                                    myQuestion.getAnswer1()
                                    , myQuestion.getAnswer2()
                                    , myQuestion.getAnswer3()
                                    , myQuestion.getAnswer4()))
                            .build();

            return replyToUser;
        }

        userDataCache.setUsersCurrentBotState(inputMsg.getFrom().getId(), BotState.GET_QUESTION_CONTINUE);
        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.GetQuestionStart");
        replyToUser.setReplyMarkup(getInlineMessageButtons());

        return replyToUser;
    }

    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();


        InlineKeyboardButton buttonStart = new InlineKeyboardButton();
        InlineKeyboardButton buttonEnd = new InlineKeyboardButton();
        InlineKeyboardButton buttonReset = new InlineKeyboardButton();

        buttonStart.setText("Получить вопросы");
        buttonEnd.setText("Закончить");
        buttonReset.setText("Обнулить");
        buttonStart.setCallbackData("buttonGetQuestion");
        buttonEnd.setCallbackData("buttonEnd");
        buttonReset.setCallbackData("buttonReset");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonStart);
        keyboardButtonsRow1.add(buttonEnd);
        keyboardButtonsRow1.add(buttonReset);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup getVariantOfAnswersMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();


        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        InlineKeyboardButton button4 = new InlineKeyboardButton();

        button1.setText("Вариант 1");
        button1.setText("Вариант 2");
        button1.setText("Вариант 3");
        button1.setText("Вариант 4");


        button1.setCallbackData("buttonNumber1");
        button1.setCallbackData("buttonNumber2");
        button1.setCallbackData("buttonNumber3");
        button1.setCallbackData("buttonNumber4");


        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(button1);
        keyboardButtonsRow1.add(button2);
        keyboardButtonsRow1.add(button3);
        keyboardButtonsRow1.add(button4);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup getVariantOfAnswersMessageButtons(String answ1, String answ2, String answ3, String answ4) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();


        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        InlineKeyboardButton button4 = new InlineKeyboardButton();

        button1.setText(answ1);
        button2.setText(answ2);
        button3.setText(answ3);
        button4.setText(answ4);


        button1.setCallbackData("buttonNumber1");
        button2.setCallbackData("buttonNumber2");
        button3.setCallbackData("buttonNumber3");
        button4.setCallbackData("buttonNumber4");


        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(button1);
        keyboardButtonsRow1.add(button2);
        keyboardButtonsRow1.add(button3);
        keyboardButtonsRow1.add(button4);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

}
