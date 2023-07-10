import java.awt.GridLayout;
import java.awt.event.*;

public class DataOpener extends DataManager implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {

        openSaveNowPopup();

        String[] allFiles = file.getParentFile().list();

        String[] editable = new String[allFiles.length-2];
        int j = 0;
        for (int i = 0; i < allFiles.length; i++) {
            if (allFiles[i].substring(allFiles[i].length()-4).equals(".txt"))
                editable[j++] = removeSuffix(allFiles[i]);
        }
            
        UIPanel message = new UIPanel(new GridLayout(2, 1));
        message.add(new UILabel("Select a note to open: "));
        
        UIComboBox<String> fileSelect = new UIComboBox<>(editable);
        fileSelect.addActionListener(new FileSelect());
        fileSelect.setSelectedItem(removeSuffix(file.getName()));
        message.add(fileSelect);


        if (editable.length != 0) setFile("mynote_data/" + fileSelect.getSelectedItem() + ".txt");
        Main.showPrompt(message, "Done");
        

    }

    

    class FileSelect implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            UIComboBox<String> fileSelect = (UIComboBox<String>) e.getSource();

            setFile("mynote_data/" + fileSelect.getSelectedItem() + ".txt");

        }

    }

    

}