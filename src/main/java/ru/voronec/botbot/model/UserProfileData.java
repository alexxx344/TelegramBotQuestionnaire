package ru.voronec.botbot.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.voronec.botbot.botapi.BotState;

import java.io.Serializable;



@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "userProfileData")
@Builder
public class UserProfileData implements Serializable {
    @Id
    String id;
    String name;
    long chatId;
    String currentNumberOfQuestion;
    BotState botState;


    @Override
    public String toString() {
        return  getName() + " " + getBotState();
    }
}