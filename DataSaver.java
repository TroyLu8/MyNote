import java.awt.event.*;

public class DataSaver extends DataManager implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e) {
        saveAndWriteData(textArea.getText());
    }

}