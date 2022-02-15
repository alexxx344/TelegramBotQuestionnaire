package ru.voronec.botbot.botapi;

import ru.voronec.botbot.MyTelegramBot;
import ru.voronec.botbot.cache.UserDataCache;
import ru.voronec.botbot.model.UserProfileData;
import ru.voronec.botbot.service.MainMenuService;
import ru.voronec.botbot.service.ReplyMessagesService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


@Component
@Slf4j
public class TelegramFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private MainMenuService mainMenuService;
    private MyTelegramBot myWizardBot;
    private ReplyMessagesService messagesService;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache, MainMenuService mainMenuService,
                          @Lazy MyTelegramBot myWizardBot, ReplyMessagesService messagesService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
        this.myWizardBot = myWizardBot;
        this.messagesService = messagesService;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
        }


        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, userId: {}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    @SneakyThrows
    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        Long userId = message.getFrom().getId();
        long chatId = message.getChatId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMsg) {
            case "/start":
                botState = BotState.FILLING_PROFILE;
                break;
            case "Случайное слово":
                botState = BotState.RANDOM_WORD;
                break;
            case "Моя анкета":
                botState = BotState.SHOW_USER_PROFILE;
                break;
            case "Скачать анкету":
                myWizardBot.sendDocument(chatId, "Ваша анкета", getUsersProfile(userId));
                botState = BotState.SHOW_USER_PROFILE;
                break;
            case "Получить вопрос":

                botState = BotState.GET_QUESTION;
                break;
            case "Помощь":
                botState = BotState.SHOW_HELP_MENU;
                break;

            case "Режим ввода новых слов":
                botState = BotState.ASK_WORD;
                break;
            case "Все":
                botState = BotState.SHOW_ALL_PROFILES_MY_STATE;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        userDataCache.setUsersCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }


    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final long userId = buttonQuery.getFrom().getId();
        BotApiMethod<?> callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "Воспользуйтесь главным меню");


        if (buttonQuery.getData().equals("buttonStart")) {
            callBackAnswer = new SendMessage(Long.toString(chatId), "Введите слово");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_WORD_CONTINUE);
        } else if (buttonQuery.getData().equals("buttonGetQuestion")) {
            callBackAnswer = new SendMessage(Long.toString(chatId), "Режим получения вопросов");
            userDataCache.setUsersCurrentBotState(userId, BotState.GET_QUESTION_CONTINUE);
        } else if (buttonQuery.getData().equals("buttonEnd")) {
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        }

        if (userDataCache.getUsersCurrentBotState(userId) == BotState.GET_QUESTION_CONTINUE) {
//            callBackAnswer = new SendMessage(Long.toString(chatId), chooseTheVariant(buttonQuery));
            callBackAnswer = botStateContext.processInputMessage(BotState.GET_QUESTION_CONTINUE, buttonQuery);

        }


        return callBackAnswer;


    }


    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }


    @SneakyThrows
    public File getUsersProfile(Long userId) {
        UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
        File profileFile = ResourceUtils.getFile("classpath:static/docs/users_profile.txt");

        try (FileWriter fw = new FileWriter(profileFile.getAbsoluteFile());
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(userProfileData.toString());
        }


        return profileFile;

    }

    public String chooseTheVariant(CallbackQuery buttonQuery) {
        String answer = null;
        switch (buttonQuery.getData()) {
            case "buttonNumber1":
                answer = "1";
                break;
            case "buttonNumber2":
                answer = "2";
                break;
            case "buttonNumber3":
                answer = "3";
                break;
            case "buttonNumber4":
                answer = "4";
                break;
            default:
                answer = "";
                break;
        }
        return answer;
    }

}
