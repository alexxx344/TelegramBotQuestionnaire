package ru.voronec.botbot.botapi.handlers.menu;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.voronec.botbot.botapi.BotState;
import ru.voronec.botbot.botapi.InputMessageHandler;
import ru.voronec.botbot.cache.UserDataCache;
import ru.voronec.botbot.model.UserProfileData;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
public class ShowProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;

    public ShowProfileHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        final long userId = message.getFrom().getId();
        final UserProfileData profileData = userDataCache.getUserProfileData(userId);

        userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        return new SendMessage(Long.toString(message.getChatId()),"Профиль: " +  profileData.getName());
    }

    @Override
    public SendMessage handle(CallbackQuery message) {
        return null;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_USER_PROFILE;
    }
}
