package view;

import java.awt.event.KeyEvent;

import javax.swing.JTextField;

public class JNumberTextField extends JTextField {
    private static final long serialVersionUID = 1L;

    @Override
    public void processKeyEvent(KeyEvent ev) {
        if (Character.isDigit(ev.getKeyChar()) || ev.getKeyChar()==KeyEvent.VK_BACK_SPACE) {
            super.processKeyEvent(ev);
        }
        ev.consume();
        return;
    }

    public Long getNumber() {
        Long result = null;
        String text = getText();
        if (text != null && !"".equals(text)) {
            result = Long.valueOf(text);
        }
        return result;
    }
}