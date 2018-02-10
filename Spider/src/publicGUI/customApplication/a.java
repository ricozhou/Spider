package publicGUI.customApplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class a extends JFrame implements ActionListener {
	JButton jb;

	public static void main(String[] args) {
		new a().init();
		// getSome();
	}

	// private static void getSome() {
	// String str = "_#_#_";
	// String[] s = str.split("#");
	// System.out.println(s.length);
	// System.out.println(s[1]);
	// System.out.println(s[2]);
	// }

	private void init() {
		JFrame jf = new JFrame("test1");
		JPanel jp = new JPanel();
		jp.setLayout(null);
		jb = new JButton("test");
		jb.setBounds(20, 20, 80, 30);
		jb.addActionListener(this);
		jp.add(jb);
		jf.add(jp);
		jf.setSize(200, 200);
		jf.setVisible(true);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jb)) {
			// 1. 创建一封邮件
			Properties props = new Properties(); // 用于连接邮件服务器的参数配置（发送邮件时才需要用到）
			Session session = Session.getInstance(props); // 根据参数配置，创建会话对象（为了发送邮件准备的）
			MimeMessage message = new MimeMessage(session); // 创建邮件对象

			/*
			 * 也可以根据已有的eml邮件文件创建 MimeMessage 对象 MimeMessage message = new
			 * MimeMessage(session, new FileInputStream("MyEmail.eml"));
			 */

			// 2. From: 发件人
			// 其中 InternetAddress 的三个参数分别为: 邮箱, 显示的昵称(只用于显示, 没有特别的要求), 昵称的字符集编码
			// 真正要发送时, 邮箱必须是真实有效的邮箱。
			try {
				message.setFrom(new InternetAddress("15606218315@163.com", "USER_AA", "UTF-8"));
				// 3. To: 收件人
				message.setRecipient(MimeMessage.RecipientType.TO,
						new InternetAddress("2320095772@qq.com", "USER_CC", "UTF-8"));

				// 4. Subject: 邮件主题
				message.setSubject("TEST邮件主题", "UTF-8");

				// 5. Content: 邮件正文（可以使用html标签）
				message.setContent("TEST这是邮件正文。。。", "text/html;charset=UTF-8");

				// 6. 设置显示的发件时间
				message.setSentDate(new Date());

				// 7. 保存前面的设置
				message.saveChanges();

				// 8. 将该邮件保存到本地
				OutputStream out = new FileOutputStream("MyEmail.eml");
				message.writeTo(out);
				out.flush();
				out.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			System.out.println(11);
			System.out.println(1133);
			System.out.println(12321);
			JOptionPane.showMessageDialog(null, "Hello Word", "提示消息", JOptionPane.WARNING_MESSAGE);
		}
	}
}