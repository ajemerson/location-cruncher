/**
 * Description: Window that prompts user for 
 * parameter values to use in calculating the
 * interactions of a list of users' times/locations.
 */
import javax.swing.*;

@SuppressWarnings("serial")
public class parameterGUI extends JFrame {
	public parameterGUI(String title) {
		super(title);
		this.setSize(400, 200);
		ControlPanel controlPanel = new ControlPanel(300,150);
		this.add(controlPanel, java.awt.BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public static void main (String [] args) {
		new parameterGUI("Choose parameters for interactions:");
	}
	
}