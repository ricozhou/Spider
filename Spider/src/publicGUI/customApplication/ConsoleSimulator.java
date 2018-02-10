package publicGUI.customApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.swing.JTextArea;

//另起线程截取程序流
public class ConsoleSimulator implements Runnable {
	public static boolean isStop = false;
	public static final int INFO = 0;
	public static final int ERROR = 1;
	public InputStream is;
	public int type;
	public JTextArea showArea;

	public ConsoleSimulator(InputStream is, int type, JTextArea showArea) {
		this.is = is;
		this.type = type;
		this.showArea = showArea;
	}

	public void run() {
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isr);
		String s;
		try {
			while ((!isStop) && (s = reader.readLine()) != null) {
				if (s.length() != 0) {
					if (type == INFO) {
						showArea.append(s + "\n");
					} else {
						showArea.append(s + "\n");
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void stop() {
		isStop = true;
	}

}
