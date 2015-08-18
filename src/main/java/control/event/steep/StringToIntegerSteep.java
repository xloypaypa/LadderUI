package control.event.steep;

import control.action.DirectRunAction;

/**
 * Created by xlo on 15-6-23.
 * it's the steep to make port in string to integer
 */
public abstract class StringToIntegerSteep extends DirectRunAction {
    protected String[] value;
    protected int[] result;

    public StringToIntegerSteep(String... value) {
        this.value = value;
    }

    @Override
    public Boolean call() throws Exception {
        this.result = new int[this.value.length];
        for (int i = 0; i < this.value.length; i++) {
            this.result[i] = Integer.valueOf(this.value[i]);
        }
        return true;
    }
}
