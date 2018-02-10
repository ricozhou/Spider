package publicGUI.communicationJPanel.remote;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.ObjectInputStream;

/**
 * 接收控制端发送的事件对象
 * @author jame
 *
 */
public class EventReciveThread extends Thread{
	/**
	 * 
	 */
	private ObjectInputStream objIn;
	private Robot robot;
	public EventReciveThread(ObjectInputStream objIn){
		this.objIn = objIn;
	}
	public void run(){
		try{
			robot = new Robot();
			while(true){
				//反序列话
				Object obj = objIn.readObject();
				InputEvent event = (InputEvent)obj;
				if(event instanceof KeyEvent){
					KeyEvent key = (KeyEvent)event;
					//获取事件类型 是通过key.getId()
					if(key.getID()==KeyEvent.KEY_PRESSED){
						robot.keyPress(key.getKeyCode());
					}else if(key.getID()==KeyEvent.KEY_RELEASED){
						robot.keyRelease(key.getKeyCode());
					}
				}else if(event instanceof MouseEvent){
					MouseEvent mouse = (MouseEvent)event;
					if(mouse.getID()==MouseEvent.MOUSE_PRESSED){
						robot.mousePress(getButton(mouse.getButton()));
					}else if(mouse.getID()==MouseEvent.MOUSE_RELEASED){
						robot.mouseRelease(getButton(mouse.getButton()));
					}else if(mouse.getID()==MouseEvent.MOUSE_MOVED){
						robot.mouseMove(mouse.getX(), mouse.getY());
					}else if(mouse.getID()==MouseEvent.MOUSE_DRAGGED){
						robot.mouseMove(mouse.getX(), mouse.getY());
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	private int getButton(int button){
		if(button==MouseEvent.BUTTON1){
			return InputEvent.BUTTON1_MASK;
		}else if(button==MouseEvent.BUTTON2){
			return InputEvent.BUTTON2_MASK;
		}else if(button==MouseEvent.BUTTON3){
			return InputEvent.BUTTON3_MASK;
		}
		return -1;
	}
}
