package publicGUI.toolJPanel;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import login.entity.UserInfo;
import publicGUI.entity.PassParameter;
import publicGUI.entity.SetParams;
import publicGUI.toolJPanel.OCRRead.OCRGui;
import publicGUI.toolJPanel.QRCode.QRMainGUI;
import publicGUI.toolJPanel.cubecreateimage.CubeCreateMain;
import publicGUI.toolJPanel.screenrecording.ScreenRecMainGUI;
import publicGUI.toolJPanel.sendmail.MailMainGui;
import publicGUI.utils.GetSetProperties;
import taobaojud.prosceniumgui.maingame.TaoBaoMainUI;

public class ToolJPanel extends JPanel implements ActionListener {
	public JLabel jlp1, jlp2, jlp3, jlp4;
	public JButton button1, button2, button3, button4, button5, button6, button7, button8, button9, button10, button11,
			button12;
	public SetParams setParams;
	public GetSetProperties gsp = new GetSetProperties();
	public UserInfo userInfo;
	public JFrame jf;
	public PassParameter passParameter;

	public ToolJPanel(PassParameter passParameter, JFrame jf) throws IOException {
		// 将之前窗口对象传递过来
		this.jf = jf;
		this.passParameter = passParameter;
		init();
	}

	private void init() {
		userInfo = passParameter.getUserInfo();
		jlp1 = new JLabel("计算器：");
		jlp1.setBounds(30, 30, 100, 25);
		button1 = new JButton("标准计算器");
		button1.setBounds(80, 50, 150, 35);
		button1.addActionListener(this);
		button2 = new JButton("科学计算器");
		button2.setBounds(80, 85, 150, 35);
		button2.addActionListener(this);
		button3 = new JButton("高级计算器");
		button3.setBounds(80, 120, 150, 35);
		button3.addActionListener(this);

		jlp2 = new JLabel("");
		jlp2.setBounds(255, 30, 100, 25);
		button4 = new JButton("普通版截图");
		button4.setBounds(295, 50, 150, 35);
		button4.addActionListener(this);
		button5 = new JButton("无障碍截图");
		button5.setBounds(295, 85, 150, 35);
		button5.addActionListener(this);
		button6 = new JButton("魔方拼图器");
		button6.setBounds(295, 120, 150, 35);
		button6.addActionListener(this);

		jlp3 = new JLabel("其它：");
		jlp3.setBounds(30, 170, 100, 25);
		button7 = new JButton("普通小闹钟");
		button7.setBounds(80, 190, 150, 35);
		button7.addActionListener(this);
		button8 = new JButton("高级小闹钟");
		button8.setBounds(80, 225, 150, 35);
		button8.addActionListener(this);
		button12 = new JButton("发送小邮件");
		button12.setBounds(80, 260, 150, 35);
		button12.addActionListener(this);

		// jlp4=new JLabel("其它：");
		// jlp4.setBounds(255, 170, 100, 25);
		button9 = new JButton("二维码生成");
		button9.setBounds(295, 190, 150, 35);
		button9.addActionListener(this);
		button10 = new JButton("无障碍录屏");
		button10.setBounds(295, 225, 150, 35);
		button10.addActionListener(this);
		button11 = new JButton("OCR识别文字");
		button11.setBounds(295, 260, 150, 35);
		button11.addActionListener(this);

		this.add(jlp1);
		this.add(button1);
		this.add(button2);
		this.add(button3);
		this.add(jlp2);
		this.add(button4);
		this.add(button5);
		this.add(button6);
		this.add(jlp3);
		this.add(button7);
		this.add(button8);
		// this.add(jlp4);
		this.add(button9);
		this.add(button10);
		this.add(button11);
		this.add(button12);
		this.setLayout(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 标准计算器
		if (e.getSource().equals(button1)) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						StandardCalculator standardCalculator = new StandardCalculator();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		// 科学计算器
		if (e.getSource().equals(button2)) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						ScienceCalculator scienceCalculator = new ScienceCalculator();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		// 高级计算器
		if (e.getSource().equals(button3)) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						AdvancedCalculator advancedCalculator = new AdvancedCalculator();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		// 普通小截图
		if (e.getSource().equals(button4)) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						Screenshot commonScreenshot = new Screenshot(false);
						commonScreenshot.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		// 无障碍截图
		if (e.getSource().equals(button5)) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						Screenshot accessibleScreenshot = new Screenshot(jf, true);
						accessibleScreenshot.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		// 魔方图案生成器
		if (e.getSource().equals(button6)) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						CubeCreateMain ccm = new CubeCreateMain();
						ccm.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		// 普通小闹钟
		if (e.getSource().equals(button7)) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						CommonAlarmClock commonAlarmClock = new CommonAlarmClock();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		// 高级小闹钟
		if (e.getSource().equals(button8)) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						AdvancedAlarmClock advancedAlarmClock = new AdvancedAlarmClock();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		// 二维码生成和解析
		if (e.getSource().equals(button9)) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						QRMainGUI QRmg = new QRMainGUI();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		// 无障碍录屏
		if (e.getSource().equals(button10)) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						ScreenRecMainGUI srm = new ScreenRecMainGUI(jf);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		// ocr识别文字
		if (e.getSource().equals(button11)) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						OCRGui ocrg = new OCRGui();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		// mail
		if (e.getSource().equals(button12)) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						MailMainGui mmg = new MailMainGui();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
}
