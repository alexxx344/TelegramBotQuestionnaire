package ru.voronec.botbot.botapi.handlers.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.voronec.botbot.botapi.BotState;
import ru.voronec.botbot.botapi.InputMessageHandler;
import ru.voronec.botbot.cache.UserDataCache;
import ru.voronec.botbot.service.ReplyMessagesService;
import ru.voronec.botbot.service.WordsDataService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AskWordHandler implements InputMessageHandler {
    private ReplyMessagesService messagesService;
    private UserDataCache userDataCache;
    private WordsDataService dataService;

    public AskWordHandler(ReplyMessagesService messagesService, UserDataCache userDataCache, WordsDataService dataService) {
        this.messagesService = messagesService;
        this.userDataCache = userDataCache;
        this.dataService = dataService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public SendMessage handle(CallbackQuery message) {
        return null;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_WORD;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();
        if (userDataCache.getUsersCurrentBotState(inputMsg.getFrom().getId()).equals(BotState.ASK_WORD_CONTINUE)) {
            SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.askNewWordNext");
            dataService.saveWordInBase(inputMsg.getText(), Long.toString(inputMsg.getChatId()));

            return replyToUser;
        }
        userDataCache.setUsersCurrentBotState(inputMsg.getFrom().getId(),BotState.ASK_WORD_CONTINUE);
        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.askNewWord");
        replyToUser.setReplyMarkup(getInlineMessageButtons());

        return replyToUser;
    }


    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();


        InlineKeyboardButton buttonStart = new InlineKeyboardButton();
        InlineKeyboardButton buttonEnd = new InlineKeyboardButton();

        buttonStart.setText("Начать");
        buttonEnd.setText("Закончить");
        buttonStart.setCallbackData("buttonStart");
        buttonEnd.setCallbackData("buttonEnd");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonStart);
        keyboardButtonsRow1.add(buttonEnd);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

}
