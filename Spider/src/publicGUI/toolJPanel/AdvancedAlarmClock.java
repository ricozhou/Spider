package publicGUI.toolJPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

//高级闹钟
public class AdvancedAlarmClock extends JFrame implements ActionListener{

	public AdvancedAlarmClock() {
		init();
	}
	
	private void init() {
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
