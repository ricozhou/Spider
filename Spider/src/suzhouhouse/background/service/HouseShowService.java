package suzhouhouse.background.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import suzhouhouse.background.entity.HouseFileMessage;
import suzhouhouse.background.entity.houseshow.HouseShowCompanyInfo;
import suzhouhouse.background.entity.houseshow.HouseShowMessage;
import suzhouhouse.background.utils.HouseCommonUtils;
import suzhouhouse.prosceniumgui.entity.HouseShowParams;
import suzhouhouse.prosceniumgui.maingame.SZHmainGui;

public class HouseShowService {
	public HouseCommonUtils hsu = new HouseCommonUtils();

	// 获取详细信息，每十条一次
	public void getHouseShowDetail(HouseShowParams houseShowParams, List<HouseShowMessage> houseList, int num)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 验证状态
		if (!SZHmainGui.houseShowFlag) {
			return;
		}
		// 获取随机码，有效期较短所以只够这十条使用
		String ranNum = hsu.getOrUrl();
		// 第一级真实地址，有效期几分钟，只够这十条信息查询
		String urlOne = "http://spf.szfcweb.com/szfcweb/" + ranNum + "/DataSerach/SaleInfoProListIndex.aspx";
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

		// 登录按钮,即可获取本页所有html
		HtmlSubmitInput sub = hf.getInputByName("ctl00$MainContent$bt_select");
		HtmlPage hp = sub.click();
		// 但要区分需要第几页，由于直接点击select不起作用，所以只能循环点击
		for (int i = 0; i < num; i++) {
			hp = hp.getElementById("ctl00_MainContent_OraclePager1_ctl12_Next").click();
		}
		// 这下彻底获取目标页的html了
		// 传递page循环取数据
		boolean isSuccess = setHouseShowMeg(houseList, hp, ranNum);

	}

	// 取数据
	public boolean setHouseShowMeg(List<HouseShowMessage> houseList, HtmlPage hp, String ranNum) throws IOException {
		// 获取表格信息
		HtmlTable ht = (HtmlTable) hp.getElementById("ctl00_MainContent_OraclePager1");
		int n = ht.getRowCount();
		for (int i = 1; i < n; i++) {
			// 验证状态
			if (!SZHmainGui.houseShowFlag) {
				return false;
			}
			// 新建一个数据对象,存储每一条
			HouseShowMessage hsm = new HouseShowMessage();
			// 从第二行开始
			HtmlTableRow row = ht.getRow(i);
			// 获取第一列项目详细信息
			HtmlElement he = row.getCell(0).getElementsByTagName("a").get(0);
			HtmlPage hp2 = he.click();
			// 这是为了获取楼幢信息，暂不实现

			// 设置项目名称
			hsm.setProjectName(he.asText().toString().trim());

			// 第二列比较详细
			// 获取第二列公司信息
			String url2 = row.getCell(1).asXml().toString();
			String url3 = url2.substring(url2.indexOf("showModalDialog('") + 17);
			String url4 = url3.substring(0, url3.indexOf("',"));
			String urlTwo = "http://spf.szfcweb.com/szfcweb/" + ranNum + "/DataSerach/" + url4;
			// 根据网址再去请求公司详细信息
			getCompanyDetail(urlTwo, hsm);

			// 设置区域
			hsm.setProjectArea(row.getCell(2).asText().trim());
			// 添加到集合
			houseList.add(hsm);
		}

		return true;

	}

	// 取公司相关信息
	private void getCompanyDetail(String urlTwo, HouseShowMessage hsm)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 验证状态
		if (!SZHmainGui.houseShowFlag) {
			return;
		}
		// 模拟浏览器操作
		// 创建WebClient
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		// 关闭css代码功能
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		// 如若有可能找不到文件js则加上这句代码
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		// 获取第一级网页html
		HtmlPage page = webClient.getPage(urlTwo);
		HouseShowCompanyInfo hsci = new HouseShowCompanyInfo();
		// 公司名称
		hsci.setCompanyName(page.getElementById("ctl00_MainContent_lb_C_NAME").asText().trim());
		// 法定代表人姓名
		hsci.setLegalRepresentative(page.getElementById("ctl00_MainContent_lb_C_LEGALPERSON").asText().trim());
		// 法定代表人电话
		hsci.setLegalRepreTel(page.getElementById("ctl00_MainContent_lb_C_LEGALPERSONTEL").asText().trim());
		// 法定地址
		hsci.setLegalRepreAddress(page.getElementById("ctl00_MainContent_lb_C_REGAREA").asText().trim());
		// 营业执照注册号
		hsci.setBusinessLicenseNum(page.getElementById("ctl00_MainContent_lb_C_BLICENSESN").asText().trim());
		// 资质证书编号
		hsci.setQualificatiomNum(page.getElementById("ctl00_MainContent_lb_C_GRADE").asText().trim());
		// 企业类型
		hsci.setCompanyType(page.getElementById("ctl00_MainContent_lb_C_TYPE").asText().trim());
		// 通讯地址
		hsci.setMailAddress(page.getElementById("ctl00_MainContent_lb_C_MAREA").asText().trim());
		// 邮政编码
		hsci.setPostalCode(page.getElementById("ctl00_MainContent_lb_C_MPOST").asText().trim());
		// E-mail
		hsci.setEmail(page.getElementById("ctl00_MainContent_lb_C_MEMAIL").asText().trim());
		// 网站
		hsci.setWebsiteUrl(page.getElementById("ctl00_MainContent_lb_C_WEBURL").asText().trim());
		// 联系人
		hsci.setLinkman(page.getElementById("ctl00_MainContent_lb_C_LINKMAN").asText().trim());
		// 联系电话
		hsci.setLinkNum(page.getElementById("ctl00_MainContent_lb_C_CONTACTTEL").asText().trim());
		hsm.setHsci(hsci);
	}

	// 创建表格
	public XSSFWorkbook createExcelFile(List<HouseShowMessage> houseList) {
		// 验证状态
		if (!SZHmainGui.houseShowFlag) {
			return null;
		}
		String[] title = { "序号", "编号", "项目名称", "项目区域", "房地产公司名称", "项目地址", "代理公司名称", "楼幢详细信息", "法定代表人姓名", "法定代表人电话",
				"法定地址", "营业执照注册号", "资质证书编号", "企业类型", "通讯地址", "邮政编码", "E-mail", "网站", "联系人", "联系电话" };
		// 创建Excel工作
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 创建一个sheet表
		XSSFSheet sheet = workbook.createSheet();
		// 设置列宽
		sheet.setColumnWidth(0, 6 * 256);
		sheet.setColumnWidth(1, 6 * 256);
		sheet.setColumnWidth(2, 18 * 256);
		sheet.setColumnWidth(3, 10 * 256);
		sheet.setColumnWidth(4, 30 * 256);
		sheet.setColumnWidth(5, 50 * 256);
		sheet.setColumnWidth(6, 20 * 256);
		sheet.setColumnWidth(7, 16 * 256);
		sheet.setColumnWidth(8, 20 * 256);
		sheet.setColumnWidth(9, 15 * 256);
		sheet.setColumnWidth(10, 50 * 256);
		sheet.setColumnWidth(11, 20 * 256);
		sheet.setColumnWidth(12, 20 * 256);
		sheet.setColumnWidth(13, 20 * 256);
		sheet.setColumnWidth(14, 50 * 256);
		sheet.setColumnWidth(15, 20 * 256);
		sheet.setColumnWidth(16, 20 * 256);
		sheet.setColumnWidth(17, 20 * 256);
		sheet.setColumnWidth(18, 15 * 256);
		sheet.setColumnWidth(19, 15 * 256);
		// 创建第一行
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell;
		// 插入第一行数据
		for (int i = 0; i < title.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(title[i]);
		}
		// 追加数据,向第二行开始加入数据 i = 1
		for (int i = 1; i < houseList.size() + 1; i++) {
			XSSFRow row2 = sheet.createRow(i);
			XSSFCell cell2 = row2.createCell(0);
			cell2.setCellValue(i);
			cell2 = row2.createCell(1);
			cell2.setCellValue(i);
			cell2 = row2.createCell(2);
			cell2.setCellValue(houseList.get(i - 1).getProjectName());
			System.out.println(houseList.get(i - 1).getProjectName());
			cell2 = row2.createCell(3);
			cell2.setCellValue(houseList.get(i - 1).getProjectArea());
			cell2 = row2.createCell(4);
			cell2.setCellValue(houseList.get(i - 1).getHsci().getCompanyName());
			cell2 = row2.createCell(5);
			// cell2.setCellValue(houseList.get(i - 1).getItemUrl());
			cell2 = row2.createCell(6);
			// cell2.setCellValue(houseList.get(i - 1).getInitialPrice());
			cell2 = row2.createCell(7);
			// cell2.setCellValue(houseList.get(i - 1).getConsultPrice());
			cell2 = row2.createCell(8);
			cell2.setCellValue(houseList.get(i - 1).getHsci().getLegalRepresentative());
			cell2 = row2.createCell(9);
			cell2.setCellValue(houseList.get(i - 1).getHsci().getLegalRepreTel());
			cell2 = row2.createCell(10);
			cell2.setCellValue(houseList.get(i - 1).getHsci().getLegalRepreAddress());
			cell2 = row2.createCell(11);
			cell2.setCellValue(houseList.get(i - 1).getHsci().getBusinessLicenseNum());
			cell2 = row2.createCell(12);
			cell2.setCellValue(houseList.get(i - 1).getHsci().getQualificatiomNum());
			cell2 = row2.createCell(13);
			cell2.setCellValue(houseList.get(i - 1).getHsci().getCompanyType());
			cell2 = row2.createCell(14);
			cell2.setCellValue(houseList.get(i - 1).getHsci().getMailAddress());
			cell2 = row2.createCell(15);
			cell2.setCellValue(houseList.get(i - 1).getHsci().getPostalCode());
			cell2 = row2.createCell(16);
			cell2.setCellValue(houseList.get(i - 1).getHsci().getEmail());
			cell2 = row2.createCell(17);
			cell2.setCellValue(houseList.get(i - 1).getHsci().getWebsiteUrl());
			cell2 = row2.createCell(18);
			cell2.setCellValue(houseList.get(i - 1).getHsci().getLinkman());
			cell2 = row2.createCell(19);
			cell2.setCellValue(houseList.get(i - 1).getHsci().getLinkNum());
		}
		// 写入文件
		return workbook;
	}

	// 写入Excel文件
	public HouseFileMessage writeExcelFile(XSSFWorkbook workbook, HouseShowParams houseShowParams) {
		// 验证状态
		if (!SZHmainGui.houseShowFlag) {
			return null;
		}
		HouseFileMessage hfm = new HouseFileMessage();
		FileOutputStream fos = null;
		String date = hsu.turnHouseDate();
		if (houseShowParams.isAllData) {
			houseShowParams.setProjectArea("全市");
		}
		hsu.createFolder(houseShowParams.getFilePath());
		String lastFilePath = houseShowParams.getFilePath() + "\\" + houseShowParams.getProjectArea()
				+ houseShowParams.getCompanyName() + houseShowParams.getProjectName() + "（可售楼盘展示）" + date + ".xls";
		try {
			fos = new FileOutputStream(lastFilePath);
			workbook.write(fos);
			fos.flush();
			System.out.println("存盘完成！");
			hfm.setBl(true);
			hfm.setLastFilePath(lastFilePath);
			return hfm;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		hfm.setBl(false);
		hfm.setLastFilePath(lastFilePath);
		return hfm;
	}

}
