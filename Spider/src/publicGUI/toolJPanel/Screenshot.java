package publicGUI.toolJPanel;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.JWindow;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

//继承jwindow，无边框，与jframe同等级
public class Screenshot extends JWindow {
	public JFrame jf;
	private int startx, starty, endx, endy;
	private BufferedImage image = null;
	private BufferedImage tempImage = null;
	private BufferedImage saveImage = null;
	// 剪切板
	Clipboard clipboard;
	// 工具类
	private ToolsWindow tools = null;

	// 普通初始化
	public Screenshot(boolean bl) throws AWTException {
		init(bl);
	}

	// 无障碍初始化
	public Screenshot(JFrame jf, Boolean bl) throws AWTException {
		this.jf = jf;
		init(bl);
	}

	private void init(boolean bl) throws AWTException {
		// 若无障碍则隐藏主体jframe（最小化）
		if (bl == true) {
			jf.setExtendedState(JFrame.ICONIFIED);
		}
		this.setVisible(true);
		// 获取屏幕尺寸
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0, 0, d.width, d.height);
		// 截取最大屏幕
		Robot robot = new Robot();
		image = robot.createScreenCapture(new Rectangle(0, 0, d.width, d.height));

		//
		
		// 监听鼠标点击松开
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// 记录鼠标点击行为 坐标
				startx = e.getX();
				starty = e.getY();

				if (tools != null) {
					tools.setVisible(false);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// 鼠标松开时，显示工具栏
				if (tools == null) {
					tools = new ToolsWindow(Screenshot.this, e.getX(), e.getY());
				} else {
					tools.setLocation(e.getX(), e.getY());
				}
				tools.setVisible(true);
				tools.toFront();
			}
		});

		// 监听鼠标拖动
		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				// 鼠标拖动时，记录坐标
				endx = e.getX();
				endy = e.getY();

				Image tempImage2 = createImage(Screenshot.this.getWidth(), Screenshot.this.getHeight());
				Graphics g = tempImage2.getGraphics();
				g.drawImage(tempImage, 0, 0, null);
				int x = Math.min(startx, endx);
				int y = Math.min(starty, endy);
				int width = Math.abs(endx - startx) + 1;
				int height = Math.abs(endy - starty) + 1;
				g.setColor(Color.BLUE);
				g.drawRect(x - 1, y - 1, width + 1, height + 1);
				saveImage = image.getSubimage(x, y, width, height);
				g.drawImage(saveImage, x, y, null);
				Screenshot.this.getGraphics().drawImage(tempImage2, 0, 0, Screenshot.this);
			}
		});

	}

	// 绘制图片
	@Override
	public void paint(Graphics g) {
		RescaleOp ro = new RescaleOp(0.8f, 0, null);
		tempImage = ro.filter(image, null);
		g.drawImage(tempImage, 0, 0, this);
	}

	// 保存图片
	public void saveImage() throws IOException {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("保存");

		// 文件过滤器，用户过滤可选择文件
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG", "jpg");
		jfc.setFileFilter(filter);

		// 初始化一个默认文件
		SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddHHmmss");
		String fileName = sdf.format(new Date());
		File filePath = FileSystemView.getFileSystemView().getHomeDirectory();
		File defaultFile = new File(filePath + File.separator + fileName + ".jpg");
		jfc.setSelectedFile(defaultFile);

		int flag = jfc.showSaveDialog(jf);
		if (flag == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			String path = file.getPath();
			// 检查文件后缀，防止用户忘记输入后缀或者输入不正确的后缀
			if (!(path.endsWith(".jpg") || path.endsWith(".JPG"))) {
				path += ".jpg";
			}
			// 写入文件
			//点击一下表示截全屏
			if(saveImage==null) {
				saveImage=image;
			}
			ImageIO.write(saveImage, "jpg", new File(path));
			dispose();
		} else {
			dispose();
		}
	}

	// 复制到剪切板
	public void copyClipImage() throws IOException {
		// 获得系统剪贴板
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		//这步不太懂
		Transferable trans = new Transferable() {
			@Override
			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
				if (isDataFlavorSupported(flavor)) {
					return saveImage;
				}
				throw new UnsupportedFlavorException(flavor);
			}

			@Override
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { DataFlavor.imageFlavor };
			}

			@Override
			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return DataFlavor.imageFlavor.equals(flavor);
			}
		};
		//添加到剪切板
		clipboard.setContents(trans, null);
		dispose();
	}

	// 窗口隐藏
	public void cancel() {
		dispose();
	}
}

/*
 * 操作窗口
 */
class ToolsWindow extends JWindow {
	private Screenshot parent;
    ImageIcon copyImage=new ImageIcon("image\\copy.png");
    ImageIcon saveImage=new ImageIcon("image\\save.png");
    ImageIcon cancelImage=new ImageIcon("image\\cancel.png");
	
	public ToolsWindow(Screenshot parent, int x, int y) {
		this.parent = parent;
		this.init();
		this.setLocation(x, y);
		this.pack();
		this.setVisible(true);

	}

	private void init() {
		this.setLayout(new BorderLayout());
		JToolBar toolBar = new JToolBar("a");

		//
		JButton copyClipButton = new JButton(copyImage);
		copyClipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					parent.copyClipImage();
					dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		toolBar.add(copyClipButton);

		// 保存按钮
		JButton saveButton = new JButton(saveImage);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					parent.saveImage();
					dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		toolBar.add(saveButton);

		// 关闭按钮
		JButton closeButton = new JButton(cancelImage);
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.cancel();
				dispose();
			}
		});
		toolBar.add(closeButton);
		this.add(toolBar, BorderLayout.NORTH);
	}

}
