package publicGUI.communicationJPanel.remote;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 
 * @author jame
 * 进行远程操作的UI，这个是给控制端使用
 */
public class RemoteUI  extends JFrame {
	private Socket socket;
	private JLabel background;//用于显示对方的桌面
	private ObjectOutputStream objOut;
	public RemoteUI(String title,Socket socket){
		try {
			objOut = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.setTitle(title);
		this.socket = socket;
		this.background = new JLabel();
		JPanel panel = new JPanel();
		panel.add(background);
		JScrollPane scrollPane = new JScrollPane(panel);
		this.add(scrollPane);
		this.setSize(800,600);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		background.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				sendEvent(e);
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				sendEvent(e);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		background.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				sendEvent(e);
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				sendEvent(e);
			}
		});
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				sendEvent(e);
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				sendEvent(e);
			}
		});
	//	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageThread imageThread = new ImageThread(socket);
		imageThread.start();
	}
	private void sendEvent(InputEvent event){
		try{
			objOut.writeObject(event);
			objOut.flush();
			System.out.println("输出事件对象！！！"+event);
		}catch(Exception ex){
			
			ex.printStackTrace();
		}
	}
	class ImageThread extends Thread{
		private Socket socket;
		public ImageThread(Socket socket){
			this.socket  = socket;
		}
		public void run(){
			try{
				DataInputStream dataIn = new DataInputStream(socket.getInputStream());
				while(true){
					byte[] image = new byte[dataIn.readInt()];
					dataIn.readFully(image);
					background.setIcon(new ImageIcon(image));
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
}


