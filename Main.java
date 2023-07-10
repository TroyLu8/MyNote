import java.io.*;
import java.awt.*;
import javax.swing.*;

public class Main{ 
    private static TextViewer textViewer;

    static byte[] passwordHash = new byte[32];
    
    public static void main(String[] args) throws IOException {

        (new File("mynote_data")).mkdir();          // ensure "mynote_data" directory exists

        boolean hasAccount = DataManager.fetchMetadata();

        textViewer = new TextViewer();
        DataManager.init(textViewer);
        
        if (hasAccount)    passwordPrompt();
        else               passwordSetup();

    }

    static int PASSWORD_SET_SUCCESSFULLY = 0;
    static int PASSWORDS_DONT_MATCH = 1;
    static int PASSWORD_TOO_WEAK = 2;

    static int setPassword(String pass, String copy) {
        if (String.valueOf(pass).equals(String.valueOf(copy))) {

            if (!judgePassword(pass)) return PASSWORD_TOO_WEAK;

            passwordHash = DataManager.getHash(pass);

            DataManager.writeMetadata(Settings.themeID, Settings.fontSize);
            
            return PASSWORD_SET_SUCCESSFULLY;
        }
        return PASSWORDS_DONT_MATCH;
    } 

    private static boolean judgePassword(String pass) {
        if (pass.length() < 4) return false;

        boolean upper = false;
        boolean lower = false;
        boolean number = false;

        int p = 0;

        while (!upper || !lower || !number ) {
            if (p == pass.length()) return false;

            char ch = pass.charAt(p++);

            if ('0' <= ch && ch <= '9')         number = true;
            else if ('a' <= ch && ch <= 'z')    lower = true;
            else if ('A' <= ch && ch <= 'Z')    upper = true;
            
        }

        return true;
    }

    private static void passwordSetup() {
        
        UIPanel message = new UIPanel();

        UILabel welcome = new UILabel("Welcome to MyNote", SwingConstants.CENTER);
        message.add(welcome, BorderLayout.NORTH);

        UIPanel passwordFields = new UIPanel(new GridLayout(5, 1));
        JPasswordField pwf1 = new JPasswordField("");
        JPasswordField pwf2 = new JPasswordField("");
        
        passwordFields.add(new UILabel("Create a password: "));
        passwordFields.add(pwf1);
        passwordFields.add(new UILabel("Re-enter password: "));
        passwordFields.add(pwf2);

        message.add(passwordFields);
    
        if (showPrompt(message, "Done") != 0) System.exit(0);

        int result = setPassword(String.valueOf(pwf1.getPassword()), String.valueOf(pwf2.getPassword()));
        if (result == PASSWORD_SET_SUCCESSFULLY) {
            showPrompt(new UILabel("Setup Success!"), "Ok");
            AESCipher.init(pwf1.getPassword());
            textViewer.openTitleScreen();
        } else if (result == PASSWORDS_DONT_MATCH) {
            if (showPrompt(new UILabel("Passwords don't match"), "Retry") == 0) {
                passwordSetup();
                return;
            }
        } else {
            if (showPrompt(new UILabel("Password must include uppercase, lowercase, and a number, and have at least 4 characters"), "Retry") == 0) {
                passwordSetup();
                return;
            }
        }

    }

    private static void passwordPrompt() {
        JPasswordField pwf = new JPasswordField();

        UIPanel message = new UIPanel(new GridLayout(2, 1));
        message.add(new UILabel("Enter password: "));
        message.add(pwf);
        
        if (Main.showPrompt(message, "Log In") != 0) 
            System.exit(0); 
        
        
        if (comparePassword(new String(pwf.getPassword()))) {
            AESCipher.init(pwf.getPassword());
            textViewer.openTitleScreen();
        }         
            
    
        else {
            if (Main.showPrompt(new UILabel("Password is incorrect"), "Retry") == 0)
                passwordPrompt();

        }
         
    }

    static boolean comparePassword(String input) {
        byte[] other = DataManager.getHash(input);
        for (int i = 0; i < other.length; i++) {
            if (other[i] != passwordHash[i])
                return false;
        }
        return true;
    }

    public static int showPrompt(Object message, String... buttons) {
        return  JOptionPane.showOptionDialog(null, message, "MyNote", 
                JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE, 
                null, buttons, null);
    }

}