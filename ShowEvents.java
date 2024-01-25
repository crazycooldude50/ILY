import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.border.*;

// CalendarApp Class
public class CalendarApp extends JFrame {

    // ... (existing code)

    // Load user credentials from file
    public Map<String, String> loadUserCredentialsCA() {
        Map<String, String> credentials = new HashMap<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user_credentials.dat"))) {
            credentials = (Map<String, String>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Handle file not found (first run)
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle other exceptions
        }

        return credentials;
    }

    // Load god map from file
    public Map<String, Map<Integer, ArrayList<String>>> loadGodMapCA() {
        Map<String, Map<Integer, ArrayList<String>>> god = new HashMap<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("god_map.dat"))) {
            god = (Map<String, Map<Integer, ArrayList<String>>>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Handle file not found (first run)
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle other exceptions
        }

        return god;
    }

    // Save god map to file
    public void saveGodMap() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("god_map.dat"))) {
            oos.writeObject(godMapCA);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception
        }
    }

    // Get selected day
    public static int getSelectedDay() {
        return currentDay;
    }

    // Create GUI components
    private void createGUI() {
        // ... (existing code)
    }

    // Update month label
    private void updateMonthLabel() {
        // ... (existing code)
    }

    // Bar Graph startup
    private void openBarGraph() {
        // ... (existing code)
    }

    // Update Bar Graph
    private void updateBarGraph(int selectedDay) {
        // ... (existing code)
    }

    // Show Events
    private void showEventsCA(int dayClicked) {
        // ... (existing code)
    }

    // Update Input for adding new events
    private void updateInput() {
        // ... (existing code)
    }

    // ... (existing code)

}

// ShowEvents Class
public class ShowEvents extends JFrame {
    // ... (existing code)

    // Constructor
    public ShowEvents(int count) {
        // ... (existing code)
    }

    // Load god map from file
    public Map<String, Map<Integer, ArrayList<String>>> loadGodMap() {
        // ... (existing code)
    }

    // Save god map to file
    public void saveGodMap() {
        // ... (existing code)
    }

    // ... (existing code)
}
