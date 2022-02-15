package ru.voronec.botbot.botapi.handlers.menu;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.voronec.botbot.botapi.BotState;
import ru.voronec.botbot.botapi.InputMessageHandler;
import ru.voronec.botbot.cache.UserDataCache;
import ru.voronec.botbot.model.UserProfileData;
import ru.voronec.botbot.service.UsersProfileDataService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

@Component
public class ShowAllProfilesHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private UsersProfileDataService profileDataService;


    public ShowAllProfilesHandler(UserDataCache userDataCache, UsersProfileDataService profileDataService) {
        this.userDataCache = userDataCache;
        this.profileDataService = profileDataService;
    }

    @Override
    public SendMessage handle(Message message) {
        final long userId = message.getFrom().getId();
        final UserProfileData profileData = userDataCache.getUserProfileData(userId);
        File f = getAllUsersProfile();
        userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);

        return new SendMessage(Long.toString(message.getChatId()), "Готово");
    }

    @Override
    public SendMessage handle(CallbackQuery message) {
        return null;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_USER_PROFILE;
    }

    @SneakyThrows
    public File getAllUsersProfile() {
        List<UserProfileData> listAllProfiles = profileDataService.getAllProfiles();
        File profileFile = ResourceUtils.getFile("classpath:static/docs/users_profile.txt");

        try (FileWriter fw = new FileWriter(profileFile.getAbsoluteFile());
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(listAllProfiles.toString());
        }


        return profileFile;

    }
}
