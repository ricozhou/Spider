package suzhouhouse.background.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import suzhouhouse.background.entity.HouseFileMessage;
import suzhouhouse.background.entity.MegAnnoAndTradeModel.MessageAnnoModel;
import suzhouhouse.background.entity.MegAnnoAndTradeModel.TradeMessageModel;
import suzhouhouse.background.entity.MegAnnoAndTradeModel.TradeRightMessageModel;
import suzhouhouse.background.utils.HouseCommonUtils;
import suzhouhouse.prosceniumgui.entity.MegAnnoAndTradeMegParams;

public class TradeMessageService {
	public HouseCommonUtils hsu = new HouseCommonUtils();

	public void getTradeMessageDetail(String tradeMessageUrl, TradeMessageModel tmm)
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
		HtmlPage page = webClient.getPage(tradeMessageUrl);

		tmm.setTradeName("成交房屋信息【" + page.getElementById("ctl00_TitleContent_lbl_date").getTextContent().trim() + "】");
		tmm.setTradeNameTwo(page.getElementById("ctl00_MainContent_lbl_jjtit").getTextContent().trim());
		HtmlTable ht = (HtmlTable) page.getElementById("ctl00_MainContent_mytable");
		List<MessageAnnoModel> messList = new ArrayList<MessageAnnoModel>();
		for (int i = 1; i < ht.getRowCount(); i = i + 2) {
			MessageAnnoModel mam = new MessageAnnoModel();
			mam.setHouseArea(ht.getRow(i).getCell(0).getTextContent().trim());
			mam.setTotalNum(ht.getRow(i).getCell(2).getTextContent().trim());
			mam.setTotalStructureArea(ht.getRow(i).getCell(3).getTextContent().trim());
			mam.setHouseTotalNum(ht.getRow(i + 1).getCell(1).getTextContent().trim());
			mam.setHouseTotalStructureArea(ht.getRow(i + 1).getCell(2).getTextContent().trim());
			messList.add(mam);
		}
		tmm.setMamList(messList);

		HtmlTable htt = (HtmlTable) page.getElementsByTagName("table").get(0);
		String trm = htt.getRow(1).getCell(0).getElementsByTagName("script").get(4).getTextContent().trim();
		// 转化为数组返回
		String[][] trmData = hsu.getStringDetail(trm);
		List<TradeRightMessageModel> trmList = new ArrayList<TradeRightMessageModel>();
		for (int i = 0; i < trmData[0].length; i++) {
			TradeRightMessageModel trmm = new TradeRightMessageModel();
			trmm.setTradeTime(trmData[0][i]);
			trmm.setTradeAveragePrice(trmData[1][i]);
			trmList.add(trmm);
		}
		tmm.setTrmmList(trmList);

	}

	public XSSFWorkbook createExcelFile(TradeMessageModel tmm) {
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
		for (int i = 1; i < tmm.getMamList().size() + 1; i++) {
			XSSFRow row2 = sheet.createRow(i);
			XSSFCell cell2 = row2.createCell(0);
			cell2.setCellValue(i);

			cell2 = row2.createCell(1);
			cell2.setCellValue(tmm.getMamList().get(i - 1).getHouseArea());
			cell2 = row2.createCell(2);
			cell2.setCellValue(tmm.getMamList().get(i - 1).getTotalNum());
			cell2 = row2.createCell(3);
			cell2.setCellValue(tmm.getMamList().get(i - 1).getTotalStructureArea());
			cell2 = row2.createCell(4);
			cell2.setCellValue(tmm.getMamList().get(i - 1).getHouseTotalNum());
			cell2 = row2.createCell(5);
			cell2.setCellValue(tmm.getMamList().get(i - 1).getHouseTotalStructureArea());

		}
		// 右侧信息
		XSSFRow row3 = sheet.createRow(tmm.getMamList().size() + 3);
		XSSFCell cell3 = row3.createCell(title.length + 3);
		cell3.setCellValue(tmm.getTradeNameTwo());
		// 设置时间
		XSSFRow row4 = sheet.createRow(tmm.getMamList().size() + 4);
		XSSFCell cell4;
		for (int i = title.length + 4; i < tmm.getTrmmList().size() + title.length + 4; i++) {
			cell4 = row4.createCell(i);
			cell4.setCellValue(tmm.getTrmmList().get(i - title.length - 4).getTradeTime());
		}

		// 设置值
		XSSFRow row5 = sheet.createRow(tmm.getMamList().size() + 5);
		XSSFCell cell5;
		for (int i = title.length + 4; i < tmm.getTrmmList().size() + title.length + 4; i++) {
			cell5 = row5.createCell(i);
			cell5.setCellValue(tmm.getTrmmList().get(i - title.length - 4).getTradeAveragePrice());
		}
		return workbook;
	}

	public HouseFileMessage writeExcelFile(XSSFWorkbook workbook, MegAnnoAndTradeMegParams maatmp, String excelName) {
		HouseFileMessage hfm = new HouseFileMessage();
		FileOutputStream fos = null;
		String date = hsu.turnHouseDate();
		hsu.createFolder(maatmp.getFilePath());
		String lastFilePath = maatmp.getFilePath() + "\\" + excelName + date + ".xls";
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
