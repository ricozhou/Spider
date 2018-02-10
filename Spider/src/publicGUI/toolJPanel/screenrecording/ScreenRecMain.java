package publicGUI.toolJPanel.screenrecording;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class ScreenRecMain implements Runnable {
	public Thread thread,thread2,thread3,thread4,thread5,thread6,thread7,thread8,thread9,thread10;
	public Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	// 0开始，1暂停，2继续，3结束
	public static int screenFlag;
	public static int screenNum;
	public ReturnScreenMessage rsm = new ReturnScreenMessage();
	public ScreenRecParams srp;
	public JLabel jlb;
	public String name;
	public String filePath;
	public Robot rb;
	public Rectangle rt;
	public static BufferedImage screenshot;
	public List<BufferedImage> bdList=new ArrayList<BufferedImage>();
	public List<BufferedImage> bdList2=new ArrayList<BufferedImage>();
	public List<BufferedImage> bdList3=new ArrayList<BufferedImage>();
	public List<BufferedImage> bdList4=new ArrayList<BufferedImage>();
	public List<BufferedImage> bdList5=new ArrayList<BufferedImage>();

	// 初始化
	public ScreenRecMain(ScreenRecParams srp, JLabel jlb) throws AWTException {
		this.srp = srp;
		this.jlb = jlb;
		this.screenNum = srp.getScreenNum();
		init();
	}

	private void init() throws AWTException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		name = sdf.format(new Date());
		filePath = "screenrecord\\" + name;
		File file = new File(filePath);
		// 创建
		if (!file.exists()) {
			file.mkdirs();
		}
		rb=new Robot();
		rt=new Rectangle(0, 0, d.width, d.height);
	}

	// 线程开始
	public void start() {
		if (thread == null) {
			// 线程开始
			screenFlag = 0;
			thread = new Thread(this);
			thread.start();
		}
		if (thread2 == null) {
			// 线程开始
			screenFlag = 0;
			thread2 = new Thread(this);
			thread2.start();
		}
		if (thread3 == null) {
			// 线程开始
			screenFlag = 0;
			thread3 = new Thread(this);
			thread3.start();
		}
		if (thread4 == null) {
			// 线程开始
			screenFlag = 0;
			thread4 = new Thread(this);
			thread4.start();
		}
		if (thread5== null) {
			// 线程开始
			screenFlag = 0;
			thread5 = new Thread(this);
			thread5.start();
		}
//		if (thread6== null) {
//			// 线程开始
//			screenFlag = 0;
//			thread6 = new Thread(this);
//			thread6.start();
//		}
//		if (thread7== null) {
//			// 线程开始
//			screenFlag = 0;
//			thread7 = new Thread(this);
//			thread7.start();
//		}
//		if (thread8== null) {
//			// 线程开始
//			screenFlag = 0;
//			thread8 = new Thread(this);
//			thread8.start();
//		}
//		if (thread9== null) {
//			// 线程开始
//			screenFlag = 0;
//			thread9 = new Thread(this);
//			thread9.start();
//		}
//		if (thread10== null) {
//			// 线程开始
//			System.out.println("sacf,jhbvcljsbckjhbkvsjbdkjhbskdjvb");
//			screenFlag = 0;
//			thread10= new Thread(this);
//			thread10.start();
//		}
	}

	// 暂停
	public void suspend() {
		screenFlag = 1;
		System.out.println(screenFlag);
		// 保存记录
		jlb.setText(String.valueOf(screenNum));
	}

	// 继续
	public void continueScreenRec() {
		screenNum = Integer.parseInt(jlb.getText());
		screenFlag = 0;
	}

	// 结束
	public String[] stop() {
		if (thread != null) {
			screenFlag = 3;
			thread.stop();
			thread = null;
		}
		jlb.setText("0");
		
		return new String[]{filePath,(String.valueOf(screenNum))};
	}

	// 执行
	@Override
	public void run() {
		Thread thisThread = Thread.currentThread();
		while (true) {
			if (screenFlag == 0) {
				getShot();
			}
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
			}
		}
//		while (thisThread==thread2) {
//			if (screenFlag == 0) {
//				getShot2();
//			}
//			try {
//				Thread.sleep(1000 / (srp.getFrameNum()));
//			} catch (InterruptedException e) {
//			}
//		}
//		while (thisThread==thread3) {
//			if (screenFlag == 0) {
//				getShot3();
//			}
//			try {
//				Thread.sleep(1000 / (srp.getFrameNum()));
//			} catch (InterruptedException e) {
//			}
//		}
//		while (thisThread==thread4) {
//			if (screenFlag == 0) {
//				getShot4();
//			}
//			try {
//				Thread.sleep(1000 / (srp.getFrameNum()));
//			} catch (InterruptedException e) {
//			}
//		}
//		while (thisThread==thread5) {
//			if (screenFlag == 0) {
//				getShot5();
//			}
//			try {
//				Thread.sleep(1000 / (srp.getFrameNum()));
//			} catch (InterruptedException e) {
//			}
//		}
	}

	// 获取屏幕
	public void getShot() {
		try {
			// 获取最大截屏
			BufferedImage screenshot = (new Robot()).createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
			screenNum++;
//			bdList.add(screenshot);
			System.out.println(screenNum);
			// 生成文件名
			String fileName = String.valueOf(screenNum) + ".JPEG";
			// 将screenshot对象写入图像文件
			ImageIO.write(screenshot, "JPEG", new File(filePath + "\\" + fileName));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void getShot2() {
		try {
			// 获取最大截屏
			screenshot = rb.createScreenCapture(rt);
			screenNum++;
//			bdList2.add(screenshot);
//			System.out.println(bdList.size());
			// 生成文件名
			String fileName = String.valueOf(screenNum) + ".JPEG";
			// 将screenshot对象写入图像文件
			ImageIO.write(screenshot, "JPEG", new File(filePath + "\\" + fileName));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void getShot3() {
		try {
			// 获取最大截屏
			screenshot = rb.createScreenCapture(rt);
			screenNum++;
//			bdList3.add(screenshot);
//			System.out.println(bdList.size());
			// 生成文件名
			String fileName = String.valueOf(screenNum) + ".JPEG";
			// 将screenshot对象写入图像文件
			ImageIO.write(screenshot, "JPEG", new File(filePath + "\\" + fileName));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void getShot4() {
		try {
			// 获取最大截屏
			screenshot = rb.createScreenCapture(rt);
			screenNum++;
//			bdList4.add(screenshot);
//			System.out.println(bdList.size());
			// 生成文件名
			String fileName = String.valueOf(screenNum) + ".JPEG";
			// 将screenshot对象写入图像文件
			ImageIO.write(screenshot, "JPEG", new File(filePath + "\\" + fileName));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void getShot5() {
		try {
			// 获取最大截屏
			screenshot = rb.createScreenCapture(rt);
			screenNum++;
//			bdList5.add(screenshot);
			System.out.println(screenNum);
			// 生成文件名
			String fileName = String.valueOf(screenNum) + ".JPEG";
			// 将screenshot对象写入图像文件
			ImageIO.write(screenshot, "JPEG", new File(filePath + "\\" + fileName));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
