package publicGUI.customApplication.setDialog;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import publicGUI.customApplication.CodeJFrame;
import publicGUI.customApplication.CustomApplicationJPanel;
import publicGUI.customApplication.entity.CodeSetParams;
import publicGUI.customApplication.utils.CodeUtils;

public class DeleteAndEditDialog extends JDialog implements ActionListener {
	public JButton button1, button2;
	public JLabel jlp1;
	public JComboBox jcb1, jcb2;
	public String[] deleteInfo, deleteInfo2;
	public int status;
	public CodeUtils cu = new CodeUtils();
	public List<JButton> jbList1, jbList2;
	// 0是编程，1是jar，2是exe
	public static int appType = 0;

	public DeleteAndEditDialog(String[] deleteInfo, int status, List<JButton> jbList2, List<JButton> jbList1) {
		this.deleteInfo = deleteInfo;
		this.status = status;
		this.jbList2 = jbList2;
		this.jbList1 = jbList1;
		init();
	}

	private void init() {

		this.setLayout(null);
		jlp1 = new JLabel("*请选择应用名：");
		jlp1.setBounds(20, 20, 100, 25);

		// 0是删除1是修改
		// 删除显示所有应用，修改只能是编程类应用
		if (status == 0) {
			deleteInfo2 = new String[deleteInfo.length];
			for (int i = 0; i < deleteInfo.length; i++) {
				deleteInfo2[i] = deleteInfo[i].split("#")[1];
			}
		} else if (status == 1) {
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < deleteInfo.length; i++) {
				if ("0".equals(deleteInfo[i].split("#")[2])) {
					list.add(deleteInfo[i].split("#")[1]);
				}
			}
			// list转数组
			deleteInfo2 = new String[list.size()];
			deleteInfo2 = list.toArray(deleteInfo2);
		}

		jcb1 = new JComboBox(deleteInfo2);
		jcb1.setBounds(115, 20, 125, 25);

		button1 = new JButton("确定");
		button1.setBounds(40, 150, 80, 25);
		button2 = new JButton("取消");
		button2.setBounds(160, 150, 80, 25);
		button1.addActionListener(this);
		button2.addActionListener(this);

		this.add(jlp1);
		this.add(jcb1);
		this.add(button1);
		this.add(button2);
		// 必须放在前面设置才有效果
		ImageIcon imageIcon = null;
		if (status == 0) {
			imageIcon = new ImageIcon("image\\headImage\\delete.png");
			this.setTitle("应用删除");
		} else if (status == 1) {
			imageIcon = new ImageIcon("image\\headImage\\edit.png");
			this.setTitle("应用修改");
		}
		this.setIconImage(imageIcon.getImage());
		// 原窗口不能动(模态窗口)
		this.setModal(true);
		this.setSize(300, 250);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 监听确定按钮
		if (e.getSource().equals(button1)) {
			// 删除/编辑下标
			int index = jcb1.getSelectedIndex();
			// 根据下标获取按钮名
			String buttonName = deleteInfo2[index].split("-")[0];
			// 按钮后缀
			String bLast = buttonName.substring(buttonName.length() - 1, buttonName.length());
			String className = deleteInfo2[index].split("-")[2];
			if (status == 0) {
				// 删除相关java或class文件或者其他类型应用
				if (cu.deleteFileByClassName(className)) {
					// 更改状态
					jbList2.get(index).setVisible(false);
					CustomApplicationJPanel.isDeleteSucc = true;
					JOptionPane.showMessageDialog(null, "应用删除成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "应用删除失败！请稍后重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
				}
				// 修改配置
				try {
					cu.updateAppInfotParams(buttonName, "-", false, "-", "0");
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "应用删除失败！请稍后重试！", "提示消息", JOptionPane.WARNING_MESSAGE);
					CustomApplicationJPanel.isDeleteSucc = false;
					e1.printStackTrace();
					return;
				}
				dispose();
			} else if (status == 1) {
				if (CustomApplicationJPanel.isOpenCodeJFrame) {
					JOptionPane.showMessageDialog(null, "不能同时打开两个编程窗口！", "提示消息", JOptionPane.WARNING_MESSAGE);
					return;
				}
				// 修改
				// 先更改相关设置
				CodeSetParams codeSetParams = new CodeSetParams();
				codeSetParams.setCodeLanguage(0);
				codeSetParams.setClassName(className);
				try {
					cu.saveCodeSetParams(codeSetParams);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				// 获取代码
				String classPath = "customFunction\\bin\\" + codeSetParams.getClassName() + "\\src\\"
						+ codeSetParams.getClassName() + ".java";
				String codeStr = cu.getFileCodeInTo(classPath);
				dispose();
				// 启动编程界面
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						CodeJFrame cj = new CodeJFrame(jbList1, codeSetParams, codeStr, true, true, true, status,
								jbList2.get(Integer.parseInt(bLast) - 1), buttonName);
						CustomApplicationJPanel.isOpenCodeJFrame = true;
					}
				});

			}
		}
		// 监听取消按钮
		if (e.getSource().equals(button2)) {
			CustomApplicationJPanel.isDeleteSucc = false;
			dispose();
		}
	}

}
