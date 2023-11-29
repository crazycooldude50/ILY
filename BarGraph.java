import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.awt.geom.AffineTransform;

public class BarGraph extends JFrame {

   private Map<String, Integer> data;

   public BarGraph(Map<String, Integer> data) {
   
      // Create the window
      setSize(1150, 600);
      setResizable(false);
   
      BarChartPanel chartPanel = new BarChartPanel(data);
      chartPanel.setPreferredSize(new Dimension(400, 300));
      setContentPane(chartPanel);
   }

   public static void main(String[] args) {
      Map<String, Integer> data = new LinkedHashMap<>(); // LinkedHashMap to store the x and y value of each bar
      
      // Add each day of the week to the LinkedHashMap
      data.put("Monday", 5);
      data.put("Tuesday", 8);
      data.put("Wednesday", 3);
      data.put("Thursday", 4);
      data.put("Friday", 1);
      data.put("Saturday", 12);
      data.put("Sunday", 10);
   
      SwingUtilities.invokeLater(() -> { BarGraph barGraph = new BarGraph(data); barGraph.setVisible(true);});
   }
}

class BarChartPanel extends JPanel {

   private Map<String, Integer> data;

   public BarChartPanel(Map<String, Integer> data) {
      this.data = data;
   }

   public void paintComponent(Graphics g) {
      super.paintComponent(g);
   
      // Declare and initialize variables to store some basic values
      int barWidth = 100;
      int barGap = 40;
      int startX = 70;
      int endX = getWidth() - 50;
      int startY = getHeight() - 50;
      int endY = 50;
   
      int maxValue = data.values().stream().max(Integer::compare).orElse(0); // Tallest bar
   
      Axes(g, startX, endX, startY, endY, maxValue); // Create, title and scale the two axes
   
      g.setColor(Color.blue);
      int x = startX + barGap; // Add some distance between the first bar and the y-axis
   
      for (Map.Entry<String, Integer> entry : data.entrySet()) { // For-each loop to traverse through the LinkedHashMap
         String category = entry.getKey(); // Retrieve the x value
         int value = entry.getValue(); // Retrieve the y value
         int barHeight = (int) ((double) value / maxValue * (startY - endY));
      
         g.fillRect(x, startY - barHeight, barWidth, barHeight); // Create the bar
         g.setColor(Color.black);
      
         int labelX = x + barWidth / 2 - g.getFontMetrics().stringWidth(category) / 2; // Label the x axis for the new bar
         g.drawString(category, labelX, startY + 15);
         
         x += barWidth + barGap;
         g.setColor(Color.blue);
      }
   }
  
   public void Axes(Graphics g, int startX, int endX, int startY, int endY, int maxValue) {
      // Create the two axes
      g.drawLine(startX, startY, endX, startY);
      g.drawLine(startX, startY, startX, endY - 25);
      
      
      Font basicFont = new Font(g.getFont().getFontName(), Font.PLAIN, 12); // Store the basic font to fall back to once done with axes titles
      Font axisLabelFont = new Font(g.getFont().getFontName(), Font.PLAIN, 18); // Bigger font for the axes title
      g.setColor(Color.RED); // Make the axes title font red
      
      // Title the x-axis
      g.setFont(axisLabelFont);
      g.drawString("Day", startX + ((endX - startX) / 2), startY + 40); 
     
      // Use AffineTransform to rotate the text for the title for the y-axis
      AffineTransform affineTransform = new AffineTransform(); 
      affineTransform.rotate(-Math.PI / 2);
      Font rotatedFont = axisLabelFont.deriveFont(affineTransform);
      
      // Title the y-axis
      g.setFont(rotatedFont);
      g.drawString("Events", startX - 40, startY - ((startY - endY) / 2)); 
      
      // Return to default
      g.setFont(basicFont);
      g.setColor(Color.BLACK);
     
      for (int i = 0; i <= maxValue; i++) {
         g.drawString(Integer.toString(i), startX - 25, startY - (i * ((startY-endY)/maxValue))); // Label the y-axis in a linear fashion starting from 0 till the height of the tallest bar
      }
   }
}
