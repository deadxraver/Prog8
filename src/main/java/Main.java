import javax.swing.*;
import java.awt.*;

public class Main {
	public static void main(String... args) {
		JFrame a = new JFrame("example");
		JButton b = new JButton("click me");
		b.setBounds(40,90,85,20);
		a.add(b);
		a.setSize(300,300);
		a.setLayout(null);
		a.setBackground(Color.CYAN);
		a.setVisible(true);
	}
}
