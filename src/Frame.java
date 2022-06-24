import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    Panel panel = new Panel();
    ImageIcon icon = new ImageIcon("snake.png");

    Frame(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setIconImage(icon.getImage());
        this.setTitle("Snake Game");

        this.add(panel);

        this.setSize((int)panel.dimension.getWidth(),(int)panel.dimension.getHeight());
        this.setLocationRelativeTo(null);

        this.pack();
        this.setVisible(true);
    }
}
