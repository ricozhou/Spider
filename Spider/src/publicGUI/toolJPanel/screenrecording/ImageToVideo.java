package publicGUI.toolJPanel.screenrecording;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import org.jim2mov.core.DefaultMovieInfoProvider;
import org.jim2mov.core.ImageProvider;
import org.jim2mov.core.Jim2Mov;
import org.jim2mov.core.MovieInfoProvider;
import org.jim2mov.core.MovieSaveException;
import org.jim2mov.utils.MovieUtils;

public class ImageToVideo {
	public Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

	// 生成视频
	public void getVideo(ScreenRecParams srp, String[] filePath,int sec) throws MovieSaveException {
		// 文件数组
		final File[] jpgs = new File(filePath[0] + "\\").listFiles();
		// final File[] jpgs = new
		// File("E:\\Java\\workspace\\taobaotupai13\\screenrecord\\20171128233359").listFiles();

		// 对文件名进行排序,数字越小越靠前
		Arrays.sort(jpgs, new Comparator<File>() {
			public int compare(File file1, File file2) {
				String numberName1 = file1.getName().replace(".JPEG", "");
				String numberName2 = file2.getName().replace(".JPEG", "");
				return new Integer(numberName1) - new Integer(numberName2);
			}
		});

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String name = sdf.format(new Date());
		DefaultMovieInfoProvider dmip = new DefaultMovieInfoProvider(name + ".avi");
		// 设置每秒帧数
		dmip.setFPS(sec);
		// 总帧数
		dmip.setNumberOfFrames(jpgs.length);
		dmip.setMWidth(d.width);
		dmip.setMHeight(d.height);

		new Jim2Mov(new ImageProvider() {
			public byte[] getImage(int frame) {
				try {
					// 设置压缩比
					return MovieUtils.convertImageToJPEG((jpgs[frame]), 1.0f);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		}, dmip, null).saveMovie(MovieInfoProvider.TYPE_AVI_MJPEG);
	}

	// 删除缓存文件
	public void deleteCacheFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
			file.delete();
		}
	}

}
