import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ShowImportant extends JFrame {
    private ArrayList<String> importantEvents = new ArrayList<>();
    private Map<String, Map<Integer, ArrayList<String>>> godMap;
    private Map<Integer, ArrayList<String>> eventDay = new HashMap<>();
    private String currentUsernameCA;
    private int currentDayCA;

    public ShowImportant(int count) {
        setTitle("Events");
        setSize(300, 200);
        setLocationRelativeTo(null);
        
        AuthenticationPanel a = AuthenticationPanel.instance;
        CalendarApp b = new CalendarApp();
        currentUsernameCA = a.getName();
        currentDayCA = b.getSelectedDay();
        godMap = loadGodMap();
        
        godMap.forEach((key, value) -> {
           if (key.equals(currentUsernameCA)) {
               value.forEach((key2, value2) -> {
                  if (key2 == currentDayCA) {
                     importantEvents = value2;
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

