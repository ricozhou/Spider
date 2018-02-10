package publicGUI.toolJPanel.QRCode;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

public class QRMainGUI extends JFrame implements ActionListener {
	public JPanel jp1, jp2;
	public JLabel jlb1, jlb2, jlb3, jlb4, jlb5, jlb6, jlb7, jlb8, jlb9, jlb10;
	public JButton button1, button2, button3, button4, button5, button6, button7;
	public JTextArea jta1, jta2;
	public JTextField tt1, tt2, tt3, tt4;
	public JFileChooser jfc1, jfc2, jfc3, jfc4;
	public JRadioButton jrb1, jrb2, jrb3;
	public ButtonGroup bg1;
	public JCheckBox jcbox1, jcbox2;
	public JTabbedPane jtp = new JTabbedPane(JTabbedPane.TOP);
	public QRUtils qru = new QRUtils();
	public QRCreateMain qrcm = new QRCreateMain();
	public BufferedImage bi1, bi2;
	public static int numImage = 0, numImage2 = 0;
	public File file;

	// 构造方法
	public QRMainGUI() {
		initJFrame();
		init();
	}

	// 初始化窗口
	private void initJFrame() {
		// 生成二维码需要的面板
		jp1 = new JPanel();
		// 解析二维码需要的面板
		jp2 = new JPanel();
		jp1.setLayout(null);
		jp2.setLayout(null);

		jlb3 = new JLabel("输出文件夹：");
		jlb3.setBounds(20, 10, 100, 25);
		jp1.add(jlb3);
		tt2 = new JTextField();// TextField 目录的路径
		tt2.setText("C:/");
		tt2.setEditable(false);
		button1 = new JButton("文件夹");// 选择
		jfc1 = new JFileChooser();// 文件选择器
		jfc1.setCurrentDirectory(new File("C:/"));// 文件选择器的初始目录定为c盘
		tt2.setBounds(115, 10, 180, 25);
		button1.setBounds(300, 10, 65, 25);
		button1.addActionListener(this); // 添加事件处理
		jp1.add(tt2);
		jp1.add(button1);
		jp1.add(jfc1);

		jlb1 = new JLabel("请选择类型：");
		jlb1.setBounds(20, 40, 100, 25);
		jp1.add(jlb1);
		jrb1 = new JRadioButton("链接");
		jrb1.setBounds(115, 40, 60, 25);
		jrb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jrb1.isSelected()) {
					tt1.setVisible(true);
					jta1.setVisible(false);
				} else {
					tt1.setVisible(false);
					jta1.setVisible(true);
				}
			}
		});
		jp1.add(jrb1);
		jrb2 = new JRadioButton("文本");
		jrb2.setBounds(220, 40, 60, 25);
		jrb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jrb2.isSelected()) {
					tt1.setVisible(false);
					jta1.setVisible(true);
				} else {
					tt1.setVisible(true);
					jta1.setVisible(false);
				}
			}
		});
		jp1.add(jrb2);
		bg1 = new ButtonGroup();
		bg1.add(jrb1);
		bg1.add(jrb2);
		jrb1.setSelected(true);

		tt3 = new JTextField();// TextField 目录的路径
		tt3.setText("C:/");
		tt3.setEditable(false);
		// tt3.setVisible(false);
		button2 = new JButton("文件夹");// 选择
		// button2.setVisible(false);
		jfc2 = new JFileChooser();// 文件选择器
		jfc2.setCurrentDirectory(new File("C:/"));// 文件选择器的初始目录定为c盘
		tt3.setBounds(115, 70, 180, 25);
		button2.setBounds(300, 70, 65, 25);
		button2.addActionListener(this); // 添加事件处理
		jp1.add(tt3);
		jp1.add(button2);
		jp1.add(jfc2);
		tt3.setEnabled(false);
		button2.setEnabled(false);

		jcbox1 = new JCheckBox("插入图：");
		jcbox1.setBounds(20, 70, 100, 25);
		jcbox1.setSelected(false);
		jcbox1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jcbox1.isSelected()) {
					tt3.setEnabled(true);
					button2.setEnabled(true);
				} else {
					tt3.setEnabled(false);
					button2.setEnabled(false);
				}
			}
		});
		jp1.add(jcbox1);

		jlb2 = new JLabel("请输入信息：");
		jlb2.setBounds(20, 100, 100, 25);
		jp1.add(jlb2);
		tt1 = new JTextField();
		tt1.setBounds(115, 100, 250, 25);
		jp1.add(tt1);
		jta1 = new JTextArea();
		jta1.setBounds(115, 100, 250, 100);
		jp1.add(jta1);
		jta1.setVisible(false);

		button3 = new JButton("生成");
		button3.setBounds(100, 510, 70, 25);
		button3.addActionListener(this);
		jp1.add(button3);
		button4 = new JButton("保存");
		button4.setBounds(230, 510, 70, 25);
		button4.addActionListener(this);
		button4.setEnabled(false);
		jp1.add(button4);

		jlb2 = new JLabel("打开二维码：");
		jlb2.setBounds(20, 10, 100, 25);
		jp2.add(jlb2);
		tt4 = new JTextField();// TextField 目录的路径
		tt4.setText("C:/");
		tt4.setEditable(false);
		button5 = new JButton("文件夹");// 选择
		jfc3 = new JFileChooser();// 文件选择器
		jfc3.setCurrentDirectory(new File("C:/"));// 文件选择器的初始目录定为c盘
		tt4.setBounds(115, 10, 180, 25);
		button5.setBounds(300, 10, 65, 25);
		button5.addActionListener(this); // 添加事件处理
		jp2.add(tt4);
		jp2.add(button5);
		jp2.add(jfc3);

		jlb6 = new JLabel();
		jlb6.setBounds(49, 40, 300, 300);
		jp2.add(jlb6);

		jlb7 = new JLabel("二维码信息：");
		jlb7.setBounds(20, 350, 100, 25);
		jp2.add(jlb7);
		jlb7.setVisible(false);

		jta2 = new JTextArea();
		jta2.setBounds(115, 350, 250, 150);
		jp2.add(jta2);
		jta2.setEditable(false);
		jta2.setVisible(false);

		button6 = new JButton("开始解析");
		button6.setBounds(150, 510, 80, 25);
		button6.addActionListener(this);
		jp2.add(button6);
		button6.setEnabled(false);

		button7 = new JButton("复制到剪切板");
		button7.setBounds(240, 510, 125, 25);
		button7.addActionListener(this);
		jp2.add(button7);
		button7.setVisible(false);

		this.add(jtp);
		jtp.add("生成二维码", jp1);
		jtp.add("解析二维码", jp2);

		this.setTitle("生成和解析二维码");
		this.setSize(400, 600);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ImageIcon imageIcon = new ImageIcon("image\\headImage\\QRCode.jpg");
		this.setIconImage(imageIcon.getImage());
	}

	private void init() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 监听选择文件夹按钮
		if (e.getSource().equals(button1)) {// 判断触发方法的按钮是哪个
			jfc1.setFileSelectionMode(1);// 设定只能选择到文件夹
			int state = jfc1.showOpenDialog(this);// 此句是打开文件选择器界面的触发语句
			if (state == 1) {
				return;
			} else {
				File f = jfc1.getSelectedFile();// f为选择到的目录
				tt2.setText(f.getAbsolutePath());
			}
		}

		// 监听选择文件夹按钮
		if (e.getSource().equals(button5)) {// 判断触发方法的按钮是哪个
			jfc3.setFileSelectionMode(0);// 设定选择到文件
			jfc3.setFileFilter(new FileCanChoose());
			int state = jfc3.showOpenDialog(this);// 此句是打开文件选择器界面的触发语句
			if (state == 1) {
				return;
			} else {
				file = jfc3.getSelectedFile();// f为选择到的文件
				tt4.setText(file.getAbsolutePath());
				// 缩放图片
				bi2 = qru.zoomImage(file.getAbsolutePath(), 300, 300);
				if (bi2 != null) {
					if (numImage2 != 0) {
						// 移除之前的图片
						jlb6.setText("");
					}
					numImage2++;
					jlb6.setIcon(new ImageIcon(bi2));
					button6.setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(null, "读取图片失败！请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
				}
			}
		}

		// 解析二维码
		if (e.getSource().equals(button6)) {
			String returnMeg = qrcm.analyQR(file);
			if (returnMeg != null) {
				jlb7.setVisible(true);
				jta2.setVisible(true);
				jta2.setText(returnMeg);
				button7.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(null, "解析失败！请检查是否为二维码或重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
			}
		}

		if (e.getSource().equals(button7)) {
			// 获取剪切板
			Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
			Transferable qrtext = new StringSelection(jta2.getText().trim().toString());
			clip.setContents(qrtext, null);
		}

		// 监听选择文件按钮
		if (e.getSource().equals(button2)) {// 判断触发方法的按钮是哪个
			jfc2.setFileSelectionMode(0);// 设定选择到文件
			jfc2.setFileFilter(new FileCanChoose());
			int state = jfc2.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
			if (state == 1) {
				return;
			} else {
				File f = jfc2.getSelectedFile();// f为选择到的文件
				tt3.setText(f.getAbsolutePath());
			}
		}

		// 监听生成按钮
		if (e.getSource().equals(button3)) {
			// 获取参数
			QRCreateParams qrParams = getQRParams();
			// 校验参数
			if (qru.checkQRCreate(qrParams)) {
				try {
					bi1 = qrcm.createImage(qrParams);
					if (bi1 == null) {
						JOptionPane.showMessageDialog(null, "生成失败！请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
					}
					if (numImage != 0) {
						// 移除之前的图片
						jp1.remove(jlb5);
					}
					numImage++;
					jlb5 = new JLabel();
					jlb5.setBounds(49, 205, 301, 301);
					jp1.add(jlb5);
					jlb5.setIcon(new ImageIcon(bi1));
					button4.setEnabled(true);
				} catch (IOException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "生成失败！请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "空或非法字符！请重新输入！", "提示消息", JOptionPane.WARNING_MESSAGE);
				tt1.setText("");
				jta1.setText("");
			}

		}

		// 监听保存
		if (e.getSource().equals(button4)) {
			// 获取参数
			QRCreateParams qrParams = getQRParams();
			if (bi1 != null) {
				if (qrcm.saveQRImage(qrParams, bi1)) {
					JOptionPane.showMessageDialog(null, "保存成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "保存失败！请稍后重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "图片为空！请重新生成！", "提示消息", JOptionPane.WARNING_MESSAGE);
			}
		}

	}

	// 获取参数
	public QRCreateParams getQRParams() {
		QRCreateParams qrParams = new QRCreateParams();
		// 获取存储路径
		qrParams.setFilePath(tt2.getText());
		// 是否插入图片
		qrParams.setInsertImage(jcbox1.isSelected());
		// 插入图片路径
		if (jcbox1.isSelected()) {
			qrParams.setInsertImagePath(tt3.getText());
		} else {
			qrParams.setInsertImagePath("");
		}
		// 获取文本
		if (jrb1.isSelected()) {
			qrParams.setLinkOrText(tt1.getText().trim().toString());
		} else {
			qrParams.setLinkOrText(jta1.getText().trim().toString());

		}
		return qrParams;
	}

	// 文件过滤器
	class FileCanChoose extends FileFilter {
		public boolean accept(File file) {
			String name = file.getName();
			return (name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".bmp")
					|| name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpeg")
					|| file.isDirectory());
		}

		@Override
		public String getDescription() {
			return "图片文件：*.jpg、 *.bmp、 *.png、 *.jpeg";
		}
	}

}
