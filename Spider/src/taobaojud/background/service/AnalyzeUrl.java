package taobaojud.background.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.formula.functions.Value;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import login.utils.GetProperties;
import taobaojud.background.entity.FileMessage;
import taobaojud.background.entity.SimpleJudSaleInfo;
import taobaojud.background.utils.BaseUtils;
import taobaojud.prosceniumgui.entity.Params;
import taobaojud.prosceniumgui.maingame.TaoBaoMainUI;

public class AnalyzeUrl {
	public GetProperties getpro;
	public BaseUtils bu = new BaseUtils();
	// public static final String ORIGINAL_URL =
	// "https://sf.taobao.com/item_list.htm";
	public static final String ORIGINAL_URL = "https://sf.taobao.com/list/0.htm?spm=a213w.7398504.filter.1.RxqQ5o&auction_start_seg=-1";

	// 开始解析网址
	// 返回有用数据
	public String startAnalyzeUrl(String url)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 验证状态
		if (!TaoBaoMainUI.taoBaoJudFlag) {
			return null;
		}
		try {
			// 模拟浏览器操作
			// 创建WebClient
			WebClient webClient = new WebClient(BrowserVersion.CHROME);
			// 关闭css代码功能
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setCssEnabled(false);
			// 如若有可能找不到文件js则加上这句代码
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			// 获取网页html
			HtmlPage page = webClient.getPage(url);
			// 淘宝懒加载，数据获取有难度，ajax数据，从script中获取data
			String originalData = page.getElementById("sf-item-list-data").asXml().toString();
			// 确保数据不为空
			if (originalData == null || originalData.substring(originalData.indexOf(":[{") + 2).length() < 5) {
				return "";
			}
			// 截取有用字符串
			String usefulData = originalData.substring(originalData.indexOf(":[{") + 2, originalData.indexOf("}]") + 1);
			// 关闭
			System.out.println(usefulData);
			webClient.close();
			return usefulData;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 处理获取的数据，放入实体类中
	// 返回list集合
	public List<SimpleJudSaleInfo> processData(String usefulData, Params param)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		List<SimpleJudSaleInfo> sjsiList = new ArrayList<SimpleJudSaleInfo>();
		boolean bl = false;
		if (param.getYNGetDetail().equals("详细信息")) {
			bl = true;
		}
		// 掐头去尾分成数组
		String[] useDatas = usefulData.substring(1, usefulData.length() - 1).split("},");
		for (int i = 0; i < useDatas.length; i++) {
			// 验证状态
			if (!TaoBaoMainUI.taoBaoJudFlag) {
				return null;
			}
			SimpleJudSaleInfo sjsi = new SimpleJudSaleInfo();
			// 分别截取对应字段
			String urlId = useDatas[i].substring(useDatas[i].indexOf("id") + 4, useDatas[i].indexOf(",")).trim();
			String itemUrl = useDatas[i].substring(useDatas[i].indexOf("itemUrl") + 10,
					useDatas[i].indexOf(",", useDatas[i].indexOf("itemUrl") + 10) - 1).trim();
			String saleStatus = useDatas[i].substring(useDatas[i].indexOf("status") + 9,
					useDatas[i].indexOf(",", useDatas[i].indexOf("status") + 9) - 1).trim();
			String saleName = useDatas[i].substring(useDatas[i].indexOf("title") + 8,
					useDatas[i].indexOf("\",", useDatas[i].indexOf("title") + 8)).trim();
			String initialPrice = useDatas[i].substring(useDatas[i].indexOf("initialPrice") + 15,
					useDatas[i].indexOf(",", useDatas[i].indexOf("initialPrice") + 15)).trim();
			String consultPrice = useDatas[i].substring(useDatas[i].indexOf("consultPrice") + 15,
					useDatas[i].indexOf(",", useDatas[i].indexOf("consultPrice") + 15)).trim();
			String sellOff = useDatas[i].substring(useDatas[i].indexOf("sellOff") + 9,
					useDatas[i].indexOf(",", useDatas[i].indexOf("sellOff") + 9)).trim();
			String startDate = useDatas[i].substring(useDatas[i].indexOf("start") + 7,
					useDatas[i].indexOf(",", useDatas[i].indexOf("start") + 7)).trim();
			String endDate = useDatas[i]
					.substring(useDatas[i].indexOf("end") + 5, useDatas[i].indexOf(",", useDatas[i].indexOf("end") + 5))
					.trim();
			// 根据url再去请求详细内容
			if (bl) {
				String urlDetail = "https:" + itemUrl;
				sjsi = getDetailMess(urlDetail);
			}

			// 添加到实体类中封装
			sjsi.setId(String.valueOf(i));
			sjsi.setUrlId(urlId);
			sjsi.setItemUrl("https:" + itemUrl);
			sjsi.setSaleStatus(bu.turnSaleStatus(saleStatus));
			sjsi.setSaleName(saleName);
			sjsi.setInitialPrice(bu.turnPrice(initialPrice));
			sjsi.setConsultPrice(bu.turnPrice(consultPrice));

			sjsi.setStartDate(bu.turnDate(startDate));
			sjsi.setEndDate(bu.turnDate(endDate));
			// 加入到集合中并返回
			sjsiList.add(sjsi);
		}

		// 利用jsonjar包解析数据,先转换为json数组
		// jar包版本不兼容fuck
		// JSONObject dataJson=new JSONObject();
		//
		// try {
		// JSONArray jsonArray=JSONArray.fromObject("[{'id':12,'itemUrl':'sss'}]");
		// if(jsonArray.size()>0) {
		// //遍历数组
		// for(int i=0;i<jsonArray.size();i++) {
		// JSONObject jsonObject=jsonArray.getJSONObject(i);
		// System.out.println(jsonObject.get("id"));
		// System.out.println(jsonObject.get("itemUrl"));
		// System.out.println(jsonObject.get("status"));
		// System.out.println(jsonObject.get("title"));
		// System.out.println(jsonObject.get("initialPrice"));
		// System.out.println(jsonObject.get("consultPrice"));
		// System.out.println(jsonObject.get("start"));
		// }
		// }
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		return sjsiList;
	}

	// 进一步获取信息
	private SimpleJudSaleInfo getDetailMess(String urlDetail)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		SimpleJudSaleInfo sjsi = new SimpleJudSaleInfo();
		// 模拟浏览器操作
		// 创建WebClient
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		// 关闭css代码功能
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		// 如若有可能找不到文件js则加上这句代码
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		// 获取网页html
		HtmlPage page = webClient.getPage(urlDetail);
		// 获取详情页中相关信息的url
		String divData;
		try {
			divData = page.getElementById("J_desc").asXml().toString();
			divData = divData.substring(divData.indexOf("data-from=") + 11);
			divData = "https:" + divData.substring(0, divData.indexOf("\">"));
		} catch (Exception e) {
			e.printStackTrace();
			return sjsi;
		}
		// 实例化为了获取textpage
		TextPage textPage;
		String dataHtml;
		try {
			// 获取纯文本源代码
			textPage = webClient.getPage(divData);
			dataHtml = textPage.getContent().toString();
			dataHtml = dataHtml.substring(dataHtml.indexOf("desc='") + 6).trim();
			dataHtml = dataHtml.substring(0, dataHtml.lastIndexOf("';"));
			dataHtml = "<!DOCTYPE html><html><head><meta charset='utf-8'><title>Insert title here</title></head><body>"
					+ dataHtml + "</body></html>";
		} catch (Exception e) {
			e.printStackTrace();
			return sjsi;
		}
		// 将此HTML字符串写入文件再用浏览器打开在获取数据
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		String timeStamp = df.format(new Date());
		String pathUrl = bu.getCreateFileUrl(dataHtml, timeStamp);
		// 获取处理过的标的物详情网页表
		HtmlPage detailPage = webClient.getPage(pathUrl);
		// System.out.println(detailPage.asText());

		bu.deleteCacheFile(timeStamp);
		webClient.close();
		return sjsi;
	}

	// 创建Excel文件
	public XSSFWorkbook createExcelFile(List<SimpleJudSaleInfo> sjsiList) {
		// 验证状态
		if (!TaoBaoMainUI.taoBaoJudFlag) {
			return null;
		}
		String[] title = { "序号", "编号", "自带ID", "拍卖名称", "拍卖状态", "网址", "起拍价", "评估价", "开拍时间", "成交时间" };
		// 创建Excel工作
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 创建一个sheet表
		XSSFSheet sheet = workbook.createSheet();
		// sheet.autoSizeColumn(1, true);
		// for(int i=1;i<title.length;i++) {
		// sheet.setColumnWidth(i, 20 * 256);
		// }
		// 设置列宽
		sheet.setColumnWidth(0, 6 * 256);
		sheet.setColumnWidth(1, 6 * 256);
		sheet.setColumnWidth(2, 18 * 256);
		sheet.setColumnWidth(3, 80 * 256);
		sheet.setColumnWidth(4, 10 * 256);
		sheet.setColumnWidth(5, 50 * 256);
		sheet.setColumnWidth(6, 16 * 256);
		sheet.setColumnWidth(7, 16 * 256);
		sheet.setColumnWidth(8, 20 * 256);
		sheet.setColumnWidth(9, 20 * 256);
		// 创建第一行
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell;
		// 插入第一行数据
		for (int i = 0; i < title.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(title[i]);
		}

		// 追加数据,向第二行开始加入数据 i = 1
		for (int i = 1; i < sjsiList.size() + 1; i++) {
			XSSFRow row2 = sheet.createRow(i);
			XSSFCell cell2 = row2.createCell(0);
			cell2.setCellValue(i);
			cell2 = row2.createCell(1);
			cell2.setCellValue(sjsiList.get(i - 1).getId());
			cell2 = row2.createCell(2);
			cell2.setCellValue(sjsiList.get(i - 1).getUrlId());
			cell2 = row2.createCell(3);
			cell2.setCellValue(sjsiList.get(i - 1).getSaleName());
			cell2 = row2.createCell(4);
			cell2.setCellValue(sjsiList.get(i - 1).getSaleStatus());
			cell2 = row2.createCell(5);
			cell2.setCellValue(sjsiList.get(i - 1).getItemUrl());
			cell2 = row2.createCell(6);
			cell2.setCellValue(sjsiList.get(i - 1).getInitialPrice());
			cell2 = row2.createCell(7);
			cell2.setCellValue(sjsiList.get(i - 1).getConsultPrice());
			cell2 = row2.createCell(8);
			cell2.setCellValue(sjsiList.get(i - 1).getStartDate());
			cell2 = row2.createCell(9);
			cell2.setCellValue(sjsiList.get(i - 1).getEndDate());
		}
		// 写入文件
		return workbook;
	}

	// 写入Excel文件
	public FileMessage writeExcelFile(XSSFWorkbook workbook, Params param) {
		// 验证状态
		if (!TaoBaoMainUI.taoBaoJudFlag) {
			return null;
		}
		FileMessage fm = new FileMessage();
		FileOutputStream fos = null;
		String date = bu.turnDate2();
		//文件夹是否存在不存在则创建
		bu.createFolder(param.getFilePath());
		String lastFilePath = param.getFilePath() + "\\" + param.getProvince() + "省（市）" + param.getCity() + "市（县）司法拍卖（"
				+ param.getSubjectType() + "）" + date + ".xls";
		try {
			fos = new FileOutputStream(lastFilePath);
			workbook.write(fos);
			fos.flush();
			System.out.println("存盘完成！");
			fm.setBl(true);
			fm.setLastFilePath(lastFilePath);
			return fm;
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
		fm.setBl(false);
		fm.setLastFilePath(lastFilePath);
		return fm;
	}

	// // 通过网页分析获取URL
	// public String getOneUrl(String province, String city)
	// throws FailingHttpStatusCodeException, MalformedURLException, IOException {
	// String url;
	// String bool;
	// if ("全省".equals(city) || "北京".equals(city) || "上海".equals(city) ||
	// "天津".equals(city) || "重庆".equals(city)) {
	// bool = "全省";
	// } else {
	// bool = "地级市";
	// }
	// url = bu.getProvinceCityUrl(province, city, bool);
	// return url;
	// }

	// 传递所有参数获取URL
	public String getOneUrl(Params param) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 验证状态
		if (!TaoBaoMainUI.taoBaoJudFlag) {
			return null;
		}
		// 模拟浏览器操作
		// 创建WebClient
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		// 关闭css代码功能
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		// 如若有可能找不到文件js则加上这句代码
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		// 获取网页html
		HtmlPage page = webClient.getPage(ORIGINAL_URL);
		// 获取初始页面所有的a标签
		List<DomElement> he = page.getElementsByTagName("a");
		String url = null;
		// 循环找到标的物类型标签
		for (int i = 38; i < 50; i++) {
			if (he.get(i).asText().toString().trim().contains(param.getSubjectType())) {
				url = he.get(i).click().getUrl().toString();
			}
		}
		if (url == null) {
			return "";
		}
		// 获取标的物状态标签
		page = webClient.getPage(url);
		he = page.getElementsByTagName("a");
		for (int i = 83; i < 89; i++) {
			if (he.get(i).asText().toString().trim().contains(param.getSubjectStatus())) {
				url = he.get(i).click().getUrl().toString();
			}
		}
		if (url == null) {
			return "";
		}
		// 获取省市信息
		page = webClient.getPage(url);
		he = page.getElementsByTagName("a");
		if ("不限".equals(param.getProvince())) {
			url = he.get(50).click().getUrl().toString();
			return url;
		} else {
			// 循环标签
			for (int i = 51; i < 83; i++) {
				// 判断是否有所选的省级
				if (he.get(i).asText().toString().substring(0, param.getProvince().length()).trim()
						.contains(param.getProvince())) {
					// 获取省级网址
					url = he.get(i).click().getUrl().toString();

					// 否则接着解析省级网址，获取省级a标签
					page = webClient.getPage(url);
					he = page.getElementsByTagName("a");
					// 循环省级a标签
					for (int j = 51; i < he.size() - 50; j++) {
						if (he.get(j).asText().toString().contains(param.getCity())) {
							// 找到地级市对应的网址
							url = he.get(j).click().getUrl().toString();
							// 返回地级市网址
							return url;
						}
					}

				}
			}
		}
		webClient.close();
		// 不存在则返回空字符串
		return "";
	}
}
