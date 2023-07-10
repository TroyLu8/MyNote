import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.awt.*;
import javax.swing.*;

public class DataManager {
    static File file;
    static TextViewer textViewer;
    static JTextArea textArea;
    static String data;

    public static void init(TextViewer textViewer) {

        DataManager.textViewer = textViewer;
        DataManager.textArea = textViewer.textArea;

        textViewer.openButton.addActionListener(new DataOpener());
        textViewer.saveButton.addActionListener(new DataSaver());
        textViewer.newButton.addActionListener(new DataNew());
        textViewer.deleteButton.addActionListener(new DataDeleter());
        textViewer.settingsButton.addActionListener(new Settings(textViewer));
    }

    static void ensureExists(String filepath) {
        try {
            new File(filepath).createNewFile();
        } 
        catch (IOException e) { e.printStackTrace();}
    }

    static void setFile(String filepath){
        ensureExists(filepath);

        file = new File(filepath);

        fetchData();

        textArea.setText(data);

        textViewer.setEditingEnabled(true);
        textViewer.setTitle("MyNote          currently editing: " + removeSuffix(file.getName()));

    }

    static void openSaveNowPopup(){
        if (!textArea.getText().equals(data)){

            UIPanel message = new UIPanel(new GridLayout(2, 1));
            message.add(new UILabel("data is not saved!"));
            message.add(new UILabel("save now?"));

            if (Main.showPrompt(message, "save", "discard") == 0){                
                saveAndWriteData(textArea.getText());
            }
                        
        }

    }

    static void fetchData(){

        try (BufferedInputStream input = new BufferedInputStream (new FileInputStream(file))){

            byte[] allBytes = input.readAllBytes();
            if (allBytes.length == 0) {
                data = "";
                return;
            }

            byte[] ivBytes = Arrays.copyOfRange(allBytes, 0, 16);
            byte[] ciphertext = Arrays.copyOfRange(allBytes, 16, allBytes.length);

            data = AESCipher.decrypt(ciphertext, ivBytes);
            
        } 
        catch (IOException ioe) { ioe.printStackTrace(); }

    }

    static void saveAndWriteData(String data){

        try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file))) {

            DataManager.data = data;

            // create iv
            byte[] ivBytes = new byte[16];          
            new SecureRandom().nextBytes(ivBytes);

            byte[] ciphertext = AESCipher.encrypt(data.getBytes(StandardCharsets.UTF_8), ivBytes);

            byte[] allBytes = new byte[16 + ciphertext.length];

            // combine iv and ciphertext into allbytes
            for (int i = 0; i < 16; i++) 
                allBytes[i] = ivBytes[i];
            for (int i = 16; i < allBytes.length; i++) 
                allBytes[i] = ciphertext[i-16];
            
            output.write(allBytes);

        } 
        catch (IOException ioe) { ioe.printStackTrace(); }
    }

    static String removeSuffix(String filename) {
        return filename.substring(0, filename.length()-4);
    } 

    static byte[] getHash(String data) {
        MessageDigest hasher;
        try {
            hasher = MessageDigest.getInstance("SHA-256");
            return hasher.digest(data.getBytes());
        } catch (NoSuchAlgorithmException e) {e.printStackTrace();}

        return null;
    }

    static void writeMetadata(byte[] passHash, int themeID, int fontSize) {
        try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream("mynote_data/metadata.dat"))) {

            output.write(passHash);
            output.write(themeID);
            output.write(fontSize);

        } 
        catch (IOException e) { e.printStackTrace(); }
    }
    static void writeMetadata(String pass, int themeID, int fontSize) {
        writeMetadata(getHash(pass), themeID, fontSize);
    }
    static void writeMetadata(int themeID, int fontSize) {
        writeMetadata(Main.passwordHash, themeID, fontSize);
    }

    static boolean fetchMetadata() {
        DataManager.ensureExists("mynote_data/metadata.dat");        // ensure metadata file exists
        try (BufferedInputStream input = new BufferedInputStream (new FileInputStream("mynote_data/metadata.dat"))){

            if (input.available() == 0) {
                Settings.themeID = 0;
                Settings.fontSize = 14;
                return false;
            }

            input.read(Main.passwordHash, 0, 32);
            Settings.themeID = input.read();
            Settings.fontSize = input.read();

            return true;
        } 
        catch (IOException ioe) { 
            ioe.printStackTrace(); 
            return false;
        }
        
    }


}
