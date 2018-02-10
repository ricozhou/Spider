package suzhouhouse.background.utils;

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
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import suzhouhouse.prosceniumgui.entity.HouseShowParams;
import suzhouhouse.prosceniumgui.entity.PermitPresaleParams;
import suzhouhouse.prosceniumgui.maingame.SZHmainGui;

public class HouseCommonUtils {
	// 获取条数，页数
	public int[] getPageAndNum(HouseShowParams houseShowParams)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 验证状态
		if (!SZHmainGui.houseShowFlag) {
			return null;
		}
		int[] num = new int[2];
		String urlOne = "http://spf.szfcweb.com/szfcweb/DataSerach/SaleInfoProListIndex.aspx";
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
		// 获取表单
		HtmlForm hf = page.getFormByName("aspnetForm");
		// 项目名称
		HtmlTextInput hti = hf.getInputByName("ctl00$MainContent$txt_Pro");
		hti.setValueAttribute(houseShowParams.getProjectName());
		// 公司名称
		HtmlTextInput hti2 = hf.getInputByName("ctl00$MainContent$txt_Com");
		hti2.setValueAttribute(houseShowParams.getCompanyName());
		// 下拉框
		HtmlSelect hs = hf.getSelectByName("ctl00$MainContent$ddl_RD_CODE");
		// hs.setSelectedIndex(5);
		hs.setSelectedAttribute(houseShowParams.getProjectArea(), true);
		// 登录按钮
		HtmlSubmitInput sub = hf.getInputByName("ctl00$MainContent$bt_select");
		HtmlPage hp = sub.click();
		// 获取表格信息
		HtmlTable ht = (HtmlTable) hp.getElementById("ctl00_MainContent_OraclePager1");
		if (ht.getRow(0).getCell(0).equals("没有数据")) {
			num[0] = 0;
			num[1] = 0;
			return num;
		}
		// 获取页数条数
		HtmlTable htt = (HtmlTable) hp.getElementsByTagName("table").get(0).getElementsByTagName("table").get(0)
				.getElementsByTagName("table").get(1);
		String s = htt.asText().toString();
		String ss = s.substring(s.indexOf("共") + 6, s.indexOf("条")).trim();
		String ss2 = s.substring(s.lastIndexOf("共") + 6);
		String sss = ss2.substring(0, ss2.indexOf("页")).trim();
		num[0] = Integer.parseInt(ss);
		num[1] = Integer.parseInt(sss);
		return num;
	}

	// 预售许可证页数
	public int[] getPageAndNum2(PermitPresaleParams permitPresaleParams)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 验证状态
		if (!SZHmainGui.permitPresaleFlag) {
			return null;
		}
		int[] num = new int[2];
		String urlOne = "http://www.szfcweb.com/szfcweb/DataSerach/MITShowList.aspx";
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
		// 获取表格信息
		HtmlTable ht = (HtmlTable) page.getElementsByTagName("table").get(3);
		String s = ht.getRow(0).asText().toString();
		String ss = s.substring(s.indexOf("共") + 6, s.indexOf("条")).trim();
		String ss2 = s.substring(s.lastIndexOf("共") + 6);
		String sss = ss2.substring(0, ss2.indexOf("页")).trim();
		num[0] = Integer.parseInt(ss);
		num[1] = Integer.parseInt(sss);
		return num;
	}

	// 获取随机码
	public String getOrUrl() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		String url = "http://www.szfcweb.com/";
		WebClient webClient = new WebClient();
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		HtmlPage page = webClient.getPage(url);
		page = page.getElementsByTagName("a").get(8).click();
		String url2 = page.toString().substring(page.toString().indexOf("szfcweb/") + 8,
				page.toString().indexOf("))") + 2);
		return url2;
	}

	//
	public String getOrUrl2() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		String url = "http://www.szfcweb.com/szfcweb/DataSerach/MITShowList.aspx";
		WebClient webClient = new WebClient();
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		HtmlPage page = webClient.getPage(url);
		String url2 = page.getBaseURI().toString().substring(page.getBaseURI().toString().indexOf("szfcweb/") + 8,
				page.getBaseURI().toString().indexOf("))") + 2);
		return url2;
	}

	// 时间戳
	public String turnHouseDate() {
		DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		String date = df.format(new Date());
		return date;
	}

	// 转化时间
	public String turnHouseDate2(long s, long e) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		String date = df.format(e - s);
		return date;
	}

	// 拼接详细网址
	public String getDetailUrl(String url2, String ranNum) {
		String url3 = url2.substring(url2.indexOf("showModalDialog('") + 17);
		String url4 = url3.substring(0, url3.indexOf("',"));
		String urlTwo = "http://www.szfcweb.com/szfcweb/" + ranNum + "/DataSerach/" + url4;
		return urlTwo;
	}

	public String[][] getStringDetail(String trm) {
		String trm2 = trm.replaceAll(" ", "");
		String trmTimeDate = trm.substring(trm.indexOf("[\"") + 1, trm.indexOf("];"));
		String trmPriceData = trm2.substring(trm2.indexOf("data=[") + 6, trm2.lastIndexOf("];"));
		String[] td = trmTimeDate.replaceAll("\"", "").split(",");
		String[] tpd = trmPriceData.split(",");
		String[][] str = new String[2][td.length];
		str[0] = td;
		str[1] = tpd;
		return str;
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
