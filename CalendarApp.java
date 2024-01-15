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

public class CalendarApp extends JFrame {
   private JLabel monthLabel;
   private JPanel calendarPanel;
   private JPanel inputPanel;
   private JPanel barGraphPanel;
   private JPanel showEventsPanel;
   private JPanel importantEventsPanel;
   private JPanel loginPanel;

   // Specified Variables
   private String barGraphInput;
   private int specifiedDay;
   private int specifiedMonth;

   // amount of events in a day (Manual) (Bar Graph)
   private Map<String, Integer> currentMap = new LinkedHashMap<>();
   private Map<Integer, Integer> eventCountsPerManualDay = new LinkedHashMap<>();

   // Amount of events in a day (clicked) (show Events)
   private int countEvents = 0;
   private Map<Integer, Integer> eventCountsPerDay = new LinkedHashMap<>();

   // Important Events
   private int countImp = 0;
   private Map<String, Integer> importantEventsMap = new LinkedHashMap<>();

   // Calendar Setup
   private Calendar currentMonth;
   private String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

   // User-specific data
   private String username;
   private Map<Integer, Set<String>> userEvents;

   public CalendarApp(String username) {
      this.username = username;
      userEvents = loadUserData(); // Load user-specific data
   
      currentMonth = Calendar.getInstance();
   
      setTitle("Java Calendar");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(600, 500);
      setLocationRelativeTo(null);
   
      createGUI();
   
      setVisible(true);
   }



   private void createGUI() {
      Container container = getContentPane();
      container.setLayout(new BorderLayout());
   
      // Month Label
      monthLabel = new JLabel();
      monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
      updateMonthLabel();
      container.add(monthLabel, BorderLayout.NORTH);
   
      // Calendar Panel
      calendarPanel = new JPanel(new GridLayout(6, 7));
      updateCalendar();
      container.add(calendarPanel, BorderLayout.CENTER);
   
      // Buttons for navigation
      JPanel buttonPanel = new JPanel();
      JButton prevButton = new JButton("<< Prev");
      JButton addButton = new JButton("Add Event");
      JButton barGraphButton = new JButton("Bar Graph");
      JButton importantButton = new JButton("Important Button");
      JButton nextButton = new JButton("Next >>");
      JButton saveButton = new JButton("Save");
   
      // addActionListener click detection
      prevButton.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               currentMonth.add(Calendar.MONTH, -1);
               updateMonthLabel();
               updateCalendar();
            }
         });
   
      importantButton.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               updateImportantEventsList();
            }
         });
   
      addButton.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               updateInput();
            }
         });
   
      barGraphButton.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               openBarGraph();
            }
         });
   
      nextButton.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               currentMonth.add(Calendar.MONTH, 1);
               updateMonthLabel();
               updateCalendar();
            }
         });
      saveButton.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               saveUserData();
               JOptionPane.showMessageDialog(CalendarApp.this, "Data saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
         });
   
      // Create buttons
   
      buttonPanel.add(prevButton);
      buttonPanel.add(addButton);
      buttonPanel.add(barGraphButton);
      buttonPanel.add(saveButton);
      buttonPanel.add(importantButton);
      buttonPanel.add(nextButton);
      container.add(buttonPanel, BorderLayout.SOUTH);
   }


   // Month Label
   private void updateMonthLabel() {
      SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
      monthLabel.setText(sdf.format(currentMonth.getTime()));
   }

   // Bar Graph startup
   private void openBarGraph() {
      // Use the current month and year
      Calendar calendar = Calendar.getInstance();
   
      String dateString = JOptionPane.showInputDialog("Enter a day of the month:");
      try {
         int selectedDay = Integer.parseInt(dateString);
      
         if (selectedDay >= 1 && selectedDay <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            // Set the selected day in the current month and year
            calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
         
            // Calculate the start day (Sunday) and end day (Saturday) of the
            // week
            int dayOfWeekIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            int startDay = selectedDay - dayOfWeekIndex;
         
            // Store each date from Sunday to Saturday in an array
            String[] selectedWeekDates = new String[7];
            for (int i = 0; i < 7; i++) {
               selectedWeekDates[i] = String.valueOf(startDay + i);
            }
            Map<Integer, Integer> eventCountsPerManualDay2 = new LinkedHashMap<>();
            eventCountsPerManualDay2.putAll(eventCountsPerManualDay);
         
            Iterator<Integer> iterator = eventCountsPerManualDay2.keySet().iterator();
         
            while (iterator.hasNext()) {
               int it = iterator.next();
               if (startDay > it || startDay + 6 < it) {
                  iterator.remove();
               }
            }
         
            Map<Integer, Integer> modifications = new HashMap<>();
         
            for (int i = startDay; i < startDay + 7; i++) {
               if (!eventCountsPerManualDay2.containsKey(i)) {
                  modifications.put(i, 0);
               }
            }
         
            eventCountsPerManualDay2.putAll(modifications);
         
            List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(eventCountsPerManualDay2.entrySet());
            Collections.sort(entryList, Comparator.comparingInt((Map.Entry<Integer, Integer> entry) -> entry.getKey()));
         
            // Create a new LinkedHashMap with sorted entries
            LinkedHashMap<Integer, Integer> sortedEventCountsPerDay2 = new LinkedHashMap<>();
            for (Map.Entry<Integer, Integer> entry : entryList) {
               sortedEventCountsPerDay2.put(entry.getKey(), entry.getValue());
            }
            System.out.println("Event Counts Per Day: " + sortedEventCountsPerDay2);
         
            // Pass the correct data (eventCountsPerDay) to the BarGraph
            // constructor
            if (sortedEventCountsPerDay2.size() > 0) {
               // Check if there is any key with a value greater than 0
               boolean hasPositiveValue = sortedEventCountsPerDay2.values().stream().anyMatch(value -> value > 0);
            
               if (hasPositiveValue) {
                  SwingUtilities.invokeLater(
                     () -> {
                        BarGraph barGraph = new BarGraph(sortedEventCountsPerDay2);
                        barGraph.setVisible(true);
                     });
               } else {
                  JOptionPane.showMessageDialog(null, "There are no events this week.");
                  updateCalendar();
               }
            } else {
               JOptionPane.showMessageDialog(null, "There are no events for this week.");
               updateCalendar();
            }
         
         } else {
            JOptionPane.showMessageDialog(null, "Invalid day. Please enter a valid day for the current month.");
         }
      } catch (NumberFormatException e) {
         JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number for the day.");
      }
   }

   private void updateBarGraph(int selectedDay) {
      // Increment the count for the selected day
      eventCountsPerManualDay.put(selectedDay, eventCountsPerManualDay.getOrDefault(selectedDay, 0) + 1);
   
      // Print the updated counts
      for (Map.Entry<Integer, Integer> entry : eventCountsPerManualDay.entrySet()) {
         Integer day = entry.getKey();
         Integer countCloudz = entry.getValue();
      
         // Print key-value pairs
         System.out.println("Day: " + day + ", Count: " + countCloudz);
      }
   }

   // Important Events update call
   private void updateImportantEventsList() {
      if (countImp > 0) {
         ShowImportant importantEventsText = new ShowImportant(countImp);
      
         for (Map.Entry<String, Integer> entry : importantEventsMap.entrySet()) {
            String event = entry.getKey();
            Integer count = entry.getValue();
            importantEventsText.add(new JLabel(event));
         }
      
         importantEventsText.setVisible(true);
      } else {
         JOptionPane.showMessageDialog(null, "There are no important events.");
         updateCalendar();
      }
   }

   // Show Events :-)
   private void showEvents(int dayClicked) {
      if (currentMap.containsValue(dayClicked)) { // Check if the day is present in currentMap
         ShowEvents eventsPanel = new ShowEvents(countEvents);
      
         for (Map.Entry<String, Integer> entry : currentMap.entrySet()) {
            Integer days = entry.getValue();
            String event = entry.getKey();
         
            if (days == dayClicked) {
               eventsPanel.add(new JLabel(event));
            }
         }
         eventsPanel.setVisible(true);
      } else {
         JOptionPane.showMessageDialog(null, "There are no events for this day.");
         updateCalendar();
      }
   }

   private void updateInput() {
      JTextField title = new JTextField();
      JTextField day = new JTextField();
      JTextField month = new JTextField();
   
      JToggleButton allDay = new JToggleButton("False");
      JToggleButton important = new JToggleButton("Not Important");
   
      allDay.addActionListener(
         new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (allDay.isSelected()) {
                  allDay.setText("True");
               } else {
                  allDay.setText("False");
               }
            }
         });
   
      important.addActionListener(
         new ActionListener() {
            int countTemp = countImp;
            @Override
            public void actionPerformed(ActionEvent e) {
               if (important.isSelected()) {
                  important.setText("Important");
                  countImp++;
               } else {
                  important.setText("Not Important");
                  countImp = countTemp;
               }
            }
         });
   
      JPanel inputPanel = new JPanel(new GridLayout(7, 2));
      inputPanel.add(new JLabel("Day: "));
      inputPanel.add(day);
      inputPanel.add(new JLabel("Month: "));
      inputPanel.add(month);
      inputPanel.add(new JLabel("Title: "));
      inputPanel.add(title);
      inputPanel.add(new JLabel("All-Day: "));
      inputPanel.add(allDay);
      inputPanel.add(new JLabel("Important: "));
      inputPanel.add(important);
   
      int result = JOptionPane.showConfirmDialog(null, inputPanel, "New Event", JOptionPane.OK_CANCEL_OPTION);
   
      // !Null input check
      if (result == JOptionPane.OK_OPTION) {
         try {
            // Adding more and more
            barGraphInput = title.getText();
            specifiedDay = Integer.parseInt(day.getText());
            currentMap.put(barGraphInput, specifiedDay);
            eventCountsPerDay.put(specifiedDay, countEvents++);
            try {
               specifiedMonth = Integer.parseInt(month.getText());
            } catch (NumberFormatException ex) {
               JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number for the month.");
               return;
            }
         } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number for the day.");
         }
      
         if (specifiedMonth < LocalDate.now().getMonthValue()) {
            JOptionPane.showMessageDialog(null, "This day is in the past!");
            updateCalendar();
         } else if (specifiedMonth == LocalDate.now().getMonthValue() && specifiedDay < LocalDate.now().getDayOfMonth()) {
            JOptionPane.showMessageDialog(null, "This day is in the past!");
            updateCalendar();
         } else {
            updateBarGraph(specifiedDay);
            importantEventsMap.put(barGraphInput, countImp);
            JOptionPane.showMessageDialog(null, "Event added successfully!");
         }
      
         saveUserData();
      }
   }

 private Map<Integer, Set<String>> loadUserData() {
    Map<Integer, Set<String>> userData = new HashMap<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(getUserDataFilePath()))) {
        String line;
        while ((line = reader.readLine()) != null) {
            // Split the line into day and events
            String[] parts = line.split("\\|");
            int day = Integer.parseInt(parts[0]);
            Set<String> events = new HashSet<>(Arrays.asList(parts[1].split(",")));

            userData.put(day, events);
        }
    } catch (FileNotFoundException e) {
        System.out.println("User data file not found. Creating a new file.");
        saveUserData(); // Create a new empty file
    } catch (IOException e) {
        e.printStackTrace(); // Handle other exceptions
    }

    return userData;
}


// Add a method to get the user-specific data file path based on the username
private String getUserDataFilePathForUsername() {
    return "user_data_" + username + ".dat";
}

  private void saveUserData() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(getUserDataFilePath()))) {
        // Save user-specific data to the file
        for (Map.Entry<Integer, Set<String>> entry : userEvents.entrySet()) {
            int day = entry.getKey();
            Set<String> events = entry.getValue();

            // Format: day|event1,event2,event3,...
            String line = day + "|" + String.join(",", events);
            writer.write(line);
            writer.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace(); // Handle the exception
    }
}
   private void updateCurrentMap(Map<Integer, Set<String>> userData) {
      for (Set<String> events : userData.values()) {
         for (String event : events) {
            currentMap.put(event, specifiedDay); // Update currentMap based on loaded data
         }
      }
   }

   private String getUserDataFilePath() {
      // Define the path to the file where user-specific data is stored
      return "user_data_" + username + ".dat";
   }

   private void updateCalendar() {
      calendarPanel.removeAll();
   
    // Days of the week in one line at the top
      for (String day : daysOfWeek) {
         JLabel label = new JLabel(day, SwingConstants.CENTER);
         calendarPanel.add(label);
      }
   
    // Blank spaces for the first day of the month
        Calendar c = Calendar.getInstance();
    c.set(Calendar.DAY_OF_MONTH, 1);
    System.out.println(c.getTime()); 
    String firstDay = c.getTime().toString().substring(0, 3);
    System.out.println(firstDay);
    HashMap<String, Integer> days = new HashMap<>();
    days.put("Sun", 1);
    days.put("Mon", 2);
    days.put("Tue", 3);
    days.put("Wed", 4);
    days.put("Thu", 5);
    days.put("Fri", 6);
    days.put("Sat", 7);
    int firstDayOfMonth = days.get(firstDay);
    System.out.println(firstDayOfMonth);
      for (int i = 1; i < firstDayOfMonth - 1; i++) {
    calendarPanel.add(new JLabel());
}              
   
    // Days of the month
      int maxDay = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
      for (int i = 1; i <= maxDay; i++) {
         JButton dayButton = new JButton(Integer.toString(i));
         dayButton.addActionListener(new DayButtonListener(i));
      
         calendarPanel.add(dayButton);
      }
   
      highlightCurrentDay();
                                                                              
      revalidate();
      repaint();
   }
   
   private class DayButtonListener implements ActionListener {
      private int day;
   
      public DayButtonListener(int day) {
         this.day = day;
      }
   
      @Override
      public void actionPerformed(ActionEvent e) {
         showEvents(day);
      }
   }

   private void highlightCurrentDay() {
      // Get current day
      int currentDay = LocalDate.now().getDayOfMonth();
   
      // Get current month
      int currentMonth = LocalDate.now().getMonthValue();
      // Check if current month matches calendar month
      if (currentMonth == this.currentMonth.get(Calendar.MONTH) + 1) {
         // Loop through calendar panel components
         for (Component component : calendarPanel.getComponents()) {
            // Check if component is a day button
            if (component instanceof JButton) {
               JButton dayButton = (JButton) component;
            
               // Get day number from button text
               int day = Integer.parseInt(dayButton.getText());
            
               // Highlight current day
               if (day == currentDay) {
                  dayButton.setBackground(Color.YELLOW);
                  dayButton.setBorder(BorderFactory.createLoweredBevelBorder());
               }
            }
         }
      }
   }
}
