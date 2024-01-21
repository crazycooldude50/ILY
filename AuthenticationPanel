import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import java.util.ArrayList;

public class AuthenticationPanel extends JFrame {
   // static variable to hold the instance
   public static AuthenticationPanel instance;
   
   private Map<String, String> userCredentials;
   private String currentUsername;
   private Map<String, Map<Integer, ArrayList<String>>> godMap;

   public AuthenticationPanel() {
      userCredentials = loadUserCredentials();
      godMap = loadGodMap();
      currentUsername = "";

      setTitle("Sign Up / Login Panel");
      setSize(300, 150);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLocationRelativeTo(null);

      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(3, 1));

      JButton signUpButton = new JButton("Sign Up");
      JButton loginButton = new JButton("Login");

      signUpButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            showSignUpDialog();
         }
      });

      loginButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            showLoginDialog();
         }
      });

      panel.add(signUpButton);
      panel.add(loginButton);

      add(panel);

      setVisible(true);
   }
   
   public String getName() {
      return currentUsername;
   }

   public Map<String, String> loadUserCredentials() {
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

   public void saveUserCredentials() {
      try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user_credentials.dat"))) {
         oos.writeObject(userCredentials);
      } catch (IOException e) {
         e.printStackTrace(); // Handle the exception
      }
   }
   
   public void saveGodMap() {
      try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("god_map.dat"))) {
         oos.writeObject(godMap);
      } catch (IOException e) {
         e.printStackTrace(); // Handle the exception
      }
   }
   
   private void authUpdate() {
      godMap.put(currentUsername, null);
      System.out.println("Update GodMap: " + godMap);
      System.out.println("Current username: " + currentUsername);
      System.out.println("Update userCredentials: " + userCredentials);
   }

  private void showSignUpDialog() {
    JFrame signUpFrame = new JFrame("Sign Up");
    signUpFrame.setSize(300, 150);
    signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    signUpFrame.setLocationRelativeTo(this);

    JPanel signUpPanel = new JPanel();
    signUpPanel.setLayout(new GridLayout(3, 2));

    JTextField usernameField = new JTextField();
    JPasswordField passwordField = new JPasswordField();

    JButton signUpButton = new JButton("Sign Up");
    signUpButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);

            if (!userCredentials.containsKey(username)) {
                userCredentials.put(username, password);
                authUpdate();
                saveUserCredentials(); // Save user credentials to file
                saveGodMap();
                JOptionPane.showMessageDialog(signUpFrame, "Sign Up Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                signUpFrame.dispose(); // Close the sign-up frame
            } else {
                JOptionPane.showMessageDialog(signUpFrame, "Username already exists. Please choose a different username.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
    signUpPanel.add(new JLabel("Username:"));
    signUpPanel.add(usernameField);
    signUpPanel.add(new JLabel("Password:"));
    signUpPanel.add(passwordField);
    signUpPanel.add(signUpButton);

    signUpFrame.add(signUpPanel);
    signUpFrame.setVisible(true);
}

private void showLoginDialog() {
    JFrame loginFrame = new JFrame("Login");
    loginFrame.setSize(300, 150);
    loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    loginFrame.setLocationRelativeTo(this);

    JPanel loginPanel = new JPanel();
    loginPanel.setLayout(new GridLayout(3, 2));

    JTextField usernameField = new JTextField();
    JPasswordField passwordField = new JPasswordField();

    JButton loginButton = new JButton("Login");
    loginButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);

            if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
                currentUsername = username;
                System.out.println("Current Username from AP: " + currentUsername);
                instance.openMainApplication(); // Start calendar
                loginFrame.dispose(); // Close the login frame
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid username or password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
    loginPanel.add(new JLabel("Username:"));
    loginPanel.add(usernameField);
    loginPanel.add(new JLabel("Password:"));
    loginPanel.add(passwordField);
    loginPanel.add(loginButton);

    loginFrame.add(loginPanel);
    loginFrame.setVisible(true);
}

   private void openMainApplication() {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            new CalendarApp(); // Pass the username to CalendarApp
         }
      });
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            instance = new AuthenticationPanel();
         }
      });
   }
}
