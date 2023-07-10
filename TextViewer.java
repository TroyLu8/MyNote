
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class TextViewer extends JFrame{

    UIPanel lowest = new UIPanel();

    JTextField searchField = new JTextField(26);
    UIButton searchButton = new UIButton("Search");
    JCheckBox matchCase = new JCheckBox("Match Case?");

    UIButton openButton = new UIButton("Open");
    UIButton saveButton = new UIButton("Save");
    UIButton newButton = new UIButton("New");
    UIButton deleteButton = new UIButton("Delete");
    UIButton settingsButton = new UIButton("...");

    UITextArea textArea = new UITextArea();

    TextViewer(){
        
        setIconImage(new ImageIcon(getClass().getResource("note-icon.png")).getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addComponents();
        setLocationRelativeTo(null);
        
        searchButton.addActionListener(new Search(this));
        matchCase.setSelected(true);

        addWindowListener(new SaveBeforeClosing());

        addCtrlShortcut("O", new OpenAction());
        addCtrlShortcut("S", new SaveAction());
        addCtrlShortcut("N", new newAction());
        addCtrlShortcut("F", new SearchAction());

    }

    private void addComponents(){
        lowest.setPreferredSize(new Dimension((int)(1280 * 0.63), (int)(720 * 0.63)));
        lowest.setLayout(new BorderLayout());

        UIPanel header = new UIPanel(new BorderLayout());

        searchField.addActionListener(new SearchField());
        
        UIPanel topLeft = new UIPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        topLeft.add(searchField);
        topLeft.add(searchButton);
        topLeft.add(matchCase);
        header.add(topLeft, BorderLayout.WEST);

        UIPanel topRight = new UIPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        topRight.add(openButton);
        topRight.add(saveButton);
        topRight.add(newButton);
        topRight.add(deleteButton);
        topRight.add(settingsButton);
        header.add(topRight, BorderLayout.EAST);

        lowest.add(header, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5)); 
        
        lowest.add(scrollPane);

        add(lowest);
        
        pack();
    }

    void openTitleScreen() {
        DataManager.setFile("mynote_data/titlescreen.dat");
        DataManager.saveAndWriteData("\nWelcome to MyNote! \n\nTo start, create a new note with the \"new\" button. \n\nOr, open an existing note with the \"open\" button.");
        DataManager.setFile("mynote_data/titlescreen.dat");
        setEditingEnabled(false);
        setTitle("MyNote");
        setVisible(true);
    }

    void setEditingEnabled(boolean enabled) {
        textArea.setEnabled(enabled);
        searchField.setEnabled(enabled);
        searchButton.setEnabled(enabled);
        matchCase.setEnabled(enabled);
    }

    class SaveBeforeClosing extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e){
            
            DataManager.openSaveNowPopup();
            DataManager.writeMetadata(Settings.themeID, Settings.fontSize);
    
            System.exit(0);
        }
    }

    private void addCtrlShortcut(String key, Action action) {
        lowest.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl " + key), key + " map key");
        lowest.getActionMap().put(key + " map key", action);
    }

    class OpenAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            openButton.doClick(0);
        }
    }
    class SaveAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveButton.doClick(0);
        }
    }
    class newAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            newButton.doClick(0);
        }
    }
    class SearchAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            searchField.requestFocus();
        }
    }
    class SearchField extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            searchButton.doClick(0);
        }
    }
    class DeleteAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            deleteButton.doClick(0);
        }
    }


}
