package ru.voronec.botbot.cache;

import ru.voronec.botbot.botapi.BotState;
import ru.voronec.botbot.model.UserProfileData;


public interface DataCache {
    void setUsersCurrentBotState(Long userId, BotState botState);

    BotState getUsersCurrentBotState(Long userId);

    UserProfileData getUserProfileData(Long userId);

    void saveUserProfileData(Long userId, UserProfileData userProfileData);

}
