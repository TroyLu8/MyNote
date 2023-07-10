import java.awt.event.*;
import java.awt.GridLayout;

import javax.swing.*;

public class DataNew extends DataManager implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {

        UIPanel message = new UIPanel(new GridLayout(2, 1));
        message.add(new UILabel("Name your new note: "));
        JTextField fileNamer = new JTextField();
        message.add(fileNamer);

        if (Main.showPrompt(message, "Create") == 0) {
            String fileName = fileNamer.getText().trim();

            if (validFileName(fileName)) {

                setFile("mynote_data/" + fileName + ".txt");

            } else {
                if (Main.showPrompt(new UILabel("cannot contain these characters:   \\ / : * ? \" < > |"), "retry") == 0)
                    actionPerformed(e);
            }
            
        }
        

    }

    private static boolean validFileName(String filename) {
        final char[] illegal = {'\\', '/', ':', '*', '?', '\"', '<', '>', '|'};
        
        for (int i = 0; i < filename.length(); i++) {
            for (int j = 0; j < illegal.length; j++) {
                if (filename.charAt(i) == illegal[j]) 
                    return false;
            }
        } 
        return true;
    }

}