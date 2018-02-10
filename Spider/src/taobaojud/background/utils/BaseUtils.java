package taobaojud.background.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class BaseUtils {
	// 修改状态为汉字
	public String turnSaleStatus(String saleStatus) {
		String newSaleStatus;
		if ("todo".equals(saleStatus)) {
			newSaleStatus = "即将开始";
		} else if ("done".equals(saleStatus)) {
			newSaleStatus = "已结束";
		} else if ("failure".equals(saleStatus)) {
			newSaleStatus = "已流拍";
		} else if ("doing".equals(saleStatus)) {
			newSaleStatus = "正在进行";
		} else if ("break".equals(saleStatus)) {
			newSaleStatus = "中止";
		} else if ("revocation".equals(saleStatus)) {
			newSaleStatus = "撤回";
		} else {
			newSaleStatus = "未知";
		}
		return newSaleStatus;
	}

	// 转换价格以万为单位
	public String turnPrice(String price) {
		if (price == null) {
			return "";
		}
		if (price.indexOf(".") != -1) {
			price = price.substring(0, price.indexOf("."));
		}
		if (price.length() > 4) {
			price = price.substring(0, price.length() - 4) + "." + price.substring(price.length() - 4);
		}
		return price;
	}

	// 转换时间
	public String turnDate(String datee) {
		if (datee == null) {
			return "";
		}
		long date = Long.parseLong(datee);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return df.format(date);
	}

	public String turnDate2() {
		DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		String date = df.format(new Date());
		return date;
	}

	// // 获取以省查询地址
	// public String getProvinceCityUrl(String province, String city, String bool)
	// throws FailingHttpStatusCodeException, MalformedURLException, IOException {
	// String oriUrl =
	// "https://sf.taobao.com/item_list.htm?spm=a213w.3064813.a214dqe.5.UHrdif&category=50025970";
	// String url;
	// String ProUrl;
	// // 模拟浏览器操作
	// // 创建WebClient
	// WebClient webClient = new WebClient(BrowserVersion.CHROME);
	// // 关闭css代码功能
	// webClient.getOptions().setThrowExceptionOnScriptError(false);
	// webClient.getOptions().setCssEnabled(false);
	// // 如若有可能找不到文件js则加上这句代码
	// webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
	// // 获取网页html
	// HtmlPage page = webClient.getPage(oriUrl);
	// HtmlPage page2;
	// //获取初始页面所有的a标签
	// List<DomElement> he = page.getElementsByTagName("a");
	// //获取下一级a标签
	// List<DomElement> hee;
	// //循环标签
	// for (int i = 51; i < 83; i++) {
	// //判断是否有所选的省级
	// if (he.get(i).asText().toString().substring(0,
	// province.length()).trim().contains(province)) {
	// //获取省级网址
	// ProUrl=he.get(i).click().getUrl().toString();
	// if ("全省".equals(bool)) {
	// //如果需要全省内容，则返回省级网址
	// return ProUrl;
	// }else {
	// //否则接着解析省级网址，获取省级a标签
	// page2=webClient.getPage(ProUrl);
	// hee=page2.getElementsByTagName("a");
	// //循环省级a标签
	// for(int j=51;i<hee.size()-50;j++) {
	// if(hee.get(j).asText().toString().contains(city)) {
	// //找到地级市对应的网址
	// url=hee.get(j).click().getUrl().toString();
	// //返回地级市网址
	// return url;
	// }
	// }
	// }
	// }
	// }
	// //不存在则返回空字符串
	// return "";
	// }

	// 此方法有问题，不能共用
	// public HtmlPage getHtmlPageByUrl(String url)
	// throws FailingHttpStatusCodeException, MalformedURLException, IOException {
	// // 模拟浏览器操作
	// // 创建WebClient
	// WebClient webClient = new WebClient(BrowserVersion.CHROME);
	// // 关闭css代码功能
	// webClient.getOptions().setThrowExceptionOnScriptError(false);
	// webClient.getOptions().setCssEnabled(false);
	// // 如若有可能找不到文件js则加上这句代码
	// webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
	// // 获取网页html
	// HtmlPage page = webClient.getPage(url);
	// webClient.close();
	// return page;
	// }

	// 写入文件夹
	public String getCreateFileUrl(String dataHtml, String timeStamp) throws IOException {
		File file = new File("cacheFile\\" + timeStamp + ".html");
		String pathUrl = file.getAbsolutePath();
		pathUrl = pathUrl.replace("\\", "/");
		FileOutputStream fos = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			byte bytes[] = new byte[512];
			bytes = dataHtml.getBytes();
			int b = bytes.length;
			fos = new FileOutputStream(file);
			fos.write(bytes, 0, b);
			fos.write(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		fos.close();
		return "file:///" + pathUrl;
	}

	public void deleteCacheFile(String timeStamp) {
		File file = new File("cacheFile\\" + timeStamp + ".html");
		if (file.exists() && file.isFile()) {
			file.delete();
		}
	}

	//创建多级文件夹
	public void createFolder(String filePath) {
		File dir=new File(filePath);
		if(!dir.exists()) {
			dir.mkdirs();
		}else {
			
		}
	}

	// 解析a标签
	// public String getHrefUrl(String ahref) {
	// String hrefUrl;
	// hrefUrl=ahref.substring(ahref.indexOf("href=\"")+6).trim();
	// hrefUrl=hrefUrl.substring(0,hrefUrl.indexOf("\"")).trim();
	// hrefUrl="https:"+hrefUrl;
	// System.out.println(hrefUrl);
	// return hrefUrl;
	// }

}
