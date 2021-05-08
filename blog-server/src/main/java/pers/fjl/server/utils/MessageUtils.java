package pers.fjl.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pers.fjl.server.chatroom.ResultMessage;

public class MessageUtils {

    public static String getMessage(boolean isSystemMessage, String fromName, Object message) {
        try {
            ResultMessage result = new ResultMessage();
            result.setSystem(isSystemMessage).setMessage(message);
            if (fromName != null) {
                result.setFromName(fromName);
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
