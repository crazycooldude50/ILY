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
import java.util.ArrayList;

/*
   Aaron, Hitaansh, Sandy
   1/24/2024
   
   
*/

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

    // Creating instances of Authentication Panel
    AuthenticationPanel a = new AuthenticationPanel();
    private Map<String, String> userCredentialsCA;
    private String currentUsernameCA;

    // Username -> Day clicked, Event List
    private Map<String, Map<Integer, ArrayList<String>>> godMapCA;
    // Day clicked, Event List
    private Map<Integer, ArrayList<String>> eventDay = new HashMap<>();
    // Event List
    private ArrayList<String> eventList = new ArrayList<>();

    // instance
    private static int currentDay = 0;
    
    // God Map accessor
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

    // God Map mutator
    public void saveGodMap() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("god_map.dat"))) {
            oos.writeObject(godMapCA);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception
        }
    }

    // Day clicked accessor
    public static int getSelectedDay() {
        return currentDay;
    }

    public CalendarApp() {
        currentMonth = Calendar.getInstance();
        
        // Authentication Panel instance
        AuthenticationPanel a = AuthenticationPanel.instance;
        currentUsernameCA = a.getName();
        
        // initialize godmap
        godMapCA = loadGodMapCA();
        
        // condition (debugging)
        if (eventDay != null) {
            godMapCA.forEach((key, value) -> {
                if (key.equals(currentUsernameCA)) {
                    eventDay = value;
                    System.out.println("eventDay: " + eventDay);
                }
            });
        }

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
        JButton nextButton = new JButton("Next >>");

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

        // Create buttons
        buttonPanel.add(prevButton);
        buttonPanel.add(addButton);
        buttonPanel.add(barGraphButton);
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

                // Pass the correct data (eventCountsPerDay) to the BarGraph constructor
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
    
    // Show Events
    private void showEventsCA(int dayClicked) {
        // condition (debugging)
        if (eventDay == null) {
            JOptionPane.showMessageDialog(null, "There are no events for this day.");
            updateCalendar();
        }
        // click day output
        else if (eventDay.containsKey(dayClicked)) {
            ShowEvents eventsPanel = new ShowEvents(countEvents);
            ArrayList<String> event = new ArrayList<String>();
            Integer days = 0;

            eventsPanel.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "There are no events for this day.");
            updateCalendar();
        }
    }

    private void updateInput() {
        // Add Event
        JTextField title = new JTextField();
        JTextField day = new JTextField();
        JTextField month = new JTextField();

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Day: "));
        inputPanel.add(day);
        inputPanel.add(new JLabel("Month: "));
        inputPanel.add(month);
        inputPanel.add(new JLabel("Title: "));
        inputPanel.add(title);

        int result = JOptionPane.showConfirmDialog(null, inputPanel, "New Event", JOptionPane.OK_CANCEL_OPTION);

        // !Null input check
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Stacking
                barGraphInput = title.getText();
                specifiedDay = Integer.parseInt(day.getText());
                eventCountsPerDay.put(specifiedDay, countEvents++);
                currentDay = specifiedDay;

                // Condition (debugging)
                if (eventDay != null) {
                    eventDay.forEach((key, value) -> {
                        if (currentDay == key) {
                            eventList = value;
                        }
                    });
                }

                // Array list of events, condition: day clicked
                eventList.add(barGraphInput);

                // Repair (debugging)
                eventDay.put(currentDay, eventList);
                godMapCA.replace(currentUsernameCA, eventDay);
                saveGodMap();
                
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
                JOptionPane.showMessageDialog(null, "Event added successfully!");
            }
        }
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
        String firstDay = c.getTime().toString().substring(0, 3);
        HashMap<String, Integer> days = new HashMap<>();
        
        days.put("Sun", 1);
        days.put("Mon", 2);
        days.put("Tue", 3);
        days.put("Wed", 4);
        days.put("Thu", 5);
        days.put("Fri", 6);
        days.put("Sat", 7);
        
        // formatting (debugging)
        int firstDayOfMonth = days.get(firstDay);
        
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

    // day clicked
    private class DayButtonListener implements ActionListener {
        private int day;

        public DayButtonListener(int day) {
            this.day = day;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            currentDay = day;
            // working? (debugging)
            showEventsCA(day);

        }
    }

    // Highlight gimmick (Sandy's only code)
    private void highlightCurrentDay() {
        int currentDay = LocalDate.now().getDayOfMonth();
        int currentMonth = LocalDate.now().getMonthValue();
        
        if (currentMonth == this.currentMonth.get(Calendar.MONTH) + 1) {
            for (Component component : calendarPanel.getComponents()) {
                if (component instanceof JButton) {
                    JButton dayButton = (JButton) component;

                    int day = Integer.parseInt(dayButton.getText());

                    if (day == currentDay) {
                        dayButton.setBackground(Color.YELLOW);
                        dayButton.setBorder(BorderFactory.createLoweredBevelBorder());
                    }
                }
            }
        }
    }
}
