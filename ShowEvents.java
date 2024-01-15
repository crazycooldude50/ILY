import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ShowEvents extends JFrame {
    private List<String> events;

    public ShowEvents(int count) {
        setTitle("Events");
        setSize(300, 200);
        setLocationRelativeTo(null);

        events = new ArrayList<>();
        loadEvents(); // Load events from file

        JPanel showEventsPanel = new JPanel(new GridLayout(count, 1));
        setContentPane(showEventsPanel);

        // Populate events from the loaded data
        for (String event : events) {
            showEventsPanel.add(new JLabel(event));
        }
    }

private void loadEvents() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user_data_1.dat"))) {
        Map<Integer, Set<String>> userData = (Map<Integer, Set<String>>) ois.readObject();
        
        // Flatten the map to a list of events
        for (Set<String> eventsSet : userData.values()) {
            events.addAll(eventsSet);
        }
    } catch (FileNotFoundException e) {
        // Handle file not found (first run)
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace(); // Handle other exceptions
    }
}


public void saveEvents() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user_data_1.dat"))) {
        oos.writeObject(events);
        System.out.println("Events saved successfully.");
    } catch (IOException e) {
        e.printStackTrace();
        System.err.println("Error saving events: " + e.getMessage());
    }
}
}
