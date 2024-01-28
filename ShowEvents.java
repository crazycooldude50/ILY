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

/* Aaron Rhim, Hitaansh Jain, Sandilya Parimi
   Period 1
   1/20/2024 

   This class opens the events GUI when the user clicks on a day 
   with an event. It utilizes a JFrame panel to do this. This class
   stores the events on a file on the user's computer. It can also 
   use that same file to access and load events that were created in 
   a prior session.
*/

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
        
        godMap.forEach((key, value) -> {
           System.out.println("Key in ShowEvents: " + key);
           System.out.println("CurrentUsername in ShowEvents: " + currentUsernameCA);
           if (key.equals(currentUsernameCA)) {
               value.forEach((key2, value2) -> {
               System.out.println("CurrentDay in ShowEvents: " + currentDayCA);
                  if (key2 == currentDayCA) {
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

      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("god_map.dat"))) {
         god = (Map<String, Map<Integer, ArrayList<String>>>) ois.readObject();
      } catch (FileNotFoundException e) {
         // Handle file not found (first run)
      } catch (IOException | ClassNotFoundException e) {
         e.printStackTrace(); // Handle other exceptions
      }

      return god;
   }
   
   public void saveGodMap() {
      try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("god_map.dat"))) {
         oos.writeObject(godMap);
      } catch (IOException e) {
         e.printStackTrace(); // Handle the exception
      }
   }
}
