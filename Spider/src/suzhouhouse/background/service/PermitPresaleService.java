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
import suzhouhouse.background.entity.permitpresale.PermitPresaleCompanyInfo;
import suzhouhouse.background.entity.permitpresale.PermitPresaleDetailInfo;
import suzhouhouse.background.entity.permitpresale.PermitPresaleMessage;
import suzhouhouse.background.utils.HouseCommonUtils;
import suzhouhouse.prosceniumgui.entity.PermitPresaleParams;
import suzhouhouse.prosceniumgui.maingame.SZHmainGui;

public class PermitPresaleService {
	public HouseCommonUtils hsu = new HouseCommonUtils();

	// 获取详细信息，每十条一次
	// 获取数据
	public void getPermitPresaleDetail(PermitPresaleParams permitPresaleParams, List<PermitPresaleMessage> ppmList,
			int num) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 验证状态
		if (!SZHmainGui.permitPresaleFlag) {
			return;
		}
		// 获取随机码，有效期较短所以只够这十条使用
		String ranNum = hsu.getOrUrl2();
		// 第一级真实地址，有效期几分钟，只够这十条信息查询
		String urlOne = "http://www.szfcweb.com/szfcweb/" + ranNum + "/DataSerach/MITShowList.aspx";
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
		// 下拉框
		HtmlSelect hs = hf.getSelectByName("ctl00$MainContent$ddl_RD_CODE");
		hs.setSelectedAttribute(permitPresaleParams.getProjectArea(), true);
		// 登录按钮,即可获取本页所有html
		HtmlSubmitInput sub = hf.getInputByName("ctl00$MainContent$bt_select");
		HtmlPage hp = sub.click();
		// 但要区分需要第几页，由于直接点击select不起作用，所以只能循环点击
		for (int i = 0; i < num; i++) {
			hp = hp.getElementById("ctl00_MainContent_PageGridView1_ctl12_Next").click();
		}
		// 放数据
		boolean isSuccess = setPermitPresaleMeg(ppmList, hp, ranNum);
	}

	// 取数据
	private boolean setPermitPresaleMeg(List<PermitPresaleMessage> ppmList, HtmlPage hp, String ranNum)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 获取表格信息
		HtmlTable ht = (HtmlTable) hp.getElementById("ctl00_MainContent_PageGridView1");
		int n = ht.getRowCount();
		for (int i = 1; i < n; i++) {
			// 验证状态
			if (!SZHmainGui.permitPresaleFlag) {
				return false;
			}
			// 新建一个数据对象,存储每一条
			PermitPresaleMessage ppm = new PermitPresaleMessage();
			// 从第二行开始
			HtmlTableRow row = ht.getRow(i);
			// 设置许可证号
			ppm.setPermitNum(row.getCell(0).asText().trim());
			// 获取项目名称
			ppm.setProjectName(row.getCell(1).asText().trim());

			// 拼接网址
			String url2 = row.getCell(0).asXml().toString();
			// 根据网址再去请求许可证详细信息
			getPermitPresaleDetail(hsu.getDetailUrl(url2, ranNum), ppm);

			// 获取第三列公司信息
			String url21 = row.getCell(2).asXml().toString();
			// 根据网址再去请求公司详细信息
			getCompanyDetail(hsu.getDetailUrl(url21, ranNum), ppm);

			// 添加到集合
			ppmList.add(ppm);
		}
		return true;
	}

	// 公司详细信息
	private void getCompanyDetail(String detailUrl, PermitPresaleMessage ppm)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 验证状态
		if (!SZHmainGui.permitPresaleFlag) {
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
		HtmlPage page = webClient.getPage(detailUrl);
		PermitPresaleCompanyInfo ppci = new PermitPresaleCompanyInfo();
		// 公司名称
		ppci.setCompanyName(page.getElementById("ctl00_MainContent_lb_C_NAME").asText().trim());
		// 法定代表人姓名
		ppci.setLegalRepresentative(page.getElementById("ctl00_MainContent_lb_C_LEGALPERSON").asText().trim());
		// 法定代表人电话
		ppci.setLegalRepreTel(page.getElementById("ctl00_MainContent_lb_C_LEGALPERSONTEL").asText().trim());
		// 法定地址
		ppci.setLegalRepreAddress(page.getElementById("ctl00_MainContent_lb_C_REGAREA").asText().trim());
		// 营业执照注册号
		ppci.setBusinessLicenseNum(page.getElementById("ctl00_MainContent_lb_C_BLICENSESN").asText().trim());
		// 资质证书编号
		ppci.setQualificatiomNum(page.getElementById("ctl00_MainContent_lb_C_GRADE").asText().trim());
		// 企业类型
		ppci.setCompanyType(page.getElementById("ctl00_MainContent_lb_C_TYPE").asText().trim());
		// 通讯地址
		ppci.setMailAddress(page.getElementById("ctl00_MainContent_lb_C_MAREA").asText().trim());
		// 邮政编码
		ppci.setPostalCode(page.getElementById("ctl00_MainContent_lb_C_MPOST").asText().trim());
		// E-mail
		ppci.setEmail(page.getElementById("ctl00_MainContent_lb_C_MEMAIL").asText().trim());
		// 网站
		ppci.setWebsiteUrl(page.getElementById("ctl00_MainContent_lb_C_WEBURL").asText().trim());
		// 联系人
		ppci.setLinkman(page.getElementById("ctl00_MainContent_lb_C_LINKMAN").asText().trim());
		// 联系电话
		ppci.setLinkNum(page.getElementById("ctl00_MainContent_lb_C_CONTACTTEL").asText().trim());
		ppm.setPpci(ppci);
	}

	// 许可证详细信息
	private void getPermitPresaleDetail(String detailUrl, PermitPresaleMessage ppm)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 验证状态
		if (!SZHmainGui.permitPresaleFlag) {
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
		HtmlPage page = webClient.getPage(detailUrl);

		PermitPresaleDetailInfo ppdi = new PermitPresaleDetailInfo();
		String companyName = page.getElementById("ctl00_MainContent_lb_PP_CORPName").getTextContent();
		String houseName = page.getElementById("ctl00_MainContent_lb_Pro_Name").getTextContent();
		String houseAddress = page.getElementById("ctl00_MainContent_lb_Pro_Address").getTextContent();
		String yuHouseArea = page.getElementById("ctl00_MainContent_lb_Pre_SumArea").getTextContent();
		String zhuHouseArea = page.getElementById("ctl00_MainContent_lb_ZG_Area").getTextContent();
		String zhuHouseNum = page.getElementById("ctl00_MainContent_lb_ZG_Count").getTextContent();
		String notZhuHouseArea = page.getElementById("ctl00_MainContent_lb_FZG_Area").getTextContent();
		String notZhuHouseNum = page.getElementById("ctl00_MainContent_lb_FZG_Count").getTextContent();
		String otherArea = page.getElementById("ctl00_MainContent_lb_QT_Area").getTextContent();
		String otherNum = page.getElementById("ctl00_MainContent_lb_QT_Count").getTextContent();
		String startDate = page.getElementById("ctl00_MainContent_lb_PP_IDate").getTextContent();
		String overDate = page.getElementById("ctl00_MainContent_lb_JZ_IDate").getTextContent();
		ppdi.setCompanyName(companyName);
		ppdi.setHouseName(houseName);
		ppdi.setHouseAddress(houseAddress);
		ppdi.setYuHouseArea(yuHouseArea);
		ppdi.setZhuHouseArea(zhuHouseArea);
		ppdi.setZhuHouseNum(zhuHouseNum);
		ppdi.setNotZhuHouseArea(notZhuHouseArea);
		ppdi.setNotZhuHouseNum(notZhuHouseNum);
		ppdi.setOtherArea(otherArea);
		ppdi.setOtherNum(otherNum);
		ppdi.setStartDate(startDate);
		ppdi.setOverDate(overDate);
		ppm.setPpdi(ppdi);
	}

	// 创建Excel
	public XSSFWorkbook createExcelFile(List<PermitPresaleMessage> ppmList) {
		// 验证状态
		if (!SZHmainGui.permitPresaleFlag) {
			return null;
		}
		String[] title = { "序号", "编号", "许可证号", "项目名称", "售房单位名称", "商品房名称", "商品房坐落", "预售总建筑面积", "住宅面积", "住宅套数", "非住宅面积",
				"非住宅套数", "其他面积", "其他套数", "颁发日期", "截止日期", "房地产公司名称", "法定代表人姓名", "法定代表人电话", "法定地址", "营业执照注册号", "资质证书编号",
				"企业类型", "通讯地址", "邮政编码", "E-mail", "网站", "联系人", "联系电话" };
		// 创建Excel工作
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 创建一个sheet表
		XSSFSheet sheet = workbook.createSheet();
		// 设置列宽

		// 创建第一行
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell;
		// 插入第一行数据
		for (int i = 0; i < title.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(title[i]);
		}
		// 追加数据,向第二行开始加入数据 i = 1
		for (int i = 1; i < ppmList.size() + 1; i++) {
			XSSFRow row2 = sheet.createRow(i);
			XSSFCell cell2 = row2.createCell(0);
			cell2.setCellValue(i);
			cell2 = row2.createCell(1);
			cell2.setCellValue(i);
			cell2 = row2.createCell(2);
			cell2.setCellValue(ppmList.get(i - 1).getPermitNum());
			cell2 = row2.createCell(3);
			cell2.setCellValue(ppmList.get(i - 1).getProjectName());
			// 许可证详细信息
			cell2 = row2.createCell(4);
			cell2.setCellValue(ppmList.get(i - 1).getPpdi().getCompanyName());
			cell2 = row2.createCell(5);
			cell2.setCellValue(ppmList.get(i - 1).getPpdi().getHouseName());
			cell2 = row2.createCell(6);
			cell2.setCellValue(ppmList.get(i - 1).getPpdi().getHouseAddress());
			cell2 = row2.createCell(7);
			cell2.setCellValue(ppmList.get(i - 1).getPpdi().getYuHouseArea());
			cell2 = row2.createCell(8);
			cell2.setCellValue(ppmList.get(i - 1).getPpdi().getZhuHouseArea());
			cell2 = row2.createCell(9);
			cell2.setCellValue(ppmList.get(i - 1).getPpdi().getZhuHouseNum());
			cell2 = row2.createCell(10);
			cell2.setCellValue(ppmList.get(i - 1).getPpdi().getNotZhuHouseArea());
			cell2 = row2.createCell(11);
			cell2.setCellValue(ppmList.get(i - 1).getPpdi().getNotZhuHouseNum());
			cell2 = row2.createCell(12);
			cell2.setCellValue(ppmList.get(i - 1).getPpdi().getOtherArea());
			cell2 = row2.createCell(13);
			cell2.setCellValue(ppmList.get(i - 1).getPpdi().getOtherNum());
			cell2 = row2.createCell(14);
			cell2.setCellValue(ppmList.get(i - 1).getPpdi().getStartDate());
			cell2 = row2.createCell(15);
			cell2.setCellValue(ppmList.get(i - 1).getPpdi().getOverDate());

			// 公司详细信息
			cell2 = row2.createCell(16);
			cell2.setCellValue(ppmList.get(i - 1).getPpci().getCompanyName());
			cell2 = row2.createCell(17);
			cell2.setCellValue(ppmList.get(i - 1).getPpci().getLegalRepresentative());
			cell2 = row2.createCell(18);
			cell2.setCellValue(ppmList.get(i - 1).getPpci().getLegalRepreTel());
			cell2 = row2.createCell(19);
			cell2.setCellValue(ppmList.get(i - 1).getPpci().getLegalRepreAddress());
			cell2 = row2.createCell(20);
			cell2.setCellValue(ppmList.get(i - 1).getPpci().getBusinessLicenseNum());
			cell2 = row2.createCell(21);
			cell2.setCellValue(ppmList.get(i - 1).getPpci().getQualificatiomNum());
			cell2 = row2.createCell(22);
			cell2.setCellValue(ppmList.get(i - 1).getPpci().getCompanyType());
			cell2 = row2.createCell(23);
			cell2.setCellValue(ppmList.get(i - 1).getPpci().getMailAddress());
			cell2 = row2.createCell(24);
			cell2.setCellValue(ppmList.get(i - 1).getPpci().getPostalCode());
			cell2 = row2.createCell(25);
			cell2.setCellValue(ppmList.get(i - 1).getPpci().getEmail());
			cell2 = row2.createCell(26);
			cell2.setCellValue(ppmList.get(i - 1).getPpci().getWebsiteUrl());
			cell2 = row2.createCell(27);
			cell2.setCellValue(ppmList.get(i - 1).getPpci().getLinkman());
			cell2 = row2.createCell(28);
			cell2.setCellValue(ppmList.get(i - 1).getPpci().getLinkNum());
		}

		// 写入文件
		return workbook;
	}

	// 写入文件
	public HouseFileMessage writeExcelFile(XSSFWorkbook workbook, PermitPresaleParams permitPresaleParams) {
		// 验证状态
		if (!SZHmainGui.permitPresaleFlag) {
			return null;
		}
		HouseFileMessage hfm = new HouseFileMessage();
		FileOutputStream fos = null;
		String date = hsu.turnHouseDate();
		hsu.createFolder(permitPresaleParams.getFilePath());
		String lastFilePath = permitPresaleParams.getFilePath() + "\\" + permitPresaleParams.getProjectArea()
				+ "预售许可证信息" + date + ".xls";
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
