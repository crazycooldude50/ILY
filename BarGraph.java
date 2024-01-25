/* Hitaansh Jain, Aaron Rhim, Sandilya Parimi
   Period 1
   1/20/2024

   Once a user has stored at least one event, they can create a bar graph that visualizes the number of events per day for any specified week. 
   This is extremely useful for looking at a week to see which days are busier or calmer. 
   If there are no events in the specified week, the user will receive will a message letting them know, and the bar graph will not be constructed.
*/

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.awt.geom.AffineTransform;

public class BarGraph extends JFrame {

    private Map<Integer, Integer> data;

    // Constructor
    public BarGraph(Map<Integer, Integer> data) {
        // Create the window
        setSize(1150, 600);
        setResizable(false);

        BarChartPanel chartPanel = new BarChartPanel(data);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        setContentPane(chartPanel);
    }

    // Inner class representing the BarChartPanel
    private class BarChartPanel extends JPanel {

        private Map<Integer, Integer> data;

        // Constructor
        public BarChartPanel(Map<Integer, Integer> data) {
            this.data = data;
        }

        // Override paintComponent to draw the bar chart
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int barWidth = 100;
            int barGap = 40;
            int startX = 70;
            int endX = getWidth() - 50;
            int startY = getHeight() - 50;
            int endY = 50;

            int maxValue = data.values().stream().max(Integer::compare).orElse(0);

            Axes(g, startX, endX, startY, endY, maxValue);

            g.setColor(Color.blue);
            int x = startX + barGap;

            for (Map.Entry<Integer, Integer> entry : data.entrySet()) {
                int day = entry.getKey();
                int value = entry.getValue();
                int barHeight = (int) ((double) value / maxValue * (startY - endY));

                g.fillRect(x, startY - barHeight, barWidth, barHeight);
                g.setColor(Color.black);

                // Convert day to the day of the week
                String dayOfWeek = getDayOfWeek(day);

                int labelX = x + barWidth / 2 - g.getFontMetrics().stringWidth(dayOfWeek) / 2;
                g.drawString(dayOfWeek, labelX, startY + 15);

                x += barWidth + barGap;
                g.setColor(Color.blue);
            }
        }

        // Method to draw Axes
        private void Axes(Graphics g, int startX, int endX, int startY, int endY, int maxValue) {
            g.drawLine(startX, startY, endX, startY);
            g.drawLine(startX, startY, startX, endY - 25);

            Font basicFont = new Font(g.getFont().getFontName(), Font.PLAIN, 12);
            Font axisLabelFont = new Font(g.getFont().getFontName(), Font.PLAIN, 18);
            g.setColor(Color.RED);

            g.setFont(axisLabelFont);
            g.drawString("Day", startX + ((endX - startX) / 2), startY + 40);

            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(-Math.PI / 2);
            Font rotatedFont = axisLabelFont.deriveFont(affineTransform);

            g.setFont(rotatedFont);
            g.drawString("Events", startX - 40, startY - ((startY - endY) / 2));

            g.setFont(basicFont);
            g.setColor(Color.BLACK);

            for (int i = 0; i <= maxValue; i++) {
                g.drawString(Integer.toString(i), startX - 25, startY - (i * ((startY - endY) / maxValue)));
            }
        }

        // Helper method to convert day to the day of the week
        private String getDayOfWeek(int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, day);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            // Convert numeric day of the week to string
            switch (dayOfWeek) {
                case Calendar.SUNDAY:
                    return "Sun";
                case Calendar.MONDAY:
                    return "Mon";
                case Calendar.TUESDAY:
                    return "Tue";
                case Calendar.WEDNESDAY:
                    return "Wed";
                case Calendar.THURSDAY:
                    return "Thu";
                case Calendar.FRIDAY:
                    return "Fri";
                case Calendar.SATURDAY:
                    return "Sat";
                default:
                    return "";
            }
        }
    }
}
