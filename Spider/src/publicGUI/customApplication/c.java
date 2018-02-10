package publicGUI.customApplication;

import java.io.File;

import publicGUI.customApplication.utils.CodeUtils;

public class c {
	public static void main(String[] args) {
//		String path = "E:\\";
//		File f = new File(path);
//		if (!f.exists()) {
//			return;
//		}
//		File[] fs = f.listFiles();
//		for (int i = 0; i < fs.length; i++) {
//			if (fs[i].isDirectory()) {
//				System.out.println(fs[i].getName());
//			}
//		}
		CodeUtils cu=new CodeUtils();
		cu.deleteAllFile(new File("E:\\合并"));
		File f = new File("E://合并//test.java");
		if (!f.exists()) {
			f.mkdirs();
		}
	}
}
