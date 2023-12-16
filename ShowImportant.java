import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ShowImportant extends JFrame {
    private List<String> importantEvents;

    public ShowImportant(int count) {
        setTitle("Important");
        setSize(300, 200);
        setLocationRelativeTo(null);

        importantEvents = new ArrayList<>();
        loadImportantEvents(); // Load important events from file

        JPanel showImportantPanel = new JPanel(new GridLayout(count, 1));
        setContentPane(showImportantPanel);

        // Populate important events from the loaded data
        for (String event : importantEvents) {
            showImportantPanel.add(new JLabel(event));
        }
    }

    private void loadImportantEvents() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("important_events.dat"))) {
            importantEvents = (List<String>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Handle file not found (first run)
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle other exceptions
        }
    }

    public void saveImportantEvents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("important_events.dat"))) {
            oos.writeObject(importantEvents);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception
        }
    }
}
