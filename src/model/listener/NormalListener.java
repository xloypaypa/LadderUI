package model.listener;

import control.listener.LogicMessageListener;

import javax.swing.*;

/**
 * Created by xlo on 15-6-23.
 * it's the normal listener
 */
public class NormalListener extends LogicMessageListener {
    @Override
    public void logAction() {
        if (this.errorFlag){
            JOptionPane.showMessageDialog(null, this.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null,this.getMessage(), "message",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void UIAction() {
        if (this.errorFlag){
            JOptionPane.showMessageDialog(null, this.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null,this.getMessage(), "message",JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
