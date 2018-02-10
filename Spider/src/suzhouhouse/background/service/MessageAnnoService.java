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
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import suzhouhouse.background.entity.HouseFileMessage;
import suzhouhouse.background.entity.ReturnHouseMessage;
import suzhouhouse.background.entity.MegAnnoAndTradeModel.MessageAnnoModel;
import suzhouhouse.background.utils.HouseCommonUtils;
import suzhouhouse.prosceniumgui.entity.MegAnnoAndTradeMegParams;

public class MessageAnnoService {
	public HouseCommonUtils hsu = new HouseCommonUtils();

	// 获取详细信息
	public void getMessageAnnoDetail(String meaaageAnnoaUrl, List<MessageAnnoModel> messList)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 模拟浏览器操作
		// 创建WebClient
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		// 关闭css代码功能
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		// 如若有可能找不到文件js则加上这句代码
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		// 获取第一级网页html
		HtmlPage page = webClient.getPage(meaaageAnnoaUrl);
		HtmlElement he = page.getHtmlElementById("ctl00_MainContent_Panel1");
		DomNodeList<HtmlElement> hts = he.getElementsByTagName("table");
		HtmlTable ht;
		for (int i = 0; i < hts.size(); i++) {
			MessageAnnoModel mam = new MessageAnnoModel();
			ht = (HtmlTable) hts.get(i);
			// 区域
			mam.setHouseArea(ht.getRow(0).getCell(0).getTextContent().toString().trim().replaceAll(" ", ""));
			// 小计套数
			String totalNum = ht.getRow(0).getCell(3).getTextContent().toString().trim();
			if (totalNum != null && !"".equals(totalNum) && totalNum.length() > 1) {
				totalNum = totalNum.substring(0, totalNum.length() - 1);
			}
			mam.setTotalNum(totalNum);
			// 小计建筑面积
			String totalStructureArea = ht.getRow(0).getCell(5).getTextContent().toString().trim();
			if (totalStructureArea != null && !"".equals(totalStructureArea) && totalStructureArea.length() > 1) {
				totalStructureArea = totalStructureArea.substring(0, totalStructureArea.length() - 3);
			}
			mam.setTotalStructureArea(totalStructureArea);

			// 住宅套数
			String houseTotalNum = ht.getRow(1).getCell(3).getTextContent().toString().trim();
			if (houseTotalNum != null && !"".equals(houseTotalNum) && houseTotalNum.length() > 1) {
				houseTotalNum = houseTotalNum.substring(0, houseTotalNum.length() - 1);
			}
			mam.setHouseTotalNum(houseTotalNum);
			// 住宅建筑面积
			String houseTotalStructureArea = ht.getRow(1).getCell(5).getTextContent().toString().trim();
			if (houseTotalStructureArea != null && !"".equals(houseTotalStructureArea)
					&& houseTotalStructureArea.length() > 1) {
				houseTotalStructureArea = houseTotalStructureArea.substring(0, houseTotalStructureArea.length() - 3);
			}
			mam.setHouseTotalStructureArea(houseTotalStructureArea);
			messList.add(mam);
		}

	}

	// 创建excel
	public XSSFWorkbook createExcelFile(List<MessageAnnoModel> messList) {
		String[] title = { "序号", "区域", "区域总计套数", "区域总建筑面积（㎡）", "住宅套数", "住宅总建筑面积（㎡）" };
		// 创建Excel工作
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 创建一个sheet表
		XSSFSheet sheet = workbook.createSheet();
		// 设置列宽
		sheet.setColumnWidth(2, 20 * 256);
		sheet.setColumnWidth(3, 20 * 256);
		sheet.setColumnWidth(4, 20 * 256);
		sheet.setColumnWidth(5, 20 * 256);
		// 创建第一行
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell;
		// 插入第一行数据
		for (int i = 0; i < title.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(title[i]);
		}
		// 追加数据,向第二行开始加入数据 i = 1
		for (int i = 1; i < messList.size() + 1; i++) {
			XSSFRow row2 = sheet.createRow(i);
			XSSFCell cell2 = row2.createCell(0);
			cell2.setCellValue(i);

			cell2 = row2.createCell(1);
			cell2.setCellValue(messList.get(i - 1).getHouseArea());
			cell2 = row2.createCell(2);
			cell2.setCellValue(messList.get(i - 1).getTotalNum());
			cell2 = row2.createCell(3);
			cell2.setCellValue(messList.get(i - 1).getTotalStructureArea());
			cell2 = row2.createCell(4);
			cell2.setCellValue(messList.get(i - 1).getHouseTotalNum());
			cell2 = row2.createCell(5);
			cell2.setCellValue(messList.get(i - 1).getHouseTotalStructureArea());

		}
		return workbook;
	}

	// 写入文件
	public HouseFileMessage writeExcelFile(XSSFWorkbook workbook, MegAnnoAndTradeMegParams maatmp) {
		HouseFileMessage hfm = new HouseFileMessage();
		FileOutputStream fos = null;
		String date = hsu.turnHouseDate();
		hsu.createFolder(maatmp.getFilePath());
		String lastFilePath = maatmp.getFilePath() + "\\" + "可售房源信息公示" + date + ".xls";
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
