import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.*;
import javax.swing.border.LineBorder;

class Settings extends JFrame implements ActionListener{

    static int themeID;
    static int fontSize;

    private static TextViewer textViewer;
    private static UIComboBox<Integer> sizeSelect;
    private static UIComboBox<String> themeSelect;

    private static UIPanel options;

    Settings(TextViewer textViewer) {
        Settings.textViewer = textViewer;

        setIconImage(new ImageIcon(getClass().getResource("note-icon.png")).getImage());
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {}

            @Override
            public void windowLostFocus(WindowEvent e) {
                setVisible(false);
            }
        });
        
        addComponents();
        setLocationRelativeTo(null);

        apply(themeID, fontSize);

    }

    private void addComponents() {
        UIPanel lowest = new UIPanel();
        lowest.setPreferredSize(new Dimension(300, 250));

        UILabel header = new UILabel("Settings", SwingConstants.CENTER);
        header.setFont(new Font(header.getFont().getName(), Font.PLAIN, 30));
        lowest.add(header, BorderLayout.NORTH);

        options = new UIPanel();
        options.setBorder(new LineBorder(ThemeSelect.PRIMARY_COLOR, 15));
        UIPanel grid = new UIPanel(new GridLayout(3, 2, 20, 20));
        
        grid.add(new UILabel("Theme", SwingConstants.RIGHT));

        themeSelect = new UIComboBox<>(new String[] {"Sky", "Matcha", "Cocoa"});
        themeSelect.addActionListener(new ThemeSelect(this));
        themeSelect.setSelectedIndex(themeID);

        grid.add(themeSelect);

        grid.add(new UILabel("Font Size", SwingConstants.RIGHT));

        sizeSelect = new UIComboBox<>(new Integer[] {8, 11, 12, 14, 18, 24});
        sizeSelect.setEditable(true);
        sizeSelect.addActionListener(new FontSelect());
        grid.add(sizeSelect);
        
        options.add(grid);

        UIPanel passPanel = new UIPanel(new FlowLayout(FlowLayout.CENTER));
        UIButton passBtn = new UIButton("Change Password");
        passBtn.addActionListener(new PasswordChange());
        passPanel.add(passBtn);

        options.add(passPanel, BorderLayout.SOUTH);

        lowest.add(options);

        UIPanel confirm = new UIPanel(new FlowLayout(FlowLayout.CENTER));
        UIButton done = new UIButton("Done");
        done.addActionListener(new CloseSettings(this));
        confirm.add(done);

        lowest.add(confirm, BorderLayout.SOUTH);

        add(lowest);
        pack();
        
    }

    static void apply(int themeID, int fontSize) {
        ThemeSelect.setTheme(themeID);
        FontSelect.setFontSize(fontSize);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setVisible(true);
        themeSelect.setSelectedIndex(themeID);
        sizeSelect.setSelectedItem(fontSize);
    } 

    @Override
    public void repaint() {
        options.setBorder(new LineBorder(ThemeSelect.PRIMARY_COLOR, 15));
        super.repaint();
    }

    static class PasswordChange implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            UIPanel message = new UIPanel();

            UILabel welcome = new UILabel("Password Change", SwingConstants.CENTER);
            message.add(welcome, BorderLayout.NORTH);

            UIPanel passwordFields = new UIPanel(new GridLayout(6, 1));
            JPasswordField old = new JPasswordField();
            JPasswordField pwf1 = new JPasswordField();
            JPasswordField pwf2 = new JPasswordField();
            
            passwordFields.add(new UILabel("Enter old password: "));
            passwordFields.add(old);
            passwordFields.add(new UILabel("Set new password: "));
            passwordFields.add(pwf1);
            passwordFields.add(new UILabel("Re-enter new password: "));
            passwordFields.add(pwf2);

            message.add(passwordFields);

            if (Main.showPrompt(message, "Done", "Cancel") != 0) return;

            if (!Main.comparePassword(new String(old.getPassword()))) {
                if (Main.showPrompt(new UILabel("Old password is incorrect"), "Retry") == 0) {
                    actionPerformed(e);
                    return;
                }
            }

            int result = Main.setPassword(String.valueOf(pwf1.getPassword()), String.valueOf(pwf2.getPassword()));
            if (result == Main.PASSWORD_SET_SUCCESSFULLY) {
                Main.showPrompt(new UILabel("Setup Success!"), "Ok");
                
                textViewer.openTitleScreen();
            } else if (result == Main.PASSWORDS_DONT_MATCH) {
                if (Main.showPrompt(new UILabel("New passwords don't match"), "Retry") == 0) {
                    actionPerformed(e);
                    return;
                }
            } else {
                if (Main.showPrompt(new UILabel("New password must include uppercase, lowercase, and a number, and have at least 4 characters"), "Retry") == 0) {
                    actionPerformed(e);
                    return;
                }
            }
        }

    }

    static class ThemeSelect implements ActionListener {
        static Settings settings;

        static Color PRIMARY_COLOR;
        static Color SECONDARY_COLOR; 
        static Color TERTIARY_COLOR;

        private static Color[][] themes = {
            {new Color(122, 138, 153), Color.WHITE, new Color(184, 210, 234)},
            {new Color(134, 148, 94), Color.WHITE, new Color(205, 208, 192)},
            {new Color(159, 127, 104), Color.WHITE, new Color(195, 175, 161)}
        };

        public ThemeSelect(Settings settings) {
            ThemeSelect.settings = settings;
        }

        static void setTheme(int themeID) {
            Settings.themeID = themeID;
            
            PRIMARY_COLOR = themes[themeID][0];
            SECONDARY_COLOR = themes[themeID][1];
            TERTIARY_COLOR = themes[themeID][2];

            textViewer.repaint();
            textViewer.textArea.repaint();
            settings.repaint();

            UIManager.put("OptionPane.background", PRIMARY_COLOR);
            UIManager.put("Panel.background", PRIMARY_COLOR);
            UIManager.put("Button.foreground", PRIMARY_COLOR);
            UIManager.put("Button.background", SECONDARY_COLOR);
            UIManager.put("ComboBox.selectionBackground", TERTIARY_COLOR);


            textViewer.textArea.setSelectionColor(Settings.ThemeSelect.PRIMARY_COLOR);
            textViewer.textArea.setSelectedTextColor(Settings.ThemeSelect.SECONDARY_COLOR);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            setTheme(themeSelect.getSelectedIndex());
        }

    }

    static class FontSelect implements ActionListener {
        static void setFontSize(int size) {
            Settings.fontSize = size;
            textViewer.textArea.setFont(new Font(textViewer.textArea.getFont().getName(), Font.PLAIN, size));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            setFontSize((Integer) sizeSelect.getSelectedItem());
        }
    }

    static class CloseSettings implements ActionListener {

        Settings settings;

        CloseSettings(Settings settings) {
            this.settings = settings;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            settings.setVisible(false);
        }
    }

}

