package publicGUI.setJPanel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import publicGUI.toolJPanel.QRCode.QRUtils;
import publicGUI.utils.SaveSet;

public class UserImageUpload extends JDialog implements ActionListener {
	public JButton button1, button2, button3;
	public JPanel jp1;
	public JLabel jlb1;
	public JFileChooser chooseFile;
	public BufferedImage bi1;
	public QRUtils qru = new QRUtils();
	public SaveSet ss = new SaveSet();
	public String fname;

	static int i = 0;

	public UserImageUpload() {
		init();
	}

	private void init() {
		setLayout(null);
		button1 = new JButton("选择头像");
		button1.setBounds(80, 20, 100, 30);
		button1.addActionListener(this);
		button2 = new JButton("打开相机");
		button2.setBounds(220, 20, 100, 30);
		button2.addActionListener(this);
		button3 = new JButton("保存");
		button3.setEnabled(false);
		button3.setBounds(150, 405, 100, 30);
		button3.addActionListener(this);

		jlb1 = new JLabel();
		jlb1.setBounds(20, 60, 340, 340);
		this.add(jlb1);

		this.add(button1);
		this.add(button2);
		this.add(button3);
		ImageIcon imageIcon = new ImageIcon("image\\headImage\\headImage.png");
		this.setIconImage(imageIcon.getImage());
		// 原窗口不能动(模态窗口)
		this.setModal(true);
		this.setTitle("上传头像");
		this.setSize(400, 480);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 选择头像
		if (e.getSource().equals(button1)) {
			chooseFile = new JFileChooser();
			// 文件过滤
			// 只能选择文件
			chooseFile.setFileSelectionMode(0);
			chooseFile.setFileFilter(new FileCanChoose());
			int returnVal = chooseFile.showOpenDialog(this);
			if (returnVal == chooseFile.APPROVE_OPTION) {
				File f = chooseFile.getSelectedFile();
				fname = f.getAbsolutePath();
				// 缩放图片
				bi1 = qru.zoomImage(fname, 340, 340);
				if (bi1 != null) {
					if (i != 0) {
						// 移除之前的图片
						jlb1.setText("");
					}
					i++;
					jlb1.setIcon(new ImageIcon(bi1));
					// 设置图片居中显示
					jlb1.setHorizontalAlignment(JLabel.CENTER);
					button3.setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(null, "读取图片失败！请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
				}
			}
		}

		// 监听保存
		if (e.getSource().equals(button3)) {
			// 先修改原文件名字
			if (ss.reNameFile("image//image.png", "image//image2.png")) {
				// 保存现文件（名字还是image）
				if (bi1 != null) {
					if (ss.saveImage(fname, bi1)) {
						// 如果成功则删除原文件，不成功则将名字改回来
						File oldFile = new File("image//image2.png");
						oldFile.delete();
						JOptionPane.showMessageDialog(null, "修改头像成功！重启生效！", "提示消息", JOptionPane.WARNING_MESSAGE);
						dispose();
					} else {
						ss.reNameFile("image//image2.png", "image//image.png");
						JOptionPane.showMessageDialog(null, "修改头像失败！请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
					}
				}

			} else {
				JOptionPane.showMessageDialog(null, "修改头像失败！请重试！", "提示消息", JOptionPane.WARNING_MESSAGE);

			}
		}
	}
}

// 文件过滤器
class FileCanChoose extends FileFilter {
	public boolean accept(File file) {
		String name = file.getName();
		return (name.toLowerCase().endsWith(".gif") || name.toLowerCase().endsWith(".jpg")
				|| name.toLowerCase().endsWith(".bmp") || name.toLowerCase().endsWith(".png")
				|| name.toLowerCase().endsWith(".jpeg")||file.isDirectory());
	}

	public String getDescription() {
		return "图片文件：*.gif、 *.jpg、 *.bmp、 *.png、 *.jpeg";
	}
}
