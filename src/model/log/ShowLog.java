package model.log;

import log.Log;

public class ShowLog implements Log {
    @Override
    public void writeLog(String s, String s1) {
        System.out.println(s1);
    }
}
