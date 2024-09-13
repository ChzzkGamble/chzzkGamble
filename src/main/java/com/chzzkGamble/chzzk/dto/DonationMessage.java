package com.chzzkGamble.chzzk.dto;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketMessage;
import java.util.Arrays;

@Getter
public class DonationMessage {
    String channelName;
    int cheese;
    String msg;
    DonationType type;

    public DonationMessage(String channelName, WebSocketMessage<?> message) {
        this.channelName = channelName;
        JsonArray bdy = JsonParser.parseString((String) message.getPayload())
                .getAsJsonObject()
                .getAsJsonArray("bdy");
        for (int i = 0; i < bdy.size(); i++) {
            JsonObject jsonObject = bdy.get(i).getAsJsonObject();
            if (jsonObject.get("msg") != null) {
                this.msg = jsonObject.get("msg").getAsString();
            }
            if (jsonObject.get("extras") != null) {
                String extras = jsonObject.get("extras").getAsJsonPrimitive().getAsString();
                this.cheese = parseCheese(extras);
                this.type = DonationType.from(parseType(extras));
            }
        }
    }

    private static int parseCheese(String extras) {
        for (String s : StringUtils.commaDelimitedListToSet(extras)) {
            if (s.contains("payAmount")) {
                return Integer.parseInt(s.split(":")[1]);
            }
        }
        return 0; //subscribe
    }

    private static String parseType(String extras) {
        for (String s : StringUtils.commaDelimitedListToSet(extras)) {
            if (s.contains("donationType")) {
                return s.split(":")[1];
            }
        }
        return "";
    }

    public boolean isDonation() {
        return cheese != 0;
    }

    @Override
    public String toString() {
        return "DonationMessage{" +
                "channelName='" + channelName + '\'' +
                ", cheese=" + cheese +
                ", msg='" + msg + '\'' +
                ", type=" + type +
                '}';
    }

    public enum DonationType {
        CHAT, VIDEO, OTHER;

        static DonationType from(String name) {
            return Arrays.stream(values())
                    .filter(value -> value.name().equals(name))
                    .findFirst()
                    .orElse(OTHER);
        }
    }
}