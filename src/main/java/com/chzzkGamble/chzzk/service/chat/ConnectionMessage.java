package com.chzzkGamble.chzzk.service.chat;

import com.chzzkGamble.chzzk.ChzzkChatCommand;
import lombok.Getter;

@Getter
public class ConnectionMessage {
    Bdy bdy;
    String cid;
    Integer cmd = ChzzkChatCommand.CONNECT.getNum();
    String svcid = "game";
    Integer tid = 1;
    String ver = "3";

    public ConnectionMessage(String accTkn, String cid) {
        this.bdy = new Bdy(accTkn);
        this.cid = cid;
    }

    @Getter
    static class Bdy {
        String accTkn;
        String auth = "READ";
        Integer devType = 2001;
        String uid = null;

        public Bdy(String accTkn) {
            this.accTkn = accTkn;
        }
    }
}
