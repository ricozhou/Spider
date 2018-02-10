package publicGUI.toolJPanel.cubecreateimage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

//魔方图案生成器主类
public class CubeCreateMain extends JFrame implements ActionListener {
	public JFrame jf;
	public JPanel jp1;
	public JLabel jlb1, jlb2, jlb3, jlb4, jlb5, jlb6, jlb7, jlb8, jlb9, jlb10;
	public JButton button1, button2, button3, button4, button5, button6, button7;
	public JTextField tt1, tt2;
	public JComboBox jcb1, jcb2, jcb3;

	// 初始化
	public CubeCreateMain() {
		init();
	}

	// 初始化
	private void init() {
		// 初始化面板
		jp1 = new JPanel();
		jp1.setLayout(null);

		// 基本设置
		jlb1 = new JLabel("基本设置：");
		jlb1.setBounds(30, 15, 80, 25);
		jp1.add(jlb1);

		jlb2 = new JLabel("三阶魔方不超过（个）：");
		jlb2.setBounds(100, 15, 140, 25);
		jp1.add(jlb2);
		tt1 = new JTextField();
		tt1.setBounds(240, 15, 60, 25);
		jp1.add(tt1);

		jlb3 = new JLabel("生成文字：");
		jlb3.setBounds(30, 95, 80, 25);
		jp1.add(jlb3);
		jlb4 = new JLabel("输入文字不超过（个）：");
		jlb4.setBounds(100, 95, 140, 25);
		jp1.add(jlb4);
		tt2 = new JTextField();
		tt2.setBounds(240, 95, 200, 25);
		jp1.add(tt2);

		jlb5 = new JLabel("字号：");
		jlb5.setBounds(100, 125, 60, 25);
		jp1.add(jlb5);

		String[] typeSize = { "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "65", "70", "75",
				"80", "85", "90" };
		jcb1 = new JComboBox(typeSize);
		jcb1.setBounds(140, 125, 60, 25);
		jp1.add(jcb1);

		jlb6 = new JLabel("字体：");
		jlb6.setBounds(220, 125, 60, 25);
		jp1.add(jlb6);

		String[] font = { "楷体", "宋体", "黑体", "仿宋", "毛体" };
		jcb2 = new JComboBox(font);
		jcb2.setBounds(260, 125, 60, 25);
		jp1.add(jcb2);

		jlb7 = new JLabel("颜色：");
		jlb7.setBounds(340, 125, 60, 25);
		jp1.add(jlb7);

		String[] colour = { "黑色", "红色", "白色", "蓝色", "绿色", "橙色", "黄色" };
		jcb3 = new JComboBox(colour);
		jcb3.setBounds(380, 125, 60, 25);
		jp1.add(jcb3);

		button1 = new JButton("选择图片");
		button1.setBounds(30, 160, 80, 30);
		button1.addActionListener(this);
		jp1.add(button1);

		button2 = new JButton("生成图片");
		button2.setBounds(150, 160, 80, 30);
		button2.addActionListener(this);
		jp1.add(button2);

		button3 = new JButton("另存图片");
		button3.setBounds(270, 160, 80, 30);
		button3.addActionListener(this);
		jp1.add(button3);

		button4 = new JButton("下载文档");
		button4.setBounds(390, 160, 80, 30);
		button4.addActionListener(this);
		jp1.add(button4);

		this.add(jp1);
		this.setTitle("魔方拼图器");
		this.setSize(500, 700);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ImageIcon imageIcon = new ImageIcon("image\\headImage\\cube.jpg");
		this.setIconImage(imageIcon.getImage());
		// 监听界面关闭事件
		this.addWindowListener(new WindowAdapter() {
			// 当关闭时
			public void windowClosing(WindowEvent e) {

			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
