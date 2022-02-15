package ru.voronec.botbot.botapi;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;


public interface InputMessageHandler {
    SendMessage handle(Message message);
    SendMessage handle(CallbackQuery message);


    BotState getHandlerName();
}
