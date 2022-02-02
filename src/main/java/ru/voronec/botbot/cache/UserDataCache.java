package ru.voronec.botbot.cache;

import ru.voronec.botbot.botapi.BotState;
import ru.voronec.botbot.model.UserProfileData;
import org.springframework.stereotype.Component;
import ru.voronec.botbot.service.UsersProfileDataService;

import java.util.HashMap;
import java.util.Map;



@Component
public class UserDataCache implements DataCache {
    private UsersProfileDataService usersProfileDataService;

    private Map<Long, BotState> usersBotStates = new HashMap<>();
    private Map<Long, UserProfileData> usersProfileData = new HashMap<>();
    private UserProfileData userProfileData;

    public UserDataCache(UsersProfileDataService usersProfileDataService) {
        this.usersProfileDataService = usersProfileDataService;
    }

    @Override
    public void setUsersCurrentBotState(Long userId, BotState botState) {
        usersBotStates.put(userId, botState);
        userProfileData = usersProfileDataService.getUserProfileData(userId);
        if (userProfileData == null) {
        } else {
            userProfileData.setBotState(botState);
            usersProfileDataService.saveUserProfileData(userProfileData);
        }
    }

    @Override
    public BotState getUsersCurrentBotState(Long userId) {
        BotState botStateSrv = null;
        userProfileData = usersProfileDataService.getUserProfileData(userId);
        if (userProfileData == null) {
            BotState botState = usersBotStates.get(userId);
        } else {
            if (usersProfileDataService.getUserProfileData(userId).getBotState() == null) {
                UserProfileData userProfileDataToUpdate = usersProfileDataService.getUserProfileData(userId);
                userProfileDataToUpdate.setBotState(BotState.ASK_START);
                usersProfileDataService.saveUserProfileData(userProfileDataToUpdate);

            } else {
                 botStateSrv = usersProfileDataService.getUserProfileData(userId).getBotState();
            }
        }


        if(botStateSrv==null){
            BotState botState = usersBotStates.get(userId);
            if (botState == null) {
                botState = BotState.ASK_START;
            }
         botStateSrv = botState;
        }else {
            return botStateSrv;
        }

        return botStateSrv;
    }

    @Override
    public UserProfileData getUserProfileData(Long userId) {
        UserProfileData userProfileDataSrv = usersProfileDataService.getUserProfileData(userId);
        if (userProfileDataSrv == null) {
            UserProfileData newUserProfileData = UserProfileData.builder().build();
        } else {
            return userProfileDataSrv;
        }
    return userProfileDataSrv;
    }

    @Override
    public void saveUserProfileData(Long userId, UserProfileData userProfileData) {
        usersProfileData.put(userId, userProfileData);
    }

    public void saveUserInBase(UserProfileData userProfileData){

        usersProfileDataService.saveUserProfileData(userProfileData);
    }

    public String getCurrentNumberOfQuestion(Long userId){
        String numbOfQuest = getUserProfileData(userId).getCurrentNumberOfQuestion();
        return numbOfQuest;
    }

    public void setDefaultCurrentNumberOfQuestion(Long userId){
        UserProfileData userProfileDataToUpdate = usersProfileDataService.getUserProfileData(userId);
        userProfileDataToUpdate.setCurrentNumberOfQuestion("1");
        usersProfileDataService.saveUserProfileData(userProfileDataToUpdate);
    }

    public void setCurrentNumberOfQuestion(Long userId,String newNumberOfQuestion){
         getUserProfileData(userId).setCurrentNumberOfQuestion("newNumberOfQuestion");
    }
}
