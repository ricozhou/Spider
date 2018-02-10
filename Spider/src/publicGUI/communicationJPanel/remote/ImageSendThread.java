package publicGUI.communicationJPanel.remote;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 发送图片映像的线程
 * @author jame
 *
 */
public class ImageSendThread extends Thread{
	private DataOutputStream dataOut;
	public ImageSendThread(DataOutputStream dataOut){
		this.dataOut  = dataOut;
	}
	public void run(){
		Robot robot = null;
		try{
			robot = new Robot();
			//本地的屏幕尺寸
			Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();
			//创建一个矩形的对象
			Rectangle rect = new Rectangle(dm);
			while(true){
				BufferedImage image = robot.createScreenCapture(rect);
				byte[] data = getByteImage(image);
				dataOut.writeInt(data.length);
				dataOut.write(data);
				dataOut.flush();
				//image对象 直接序列化和反序列化
				Thread.sleep(20);
				//System.out.println("图像的发送！！！");
				
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public byte[] getByteImage(BufferedImage image) throws ImageFormatException, IOException{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		//将图片对象编码成字节数组
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bout);
		encoder.encode(image);
		return bout.toByteArray();
	}
}

