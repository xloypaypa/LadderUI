package control.event.steep;

import control.event.AbstractSteep;
import control.event.tool.ValueChecker;
import control.listener.ListenerManager;
import javafx.util.Pair;
import log.LogManager;

/**
 * Created by xlo on 15-6-23.
 * it's the steep to make port in string to integer
 */
public class StringToIntegerSteep extends AbstractSteep {
    private int ans;
    private String name;

    public StringToIntegerSteep(String name) {
        this.name = name;
    }

    @Override
    protected boolean checkNeedData() {
        ValueChecker valueChecker = new ValueChecker();
        valueChecker.setEventCallBack(this.eventCallBack);
        valueChecker.addItem(new Pair<>(name, String.class));
        if (valueChecker.checkAllItem()) {
            return true;
        } else {
            ListenerManager.setErrorMessage("Data not found!");
            ListenerManager.UIAction();
            return false;
        }
    }

    @Override
    protected boolean steep() {
        try {
            String word = (String) this.eventCallBack.getValue(name);
            ans = Integer.valueOf(word);
            return true;
        } catch (Exception e) {
            ListenerManager.setErrorMessage("Data error!");
            ListenerManager.UIAction();
            return false;
        }
    }

    @Override
    protected void putData() {
        this.eventCallBack.putValue(name, ans);
    }
}
