import javax.swing.*;
import java.awt.*;

public class ShowImportant extends JFrame {

    public ShowImportant(int count) {
        setTitle("Important");
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel showImportantPanel = new JPanel(new GridLayout(count, 1));
        setContentPane(showImportantPanel);
    }
}
