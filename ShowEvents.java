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

public class ShowEvents extends JFrame {
   private ArrayList<String> events = new ArrayList<>();
   private Map<String, Map<Integer, ArrayList<String>>> godMap;
   private Map<Integer, ArrayList<String>> eventDay = new HashMap<>();
   private String currentUsernameCA;
   private int currentDayCA;

   public ShowEvents(int count) {
      setTitle("Events");
      setSize(300, 200);
      setLocationRelativeTo(null);
        
        //AuthenticationPanel a = AuthenticationPanel.instance;
        //CalendarApp b = new CalendarApp();
      currentUsernameCA = AuthenticationPanel.instance.getName();
      currentDayCA = CalendarApp.getSelectedDay();
      godMap = loadGodMap();
        
      godMap.forEach(
         (key, value) -> {
            System.out.println("Key in ShowEvents: " + key);
            System.out.println("CurrentUsername in ShowEvents: " + currentUsernameCA);
            if (key.equals(currentUsernameCA)) {
               value.forEach(
                  (key2, value2) -> {
                     System.out.println("CurrentDay in ShowEvents: " + currentDayCA);
                     if (key2.equals(currentDayCA)) { // Changed to key2.equals(currentDayCA)
                        events = value2;
                        System.out.println("From Show Events: " + godMap);
                     }
                  });
            }
         });
                //loadEvents(); // Load events from file
   
      JPanel showEventsPanel = new JPanel(new GridLayout(count, 1));
      setContentPane(showEventsPanel);
        
        
        // Populate events from the loaded data
      for (String event : events) {
         showEventsPanel.add(new JLabel(event));
      }
   }

   public Map<String, Map<Integer, ArrayList<String>>> loadGodMap() {
      Map<String, Map<Integer, ArrayList<String>>> god = new HashMap<>();
      System.out.println("hiiii");
      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("god_map.dat"))) {
         god = (Map<String, Map<Integer, ArrayList<String>>>) ois.readObject();
         System.out.println("Successfully loaded godMap: " + god);
      } catch (FileNotFoundException e) {
         System.out.println("File not found: god_map.dat");
        // Handle file not found (first run)
      } catch (IOException | ClassNotFoundException e) {
         e.printStackTrace(); // Handle other exceptions
      }
   
      return god;
   }

   
   public void saveGodMap() {
      try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("god_map.dat"))) {
            // Update events for the current user
         if (godMap.containsKey(currentUsernameCA)) {
            Map<Integer, ArrayList<String>> userEvents = godMap.get(currentUsernameCA);
            userEvents.put(currentDayCA, events);
         } else {
                // Create a new entry for the current user
            Map<Integer, ArrayList<String>> newUserEvents = new HashMap<>();
            newUserEvents.put(currentDayCA, events);
            godMap.put(currentUsernameCA, newUserEvents);
         }
      
            // Save the modified godMap to the file
         oos.writeObject(godMap);
      } catch (IOException e) {
         e.printStackTrace(); // Handle the exception
      }
   }
}
