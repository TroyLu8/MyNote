import java.awt.*;
import javax.swing.*;

public class UIButton extends JButton{

    public UIButton(String text) {
        setText(text);
        
        for (Component c : getComponents())
            c.setFocusable(false);

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {

        if (getModel().isPressed()) {
            g.setColor(Settings.ThemeSelect.TERTIARY_COLOR);
            setForeground(Settings.ThemeSelect.SECONDARY_COLOR);
        } else {
            g.setColor(Settings.ThemeSelect.SECONDARY_COLOR);
            setForeground(Settings.ThemeSelect.PRIMARY_COLOR);
        }

        g.fillRect(0, 0, getWidth(), getHeight());

        super.paintComponent(g);
    }
}

class UIPanel extends JPanel {
    public UIPanel(LayoutManager lm) {
        super(lm);

        setBackground(Settings.ThemeSelect.PRIMARY_COLOR);
        setForeground(Settings.ThemeSelect.SECONDARY_COLOR);
    }
    public UIPanel() {
        this(new BorderLayout());
    }
    @Override
    public void paintComponent(Graphics g) {
        setBackground(Settings.ThemeSelect.PRIMARY_COLOR);
        setForeground(Settings.ThemeSelect.SECONDARY_COLOR);

        super.paintComponent(g);
    }
}
class UILabel extends JLabel {
    public UILabel(String txt, int alignment) {
        super(txt, alignment);
    }
    public UILabel(String txt) {
        this(txt, SwingConstants.LEFT);
    }
    @Override
    public void paintComponent(Graphics g) {
        setForeground(Settings.ThemeSelect.SECONDARY_COLOR);

        super.paintComponent(g);
    }
}
class UITextArea extends JTextArea {

    public UITextArea() {
        setLineWrap(true);
        setWrapStyleWord(true);
    }
    @Override
    public void paintComponent(Graphics g) {
        setDisabledTextColor(Settings.ThemeSelect.SECONDARY_COLOR);
        if (isEnabled())    setBackground(Settings.ThemeSelect.TERTIARY_COLOR);
        else                setBackground(Settings.ThemeSelect.PRIMARY_COLOR);

        super.paintComponent(g);
    }
    @Override
    public void setEnabled(boolean enabled) {

        super.setEnabled(enabled);

        repaint();
    }
}

class UIComboBox<T> extends JComboBox<T> {
    public UIComboBox(T[] list) {
        super(list);
    }
    @Override
    public void paintComponent(Graphics g) {
        setForeground(Settings.ThemeSelect.PRIMARY_COLOR);
        setBackground(Settings.ThemeSelect.SECONDARY_COLOR);
        
        super.paintComponent(g);
    }
}