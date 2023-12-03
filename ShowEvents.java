import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("events.dat"))) {
            events = (List<String>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Handle file not found (first run)
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle other exceptions
        }
    }

    public void saveEvents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("events.dat"))) {
            oos.writeObject(events);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception
        }
    }
}
