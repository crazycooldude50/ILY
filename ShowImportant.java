import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ShowImportant extends JFrame {
    private List<String> importantEvents;
    private Map<String, Integer> importantEventsMap;

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
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user_data_1.dat"))) {
        Map<Integer, Set<String>> userData = (Map<Integer, Set<String>>) ois.readObject();
        
        // Flatten the map to a list of important events
        for (Set<String> eventsSet : userData.values()) {
            for (String event : eventsSet) {
                if (importantEventsMap.containsKey(event) && importantEventsMap.get(event) > 0) {
                    importantEvents.add(event);
                }
            }
        }
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
