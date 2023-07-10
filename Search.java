import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class Search implements ActionListener{
    JTextArea textArea;
    JTextField searchField;
    JCheckBox matchCase;

    ArrayList<Integer> allIndexes;
    String body;
    String keyword;
    int currentIndex;

    public Search(TextViewer textViewer){
        textArea = textViewer.textArea;
        searchField = textViewer.searchField;
        matchCase = textViewer.matchCase;

        body = "";
        keyword = "";
        currentIndex = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String newBody = textArea.getText();
        String newKeyword = searchField.getText();

        if (newKeyword.length() == 0) return;

        // match case
        if (!matchCase.isSelected()) {
            newBody = newBody.toLowerCase();
            newKeyword = newKeyword.toLowerCase();
        }

        // if either body or keyword has changed, generate allIndexes again and start highlighting from beginning
        if (!body.equals(newBody) || !keyword.equals(newKeyword)){
            
            body = newBody;
            keyword = newKeyword;

            allIndexes = getAllIndexes(body, keyword);
            currentIndex = 0;

        }

        // if no matches found, dont search!
        if (allIndexes.isEmpty()) return;
        
        
        int index = allIndexes.get(currentIndex % allIndexes.size());
        int keywordLength = keyword.length();
        
        textArea.requestFocus();
        textArea.select(index, index + keywordLength);
        
        currentIndex++;
    }

    private ArrayList<Integer> getAllIndexes(String body, String keyword){

        ArrayList<Integer> allIndexes = new ArrayList<>();

        int index = body.indexOf(keyword);
        while (index >= 0) {
            allIndexes.add(index);
            index = body.indexOf(keyword, index + 1);
        }

        return allIndexes;

    }
    
}