package ru.voronec.botbot.botapi.handlers.fillingprofile;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.voronec.botbot.botapi.BotState;
import ru.voronec.botbot.botapi.InputMessageHandler;
import ru.voronec.botbot.cache.UserDataCache;
import ru.voronec.botbot.model.UserProfileData;
import ru.voronec.botbot.service.ReplyMessagesService;
import ru.voronec.botbot.service.UsersProfileDataService;
import ru.voronec.botbot.utils.Emojis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;




@Slf4j
@Component
public class FillingProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private UsersProfileDataService profileDataService;

    public FillingProfileHandler(UserDataCache userDataCache, ReplyMessagesService messagesService,
                                  UsersProfileDataService profileDataService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.profileDataService = profileDataService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_NAME);
        }
        return processUsersInput(message);
    }

    @Override
    public SendMessage handle(CallbackQuery message) {
        return null;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        long userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_NAME)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askName");
            userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
        }


        if (botState.equals(BotState.PROFILE_FILLED)) {
            profileData.setName(usersAnswer);
            profileData.setChatId(chatId);
            if (profileDataService.getUserProfileData(userId)==null){
                profileDataService.saveUserProfileData(profileData);
            }else {
                log.info("User:{} id: {} is already exist", profileData.getName(),profileData.getChatId());
            }

            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);

            String profileFilledMessage = messagesService.getReplyText("reply.profileFilled",
                    profileData.getName(), Emojis.SPARKLES);


            replyToUser = new SendMessage(Long.toString(chatId), String.format("%s%n%n%s ", profileFilledMessage, Emojis.SCROLL));

            replyToUser.setParseMode("HTML");
        }


        userDataCache.saveUserProfileData(userId, profileData);

        return replyToUser;
    }


}



