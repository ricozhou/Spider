package publicGUI.toolJPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

//标准计算器
public class StandardCalculator extends JFrame implements ActionListener, KeyListener{

	public StandardCalculator() {
		init();
	}
	
	private void init() {
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
