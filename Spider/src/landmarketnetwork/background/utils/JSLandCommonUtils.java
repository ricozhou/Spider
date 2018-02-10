package landmarketnetwork.background.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import landmarketnetwork.prosceniumgui.entity.JSLandParams;
import landmarketnetwork.prosceniumgui.maingame.JSLandMainGUI;
import suzhouhouse.prosceniumgui.maingame.SZHmainGui;

public class JSLandCommonUtils {

	// 获取页数
	public int[] getPageAndNum(JSLandParams jsLandParams)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 验证状态
		if (!JSLandMainGUI.JSLandFlag) {
			return null;
		}
		int[] num = new int[2];
		String urlOne = "http://www.landjs.com/web/gygg_list.aspx?gglx=1";
		// 模拟浏览器操作
		// 创建WebClient
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		// 关闭css代码功能
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		// 如若有可能找不到文件js则加上这句代码
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		// 获取第一级网页html
		HtmlPage page = webClient.getPage(urlOne);
		HtmlElement he = page.getHtmlElementById("AspNetPager1");
		he = he.getElementsByTagName("div").get(0);
		String s = he.asText().replaceAll(" ", "");
		String ss = s.substring(s.indexOf("记录") + 3, s.indexOf("条"));
		String sss = s.substring(s.indexOf("/") + 1);
		num[0] = Integer.parseInt(ss);
		num[1] = Integer.parseInt(sss);
		return num;
	}

	public String turnHouseDate2(long startTime, long endTime) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		String date = df.format(endTime - startTime);
		return date;
	}

	public String turnHouseDate() {
		DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		String date = df.format(new Date());
		return date;
	}

	// 格式化保证金到账时间
	public String formatPaymentTime(String paymentEndTime) {
		String str = paymentEndTime.substring(0, paymentEndTime.indexOf(" "));
		String[] md = str.split("/");
		if (md[1].length() == 1) {
			md[1] = "0" + md[1];
		}
		if (md[2].length() == 1) {
			md[2] = "0" + md[2];
		}
		String formatDate = md[0] + md[1] + md[2];
		return formatDate;
	}

	// 格式化保证金到账时间2
	public String formatPaymentTime2(String paymentEndTime) {
		String year = paymentEndTime.substring(0, 4);
		String month = paymentEndTime.substring(5, 7);
		String date = paymentEndTime.substring(8, 10);
		String formatDate = year + month + date;
		return formatDate;
	}

	// 格式化起始价格
	public String formatStartPrice(String startPrice) {
		if (!startPrice.contains("万")) {
			return startPrice;
		}
		String formatPrice = startPrice.substring(0, startPrice.indexOf("万"));
		return formatPrice;
	}

	// 格式化起始价格2
	public String formatStartPrice2(String startPrice, String remiseArea) {
		String formatPrice = "";
		if (startPrice.contains("万") && !startPrice.contains("/")) {
			formatPrice = startPrice.substring(0, startPrice.indexOf("万"));
			formatPrice = formatPrice.replaceAll(",", "");
			formatPrice = formatPrice.replaceAll("，", "");
		} else if (startPrice.contains("元/")) {
			formatPrice = startPrice.substring(0, startPrice.indexOf("元/"));
			formatPrice = formatPrice.replaceAll(",", "");
			formatPrice = formatPrice.replaceAll("，", "");
			int price = (int) (Double.parseDouble(formatPrice) * Double.parseDouble(remiseArea) / 10000);
			formatPrice = String.valueOf(price);
		}
		return formatPrice;
	}

	// 格式化面积2
	public String formatRemiseArea2(String remiseArea) {
		if (!remiseArea.contains("平")) {
			return remiseArea;
		}
		String formatRemiseArea = remiseArea.substring(0, remiseArea.indexOf("平"));
		formatRemiseArea.replaceAll(",", "");
		formatRemiseArea.replaceAll("，", "");
		return formatRemiseArea;
	}

	// 创建多级文件夹
	public void createFolder(String filePath) {
		File dir = new File(filePath);
		if (!dir.exists()) {
			dir.mkdirs();
		} else {

		}
	}

}
