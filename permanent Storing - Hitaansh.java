import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class InformationManagementApp {
    private static final String FILE_NAME = "saved_information.txt";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Information Management App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JTextArea informationTextArea = new JTextArea(5, 30);
        JButton saveAndDisplayButton = new JButton("Save and Display Information");

        saveAndDisplayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String information = JOptionPane.showInputDialog("Enter Information:");
                if (information != null && !information.isEmpty()) {
                    appendInformationToFile(information);
                    displayInformationFromFile(informationTextArea);
                }
            }
        });

        JPanel panel = new JPanel();
        panel.add(saveAndDisplayButton);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(new JScrollPane(informationTextArea), BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static void appendInformationToFile(String information) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(information);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayInformationFromFile(JTextArea textArea) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            textArea.setText(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
