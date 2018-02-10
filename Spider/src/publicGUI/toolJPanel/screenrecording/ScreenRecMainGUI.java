package publicGUI.toolJPanel.screenrecording;

import java.awt.AWTException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.jim2mov.core.MovieSaveException;

public class ScreenRecMainGUI extends JFrame implements ActionListener {
	public JFrame jf;
	public JPanel jp1, jp2;
	public JLabel jlb1, jlb2, jlb3, jlb4, jlb5, jlb6, jlb7, jlb8, jlb9, jlb10;
	public JButton button1, button2, button3, button4, button5, button6, button7;
	public JTextField tt1;
	public JFileChooser jfc1;
	public JComboBox jcb1;
	public JCheckBox jck1, jck2;
	public JProgressBar jpbProcessLoading;
	public long startJsTime, stopTime, continueTime;
	public CountThread thread;
	public SwingWorker<ReturnScreenMessage, String> sw;
	public ScreenRecMain srm;
	public ScreenRecParams srp;
	public ImageToVideo itv=new ImageToVideo();
	public static int f = 1;
	public long elapsed;

	// 初始化
	public ScreenRecMainGUI(JFrame jf) {
		this.jf = jf;
		init();
	}

	// 初始化
	private void init() {
		// 初始化面板
		jp1 = new JPanel();
		jp1.setLayout(null);

		jlb6 = new JLabel();
		jlb6.setText("0");
		jlb6.setVisible(false);
		jp1.add(jlb6);

		jlb1 = new JLabel("设置：");
		jlb1.setBounds(20, 10, 100, 25);
		jp1.add(jlb1);

		jlb2 = new JLabel("输出文件夹：");
		jlb2.setBounds(50, 30, 100, 25);
		jp1.add(jlb2);
		tt1 = new JTextField();// TextField 目录的路径
		tt1.setText("C:/");
		tt1.setEditable(false);
		button4 = new JButton("文件夹");// 选择
		jfc1 = new JFileChooser();// 文件选择器
		jfc1.setCurrentDirectory(new File("C:/"));// 文件选择器的初始目录定为c盘
		tt1.setBounds(120, 30, 180, 25);
		button4.setBounds(310, 30, 65, 25);
		button4.addActionListener(this); // 添加事件处理
		jp1.add(tt1);
		jp1.add(button4);
		jp1.add(jfc1);

		jlb3 = new JLabel("设置帧数：");
		jlb3.setBounds(50, 60, 100, 25);
		jp1.add(jlb3);
		String[] num = { "25", "35", "45", "55", "65", "75" };
		jcb1 = new JComboBox(num);
		jcb1.setBounds(120, 60, 70, 25);
		jp1.add(jcb1);

		jck1 = new JCheckBox("是否录制声音");
		jck1.setSelected(false);
		jck1.setBounds(50, 90, 120, 25);
		jp1.add(jck1);

		jck2 = new JCheckBox("是否无障碍录屏");
		jck2.setSelected(false);
		jck2.setBounds(200, 90, 120, 25);
		jp1.add(jck2);

		// 进度条
		jpbProcessLoading = new JProgressBar();
		jpbProcessLoading.setStringPainted(true); // 呈现字符串
		jpbProcessLoading.setBounds(80, 120, 220, 25);
		jpbProcessLoading.setVisible(false);
		jp1.add(jpbProcessLoading);

		// 设置显示时间（秒表）
		jlb4 = new JLabel();
		jlb5 = new JLabel();
		jlb4.setBounds(50, 150, 150, 25);
		jlb5.setBounds(190, 150, 150, 25);
		jp1.add(jlb4);
		jp1.add(jlb5);

		button1 = new JButton("开始");
		button1.setBounds(40, 210, 60, 30);
		button1.addActionListener(this);
		jp1.add(button1);
		button2 = new JButton("暂停");
		button2.setBounds(130, 210, 60, 30);
		button2.addActionListener(this);
		button2.setEnabled(false);
		jp1.add(button2);
		button5 = new JButton("继续");
		button5.setBounds(220, 210, 60, 30);
		button5.addActionListener(this);
		button5.setEnabled(false);
		jp1.add(button5);
		button3 = new JButton("停止");
		button3.setBounds(300, 210, 60, 30);
		button3.addActionListener(this);
		button3.setEnabled(false);
		jp1.add(button3);
		// button4 = new JButton("播放");

		this.add(jp1);
		this.setTitle("无障碍录屏");
		this.setSize(400, 400);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ImageIcon imageIcon = new ImageIcon("image\\headImage\\recording.png");
		this.setIconImage(imageIcon.getImage());
		// 监听界面关闭事件
		this.addWindowListener(new WindowAdapter() {
			// 当关闭时
			public void windowClosing(WindowEvent e) {
				if (srm != null) {
					srm.stop();
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 监听选择文件夹按钮
		if (e.getSource().equals(button4)) {// 判断触发方法的按钮是哪个
			jfc1.setFileSelectionMode(1);// 设定只能选择到文件夹
			int state = jfc1.showOpenDialog(this);// 此句是打开文件选择器界面的触发语句
			if (state == 1) {
				return;
			} else {
				File f = jfc1.getSelectedFile();// f为选择到的目录
				tt1.setText(f.getAbsolutePath());
			}
		}

		// 开始
		if (e.getSource().equals(button1)) {
			// 获取参数
			srp = new ScreenRecParams();
			// 文件夹路径
			srp.setFilePath(tt1.getText().trim().toString());
			// 设置帧数
			srp.setFrameNum(Integer.parseInt(jcb1.getSelectedItem().toString()));
			// 是否有声音
			srp.setRecordVoice(jck1.isSelected());
			// 是否无障碍录屏
			srp.setBarrierFreeScreen(jck2.isSelected());
			// 张数
			srp.setScreenNum(Integer.parseInt(jlb6.getText().trim().toString()));

			// 接入计时器（秒表）
			startJsTime = System.currentTimeMillis();
			jlb4.setText("录屏已开始！已进行：");
			thread = new CountThread();
			thread.start();

			// 设置进度条
			jpbProcessLoading.setVisible(true);
			jpbProcessLoading.setIndeterminate(true); // 设置进度条为不确定模式,默认为确定模式
			jpbProcessLoading.setString("正在录屏中...");

			//是否无障碍
			if(srp.isBarrierFreeScreen()) {
				jf.setExtendedState(JFrame.ICONIFIED);
				this.setExtendedState(JFrame.ICONIFIED);
			}
			
			// 较复杂，耗时线程swingworker
			// sw = new SwingWorker<ReturnScreenMessage, String>() {
			//
			// // 此方法处理耗时任务
			// @Override
			// protected ReturnScreenMessage doInBackground() throws Exception {
			//
			//
			//
			// return null;
			// }
			//
			// // 此方法是耗时任务完成后的操作
			// protected void done() {
			//
			// }
			// };
			// sw.execute();

			try {
				srm = new ScreenRecMain(srp, jlb6);
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
			srm.start();
			button1.setEnabled(false);
			button2.setEnabled(true);
			button3.setEnabled(true);
			button5.setEnabled(false);
		}

		// 暂停
		if (e.getSource().equals(button2)) {
			srm.suspend();
			button1.setEnabled(false);
			button2.setEnabled(false);
			button3.setEnabled(true);
			button5.setEnabled(true);
			jpbProcessLoading.setVisible(true);
			jpbProcessLoading.setIndeterminate(false);
			jpbProcessLoading.setString("录屏暂停");
			thread.stop();
			stopTime = System.currentTimeMillis();
		}

		// 继续
		if (e.getSource().equals(button5)) {
			srm.continueScreenRec();
			button1.setEnabled(false);
			button2.setEnabled(true);
			button3.setEnabled(true);
			button5.setEnabled(false);
			jpbProcessLoading.setVisible(true);
			jpbProcessLoading.setIndeterminate(true);
			jpbProcessLoading.setString("正在录屏中...");
			continueTime = System.currentTimeMillis();
			startJsTime = (continueTime - stopTime) + startJsTime;
			thread = new CountThread();
			thread.start();
		}
		// 停止
		if (e.getSource().equals(button3)) {
			String[] filePath = srm.stop();
			button1.setEnabled(false);
			button2.setEnabled(false);
			button3.setEnabled(false);
			button5.setEnabled(false);
			jpbProcessLoading.setVisible(true);
			jpbProcessLoading.setIndeterminate(false);
			jpbProcessLoading.setString("录屏结束！正在生成视频！请稍等！");
			thread.stop();

			
			// 较复杂，耗时线程swingworker
			sw = new SwingWorker<ReturnScreenMessage, String>() {

				// 此方法处理耗时任务
				@Override
				protected ReturnScreenMessage doInBackground() throws Exception {
					// 生成视频
					try {
						int sec=(int) (Integer.parseInt(filePath[1])/(elapsed/1000));
						itv.getVideo(srp, filePath,sec);
					} catch (MovieSaveException e1) {
						e1.printStackTrace();
					}
					return null;
				}

				// 此方法是耗时任务完成后的操作
				protected void done() {
					jpbProcessLoading.setString("已生成！");
					JOptionPane.showMessageDialog(null, "已生成！", "提示消息", JOptionPane.WARNING_MESSAGE);
					button1.setEnabled(true);
					//删除缓存文件
					itv.deleteCacheFile(filePath[0]);
				}
			};
			sw.execute();
		}

	}

	// 计时线程
	private class CountThread extends Thread {
		private CountThread() {
			setDaemon(true);
		}

		@Override
		public void run() {
			while (true) {
				elapsed = System.currentTimeMillis() - startJsTime;
				jlb5.setText(format(elapsed));
				try {
					sleep(1); // 1毫秒更新一次显示
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		}

		// 将毫秒数格式化
		private String format(long elapsed) {
			int hour, minute, second, milli;
			milli = (int) (elapsed % 1000);
			elapsed = elapsed / 1000;
			second = (int) (elapsed % 60);
			elapsed = elapsed / 60;
			minute = (int) (elapsed % 60);
			elapsed = elapsed / 60;
			hour = (int) (elapsed % 60);
			return String.format("%02d:%02d:%02d %03d", hour, minute, second, milli);
		}
	}

}
