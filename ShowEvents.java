import javax.swing.*;
import java.awt.*;

public class ShowEvents extends JFrame {

    public ShowEvents(int count) {
        setTitle("Events");
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel showEventsPanel = new JPanel(new GridLayout(count, 1));
        setContentPane(showEventsPanel);
    }
}
