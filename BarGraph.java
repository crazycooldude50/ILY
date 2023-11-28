import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class BarGraph extends JFrame {

   private Map<String, Integer> data;

   public BarGraph(Map<String, Integer> data) {
   
      setSize(1200, 1000);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   
      BarChartPanel chartPanel = new BarChartPanel(data);
      chartPanel.setPreferredSize(new Dimension(400, 300));
      setContentPane(chartPanel);
   }

   public static void main(String[] args) {
      Map<String, Integer> data = new LinkedHashMap<>();
      data.put("Monday", 5);
      data.put("Tuesday", 8);
      data.put("Wednesday", 3);
      data.put("Thursday", 4);
      data.put("Friday", 1);
      data.put("Saturday", 14);
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
   
      int barWidth = 100;
      int barGap = 40;
      int startX = 50;
      int endX = getWidth() - 50;
      int startY = getHeight() - 50;
      int endY = 50;
   
      int maxValue = data.values().stream().max(Integer::compare).orElse(0);
   
      g.drawLine(startX, startY, endX, startY);
      g.drawLine(startX, startY, startX, endY);
      g.setColor(Color.RED);
      g.drawString("Day", endX - 50, startY + 20);
      g.drawString("Events", startX - 40, endY + 20);
      g.setColor(Color.BLACK);
   
      for (int i = 0; i <= maxValue; i++) {
         g.drawString(Integer.toString(i), startX - 30, startY - (i * 30));
      }
   
      g.setColor(Color.blue);
      int x = startX + barGap;
   
      for (Map.Entry<String, Integer> entry : data.entrySet()) {
         String category = entry.getKey();
         int value = entry.getValue();
         int barHeight = (int) ((double) value / maxValue * (startY - endY));
      
         g.fillRect(x, startY - barHeight, barWidth, barHeight);
         g.setColor(Color.black);
      
         int labelX = x + barWidth / 2 - g.getFontMetrics().stringWidth(category) / 2;
         g.drawString(category, labelX, startY + 15);
      
         g.drawString(Integer.toString(value), startX - 30, startY - barHeight + barHeight / 2);
         x += barWidth + barGap;
         g.setColor(Color.blue);
      }
   }
}
