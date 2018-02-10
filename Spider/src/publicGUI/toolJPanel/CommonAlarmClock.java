package publicGUI.toolJPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

//普通闹钟
public class CommonAlarmClock extends JFrame implements ActionListener{

	public CommonAlarmClock() {
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
