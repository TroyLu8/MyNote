import java.awt.*;
import java.awt.event.*;

public class DataDeleter extends DataManager implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {

        UIPanel message = new UIPanel(new GridLayout(2,1));
        message.add(new UILabel("deleting: " + removeSuffix(file.getName())));
        message.add(new UILabel("are you sure?"));

        if (Main.showPrompt(message, "yes", "no") == 0) {
            file.delete();

            textViewer.openTitleScreen();
        }
    }
    
}
