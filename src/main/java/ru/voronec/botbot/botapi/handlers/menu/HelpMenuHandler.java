package ru.voronec.botbot.botapi.handlers.menu;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.voronec.botbot.botapi.BotState;
import ru.voronec.botbot.botapi.InputMessageHandler;
import ru.voronec.botbot.service.MainMenuService;
import ru.voronec.botbot.service.ReplyMessagesService;
import ru.voronec.botbot.utils.Emojis;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
public class HelpMenuHandler implements InputMessageHandler {
    private MainMenuService mainMenuService;
    private ReplyMessagesService messagesService;

    public HelpMenuHandler(MainMenuService mainMenuService, ReplyMessagesService messagesService) {
        this.mainMenuService = mainMenuService;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        return mainMenuService.getMainMenuMessage(message.getChatId(),
                messagesService.getReplyText("reply.showHelpMenu", Emojis.MAGE));
    }

    @Override
    public SendMessage handle(CallbackQuery message) {
        return null;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_HELP_MENU;
    }
}
