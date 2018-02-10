package landmarketnetwork.background.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableRowElement;

import landmarketnetwork.background.entity.JSLandFileMessage;
import landmarketnetwork.background.entity.JSLandMessageDetail;
import landmarketnetwork.background.utils.JSLandCommonUtils;
import landmarketnetwork.prosceniumgui.entity.JSLandParams;
import landmarketnetwork.prosceniumgui.maingame.JSLandMainGUI;
import suzhouhouse.background.entity.HouseFileMessage;

public class ZJLandService {
	public static int landNum = 0;
	public JSLandCommonUtils jslcu = new JSLandCommonUtils();

	// 获取详细
	public void getZJLandDetail(JSLandParams jsLandParams, List<JSLandMessageDetail> jsLandList, int num)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		String urlOne = "http://land.zjgtjy.cn/GTJY_ZJ/go_home";
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
		page = page.getElementById("sy_main").getElementsByTagName("a").get(1).click();
		// 获取第一页的六个土地代码
		List<String> landCodeList = getLandCode(num);
		// 遍历
		for (String landCode : landCodeList) {
			// 验证状态
			if (!JSLandMainGUI.ZJLandFlag) {
				return;
			}
			// 执行对应的js
			ScriptResult result = page.executeJavaScript("goRes(" + landCode + ",01)");
			// 获取执行后获取的page
			HtmlPage newPage = (HtmlPage) result.getNewPage();
			// System.out.println(newPage);
			// 找到对应的基本信息
			DomNodeList<DomElement> he = newPage.getElementsByTagName("td");
			for (DomElement temp : he) {
				if (temp.getAttribute("class").equals("td_line2")) {
					// 此时已获取第一条信息
					HtmlTable ht = (HtmlTable) temp.getElementsByTagName("table").get(0);

					// 传递table循环取数据
					boolean isSuccess = setZJLandMeg(jsLandList, ht, landCode);
				}
			}
		}

	}

	// 获取关键码,相当于每个公告有个代码
	private List<String> getLandCode(int pageNum)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		List<String> landCodeList = new ArrayList<String>();
		String url = "http://land.zjgtjy.cn/GTJY_ZJ/deala_js_action?resourcelb=01&dealtype=&JYLB=&JYFS=&JYZT=&RESOURCENO=&RESOURCEMC=&endDate=&ZYWZ=&zylb=01&currentPage="
				+ pageNum;
		// 模拟浏览器操作
		// 创建WebClient
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		// 关闭css代码功能
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		// 如若有可能找不到文件js则加上这句代码
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		// 获取网页html
		HtmlPage page = webClient.getPage(url);
		DomNodeList<DomElement> he = page.getElementsByTagName("input");
		for (DomElement temp : he) {
			if (temp.getAttribute("class").equals("list1_btn1")) {
				String s = temp.asXml().trim().toString().replaceAll(" ", "");
				String ss = s.substring(s.indexOf("goRes('") + 7, s.indexOf("goRes('") + 11);
				landCodeList.add(ss);
				// 5200
			}
		}
		return landCodeList;
	}

	// 取数据
	private boolean setZJLandMeg(List<JSLandMessageDetail> jsLandList, HtmlTable ht, String landCode)
			throws IndexOutOfBoundsException, IOException {
		// 验证状态
		if (!JSLandMainGUI.ZJLandFlag) {
			return false;
		}
		// 创建实体类对象
		JSLandMessageDetail jslmd = new JSLandMessageDetail();
		landNum++;
		System.out.println(landNum);
		int rowNum = ht.getRowCount();

		// 添加获取的数据到实体类中
		// 编号

		// 新方法
		// 遍历table下所有的tr，td，然后判断，写入，以防止遗漏
		// 遍历行
		jslmd.setId(landNum);
		HtmlTableRow row;
		String label = "";
		String content = "";
		for (int i = 0; i < rowNum; i++) {
			// 遍历列
			row = ht.getRow(i);
			for (int j = 0; j < row.getCells().size(); j = j + 2) {
				// 得到列标签
				label = row.getCell(j).getTextContent().trim().toString();
				// 得到数据
				content = row.getCell(j + 1).getTextContent().trim().toString();
				if ("竞买人条件".equals(label)) {
					if ("[]".equals(row.getCell(j + 1).getElementsByTagName("div").toString().trim())) {
						jslmd.setBidCondition("");
					} else {
						jslmd.setBidCondition(row.getCell(j + 1).getElementsByTagName("div").get(0).getTextContent()
								.trim().toString());
					}
				}
				setContentByLabel(label, content, jslmd);
			}
			row = null;
			label = "";
			content = "";
		}

		// // // 公告名称
		// // jslmd.setNoticeName("");
		// // // 公告链接
		// // jslmd.setNoticeLink("");
		// // 地块编号
		// jslmd.setParcelNum(ht.getRow(0).getCell(1).getTextContent().trim().toString());
		// // 公告编号
		// // 挂牌起始时间
		// jslmd.setBidStartTime(ht.getRow(0).getCell(3).getTextContent().trim().toString());
		// // 挂牌截止时间
		// jslmd.setBidEndTime(ht.getRow(1).getCell(1).getTextContent().trim().toString());
		// // 报名开始时间
		// jslmd.setSignStartTime(ht.getRow(2).getCell(1).getTextContent().trim().toString());
		// // 报名截止
		// jslmd.setSignEndTime(ht.getRow(3).getCell(1).getTextContent().trim().toString());
		// // 保证金开始时间
		//
		// // 保证金到账截止时间
		// jslmd.setPaymentEndTime(ht.getRow(2).getCell(3).getTextContent().trim().toString().replaceAll("
		// ", ""));
		// // 土地权属单位
		// // 交易方式
		// // 地块名称
		// jslmd.setParcelName(ht.getRow(4).getCell(1).getTextContent().trim().toString());
		// // 土地位置
		// jslmd.setLandPosition(ht.getRow(5).getCell(1).getTextContent().trim().toString());
		// // 土地用途
		// jslmd.setLandUse(ht.getRow(6).getCell(1).getTextContent().trim().toString().replaceAll("
		// ", ""));
		// // 竞价规则
		//
		// // 是否有底价
		// jslmd.setYnBasePrice(ht.getRow(3).getCell(3).getTextContent().trim().toString());
		//
		// if (rowNum == 15) {
		// // 交易方式
		// jslmd.setRemiseType("挂牌");
		// // 所属行政区
		// jslmd.setXzqDm(ht.getRow(8).getCell(1).getTextContent().trim().toString());
		// // 出让面积
		// jslmd.setRemiseArea(ht.getRow(8).getCell(3).getTextContent().trim().toString().replaceAll("
		// ", ""));
		// // 建筑面积
		//
		// // 出让年限
		// jslmd.setUseYear(ht.getRow(9).getCell(1).getTextContent().trim().toString());
		// // 竞买保证金
		// jslmd.setBail(ht.getRow(10).getCell(1).getTextContent().trim().toString());
		// // 起始价
		// jslmd.setStartPrice(ht.getRow(9).getCell(3).getTextContent().trim().toString().replaceAll("
		// ", ""));
		// // 竞价增价幅度
		// jslmd.setBidScope(ht.getRow(10).getCell(3).getTextContent().trim().toString());
		// // 出价方式
		// // 出价单位
		// // 特定竞价方式
		// // 最高限价
		// // 容积率
		// jslmd.setRjl(ht.getRow(7).getCell(1).getTextContent().trim().toString());
		// // 绿化率
		// // 建筑密度
		// // 建筑限高（米）
		// // 办公与服务设施用地比例（%）
		// // 固定资产投资强度
		// jslmd.setLowestInvest(ht.getRow(11).getCell(1).getTextContent().trim().toString());
		// // 建设内容
		// // 竞买人条件
		// if
		// ("[]".equals(ht.getRow(12).getCell(1).getElementsByTagName("div").toString().trim()))
		// {
		// jslmd.setBidCondition("");
		// } else {
		// jslmd.setBidCondition(
		// ht.getRow(12).getCell(1).getElementsByTagName("div").get(0).getTextContent().trim().toString());
		// }
		// // 联系人
		// jslmd.setLinkMan(ht.getRow(13).getCell(1).getTextContent().trim().toString());
		// // 联系人电话
		// jslmd.setLinkManTel(ht.getRow(13).getCell(3).getTextContent().trim().toString());
		// // 联系人地址
		// jslmd.setLinkManAdd(ht.getRow(14).getCell(1).getTextContent().trim().toString());
		// } else if (rowNum == 14) {
		// // 交易方式
		// jslmd.setRemiseType("拍卖");
		// // 所属行政区
		// jslmd.setXzqDm(ht.getRow(7).getCell(1).getTextContent().trim().toString());
		// // 出让面积
		// jslmd.setRemiseArea(ht.getRow(7).getCell(3).getTextContent().trim().toString());
		// // 建筑面积
		//
		// // 出让年限
		// jslmd.setUseYear(ht.getRow(8).getCell(1).getTextContent().trim().toString());
		// // 竞买保证金
		// jslmd.setBail(ht.getRow(9).getCell(1).getTextContent().trim().toString());
		// // 起始价
		// jslmd.setStartPrice(ht.getRow(8).getCell(3).getTextContent().trim().toString());
		// // 竞价增价幅度
		// jslmd.setBidScope(ht.getRow(9).getCell(3).getTextContent().trim().toString());
		// // 出价方式
		// // 出价单位
		// // 特定竞价方式
		// // 最高限价
		// // 容积率
		// jslmd.setRjl(ht.getRow(10).getCell(1).getTextContent().trim().toString());
		// // 绿化率
		// // 建筑密度
		// // 建筑限高（米）
		// // 办公与服务设施用地比例（%）
		// // 固定资产投资强度
		// jslmd.setLowestInvest(ht.getRow(10).getCell(3).getTextContent().trim().toString());
		// // 建设内容
		// // 竞买人条件
		// if
		// ("[]".equals(ht.getRow(11).getCell(1).getElementsByTagName("div").toString().trim()))
		// {
		// jslmd.setBidCondition("");
		// } else {
		// jslmd.setBidCondition(
		// ht.getRow(11).getCell(1).getElementsByTagName("div").get(0).getTextContent().trim().toString());
		// }
		// // jslmd.setBidCondition(
		// //
		// ht.getRow(11).getCell(1).getElementsByTagName("div").get(0).getTextContent().trim().toString());
		// // 联系人
		// jslmd.setLinkMan(ht.getRow(12).getCell(1).getTextContent().trim().toString());
		// // 联系人电话
		// jslmd.setLinkManTel(ht.getRow(12).getCell(3).getTextContent().trim().toString());
		// // 联系人地址
		// jslmd.setLinkManAdd(ht.getRow(13).getCell(1).getTextContent().trim().toString());
		//
		// }

		// 获取其他内容需要另想办法
		// http://land.zjgtjy.cn/GTJY_ZJ/crgg_info?rid=5185&JYFS=01
		// 尚未找到好办法
		getOtherLandDetail(jslmd, landCode);
		jsLandList.add(jslmd);
		// 处理下载附件

		return true;
	}

	// 填充信息
	private void setContentByLabel(String label, String content, JSLandMessageDetail jslmd) {
		if ("地块编号".equals(label)) {
			jslmd.setParcelNum(content);
		} else if ("挂牌起始时间".equals(label) || "拍卖开始时间".equals(label)) {
			jslmd.setBidStartTime(content);
		} else if ("挂牌截止时间".equals(label) || "限时竞价开始时间".equals(label)) {
			jslmd.setBidEndTime(content);
		} else if ("报名开始时间".equals(label)) {
			jslmd.setSignStartTime(content);
		} else if ("保证金到账截止时间".equals(label)) {
			jslmd.setPaymentEndTime(content.replaceAll(" ", ""));
		} else if ("报名截止时间".equals(label)) {
			jslmd.setSignEndTime(content);
		} else if ("是否有底价".equals(label)) {
			jslmd.setYnBasePrice(content);
		} else if ("地块名称".equals(label)) {
			jslmd.setParcelName(content);
		} else if ("土地位置".equals(label)) {
			jslmd.setLandPosition(content);
		} else if ("土地用途".equals(label)) {
			jslmd.setLandUse(content.replaceAll(" ", ""));
		} else if ("容积率".equals(label)) {
			jslmd.setRjl(content);
		} else if ("所属行政区".equals(label)) {
			jslmd.setXzqDm(content);
		} else if ("出让面积".equals(label)) {
			jslmd.setRemiseArea(content.replaceAll(" ", ""));
		} else if ("出让年限".equals(label)) {
			jslmd.setUseYear(content);
		} else if ("起始价".equals(label)) {
			jslmd.setStartPrice(content.replaceAll(" ", ""));
		} else if ("竞买保证金".equals(label)) {
			jslmd.setBail(content);
		} else if ("竞价增价幅度".equals(label)) {
			jslmd.setBidScope(content);
		} else if ("固定资产投资强度".equals(label)) {
			jslmd.setLowestInvest(content);
		} else if ("联系人".equals(label)) {
			jslmd.setLinkMan(content);
		} else if ("联系人地址".equals(label)) {
			jslmd.setLinkManAdd(content);
		} else if ("联系人电话".equals(label)) {
			jslmd.setLinkManTel(content);
		} else if ("最高限价".equals(label)) {
			jslmd.setMaxLimPrice(content);
		} else if ("配套用房起始面积".equals(label)) {
			jslmd.setPtyfStartArea(content);
		} else if ("投报幅度".equals(label)) {
			jslmd.setTbfuPrice(content);
		} else if ("".equals(label)) {
			jslmd.setOtherMeg(content);
		}

	}

	// 获取其他信息
	private void getOtherLandDetail(JSLandMessageDetail jslmd, String landCode)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 验证状态
		if (!JSLandMainGUI.ZJLandFlag) {
			return;
		}
		String url = "http://land.zjgtjy.cn/GTJY_ZJ/crgg_info?rid=" + landCode + "&JYFS=01";
		// 模拟浏览器操作
		// 创建WebClient
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		// 关闭css代码功能
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		// 如若有可能找不到文件js则加上这句代码
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		// try {
		// Thread.sleep(5000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// 获取网页html
		HtmlPage page = webClient.getPage(url);
		// try {
		// Thread.sleep(5000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// System.out.println(page.asText());

		// 公告名称
		jslmd.setNoticeName("");
		// 公告链接
		jslmd.setNoticeLink(url);
		// 公告编号
		jslmd.setAfficheNum("");
		// 保证金交纳开始时间
		// jslmd.setPaymentStartTime();
		// 土地权属单位
		// jslmd.setRemiseUnit();

		// 竞价规则
		// jslmd.setBidRules();
		// 建筑面积（平方米）
		jslmd.setConstructArea("");
		// 出价方式
		// jslmd.setCjfs();
		// 出价单位
		// jslmd.setCjdw();
		// 特定竞价方式
		// jslmd.setCrxzfs();
		// 最高限价
		// jslmd.setZgxj();
		// 绿化率
		jslmd.setLhl("");
		// 建筑密度
		jslmd.setJzmd("");
		// 建筑限高（米）
		jslmd.setJzxg("");
		// 办公与服务设施用地比例（%）
		// jslmd.setSharePercent();
		// 建设内容
		// jslmd.setBuildMatter();

	}

	public XSSFWorkbook createExcelFile(List<JSLandMessageDetail> jsLandList) {
		// 验证状态
		if (!JSLandMainGUI.ZJLandFlag) {
			return null;
		}
		String[] title = { "序号", "编号", "公告名称", "公告地块编号", "公告编号", "挂牌/拍卖起始时间", "挂牌截止/限时竞价开始时间", "报名开始时间", "报名截止时间",
				"保证金到账截止时间", "交易方式", "地块名称", "土地位置", "所属行政区", "出让面积（平方米）", "建筑面积（平方米）", "规划用途", "出让年限（年）", "竞买保证金（万元）",
				"起始价", "竞价幅度", "联系人", "联系人电话", "联系人地址", "容积率", "绿化率", "建筑密度", "建筑限高（米）", "投资强度", "竞买人条件", "最高限价",
				"配套用房起始面积", "投报幅度", "其他信息", "公告链接" };
		// 创建Excel工作
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 创建一个sheet表
		XSSFSheet sheet = workbook.createSheet();
		// 设置列宽
		sheet.setColumnWidth(2, 80 * 256);
		sheet.setColumnWidth(3, 15 * 256);
		sheet.setColumnWidth(4, 30 * 256);
		sheet.setColumnWidth(5, 25 * 256);
		sheet.setColumnWidth(6, 25 * 256);
		sheet.setColumnWidth(7, 25 * 256);
		sheet.setColumnWidth(8, 25 * 256);
		sheet.setColumnWidth(9, 25 * 256);
		sheet.setColumnWidth(10, 10 * 256);
		sheet.setColumnWidth(11, 30 * 256);
		sheet.setColumnWidth(12, 30 * 256);
		sheet.setColumnWidth(13, 15 * 256);
		sheet.setColumnWidth(14, 30 * 256);
		sheet.setColumnWidth(15, 10 * 256);
		sheet.setColumnWidth(16, 30 * 256);
		sheet.setColumnWidth(17, 10 * 256);
		sheet.setColumnWidth(18, 10 * 256);
		sheet.setColumnWidth(19, 20 * 256);
		sheet.setColumnWidth(20, 15 * 256);
		sheet.setColumnWidth(21, 15 * 256);
		sheet.setColumnWidth(22, 15 * 256);
		sheet.setColumnWidth(23, 40 * 256);
		sheet.setColumnWidth(24, 20 * 256);
		sheet.setColumnWidth(25, 15 * 256);
		sheet.setColumnWidth(26, 15 * 256);
		sheet.setColumnWidth(27, 15 * 256);
		sheet.setColumnWidth(28, 10 * 256);
		sheet.setColumnWidth(29, 70 * 256);
		sheet.setColumnWidth(30, 20 * 256);
		sheet.setColumnWidth(31, 20 * 256);
		sheet.setColumnWidth(32, 20 * 256);
		sheet.setColumnWidth(33, 20 * 256);
		sheet.setColumnWidth(34, 80 * 256);

		// 创建第一行
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell;
		// 插入第一行数据
		for (int i = 0; i < title.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(title[i]);
		}
		// 追加数据,向第二行开始加入数据 i = 1
		for (int i = 1; i < jsLandList.size() + 1; i++) {
			XSSFRow row2 = sheet.createRow(i);
			XSSFCell cell2 = row2.createCell(0);
			cell2.setCellValue(i);
			cell2 = row2.createCell(1);
			cell2.setCellValue(jsLandList.get(i - 1).getId());
			cell2 = row2.createCell(2);
			cell2.setCellValue(jsLandList.get(i - 1).getNoticeName());
			cell2 = row2.createCell(3);
			cell2.setCellValue(jsLandList.get(i - 1).getParcelNum());
			cell2 = row2.createCell(4);
			cell2.setCellValue(jsLandList.get(i - 1).getAfficheNum());
			cell2 = row2.createCell(5);
			cell2.setCellValue(jsLandList.get(i - 1).getBidStartTime());
			cell2 = row2.createCell(6);
			cell2.setCellValue(jsLandList.get(i - 1).getBidEndTime());
			cell2 = row2.createCell(7);
			cell2.setCellValue(jsLandList.get(i - 1).getSignStartTime());
			cell2 = row2.createCell(8);
			cell2.setCellValue(jsLandList.get(i - 1).getSignEndTime());
			cell2 = row2.createCell(9);
			cell2.setCellValue(jsLandList.get(i - 1).getPaymentEndTime());
			cell2 = row2.createCell(10);
			cell2.setCellValue(jsLandList.get(i - 1).getRemiseType());
			cell2 = row2.createCell(11);
			cell2.setCellValue(jsLandList.get(i - 1).getParcelName());
			cell2 = row2.createCell(12);
			cell2.setCellValue(jsLandList.get(i - 1).getLandPosition());
			cell2 = row2.createCell(13);
			cell2.setCellValue(jsLandList.get(i - 1).getXzqDm());
			cell2 = row2.createCell(14);
			cell2.setCellValue(jsLandList.get(i - 1).getRemiseArea());
			cell2 = row2.createCell(15);
			cell2.setCellValue(jsLandList.get(i - 1).getConstructArea());
			cell2 = row2.createCell(16);
			cell2.setCellValue(jsLandList.get(i - 1).getLandUse());
			cell2 = row2.createCell(17);
			cell2.setCellValue(jsLandList.get(i - 1).getUseYear());
			cell2 = row2.createCell(18);
			cell2.setCellValue(jsLandList.get(i - 1).getBail());
			cell2 = row2.createCell(19);
			cell2.setCellValue(jsLandList.get(i - 1).getStartPrice());
			cell2 = row2.createCell(20);
			cell2.setCellValue(jsLandList.get(i - 1).getBidScope());
			cell2 = row2.createCell(21);
			cell2.setCellValue(jsLandList.get(i - 1).getLinkMan());
			cell2 = row2.createCell(22);
			cell2.setCellValue(jsLandList.get(i - 1).getLinkManTel());
			cell2 = row2.createCell(23);
			cell2.setCellValue(jsLandList.get(i - 1).getLinkManAdd());
			cell2 = row2.createCell(24);
			cell2.setCellValue(jsLandList.get(i - 1).getRjl());
			cell2 = row2.createCell(25);
			cell2.setCellValue(jsLandList.get(i - 1).getLhl());
			cell2 = row2.createCell(26);
			cell2.setCellValue(jsLandList.get(i - 1).getJzmd());
			cell2 = row2.createCell(27);
			cell2.setCellValue(jsLandList.get(i - 1).getJzxg());
			cell2 = row2.createCell(28);
			cell2.setCellValue(jsLandList.get(i - 1).getLowestInvest());
			cell2 = row2.createCell(29);
			cell2.setCellValue(jsLandList.get(i - 1).getBidCondition());
			cell2 = row2.createCell(30);
			cell2.setCellValue(jsLandList.get(i - 1).getMaxLimPrice());
			cell2 = row2.createCell(31);
			cell2.setCellValue(jsLandList.get(i - 1).getPtyfStartArea());
			cell2 = row2.createCell(32);
			cell2.setCellValue(jsLandList.get(i - 1).getTbfuPrice());
			cell2 = row2.createCell(33);
			cell2.setCellValue(jsLandList.get(i - 1).getOtherMeg());
			cell2 = row2.createCell(34);
			cell2.setCellValue(jsLandList.get(i - 1).getNoticeLink());
		}

		// 写入文件
		return workbook;
	}

	public JSLandFileMessage writeExcelFile(XSSFWorkbook workbook, String path) {
		// 验证状态
		if (!JSLandMainGUI.ZJLandFlag) {
			return null;
		}
		JSLandFileMessage jsfm = new JSLandFileMessage();
		FileOutputStream fos = null;
		String date = jslcu.turnHouseDate();
		jslcu.createFolder(path);
		String lastFilePath = path + "\\浙江省国有建设用地挂牌出让公告" + date + ".xls";
		try {
			fos = new FileOutputStream(lastFilePath);
			workbook.write(fos);
			fos.flush();
			System.out.println("存盘完成！");
			jsfm.setBl(true);
			jsfm.setLastFilePath(lastFilePath);
			return jsfm;
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
		jsfm.setBl(false);
		jsfm.setLastFilePath(lastFilePath);
		return jsfm;
	}

	// 筛选
	public void checkJSLandList(List<JSLandMessageDetail> jsLandList, JSLandParams jsLandParams) {
		// 迭代器循环
		// 不能使用普通循环
		Iterator<JSLandMessageDetail> it = jsLandList.iterator();
		while (it.hasNext()) {
			JSLandMessageDetail jslmd = it.next();
			// 格式化时间为19700101
			String paymentEndTime = jslcu.formatPaymentTime2(jslmd.getPaymentEndTime());
			// 格式化面积
			String remiseArea = jslcu.formatRemiseArea2(jslmd.getRemiseArea());
			// 格式化起始价格
			String startPrice = jslcu.formatStartPrice2(jslmd.getStartPrice(), remiseArea);

			if (Integer.parseInt(jsLandParams.getMinDate()) > Integer.parseInt(paymentEndTime)
					|| Integer.parseInt(jsLandParams.getMaxDate()) < Integer.parseInt(paymentEndTime)
					|| Double.parseDouble(jsLandParams.getMinPrice()) > Double.parseDouble(startPrice)
					|| Double.parseDouble(jsLandParams.getMaxPrice()) < Double.parseDouble(startPrice)
					|| Double.parseDouble(jsLandParams.getMinLandArea()) > Double.parseDouble(remiseArea)
					|| Double.parseDouble(jsLandParams.getMaxLandArea()) < Double.parseDouble(remiseArea)) {
				it.remove();
				continue;
			}
			if (!"不限".equals(jsLandParams.getLandUse())) {
				if ("商业".equals(jsLandParams.getLandUse())) {
					if (!jslmd.getLandUse().contains("业")) {
						it.remove();
					} else if (jslmd.getLandUse().contains("工业") && !jslmd.getLandUse().contains("商业")) {
						it.remove();
					}
				} else if ("商住".equals(jsLandParams.getLandUse())) {
					if (!jslmd.getLandUse().contains("住")) {
						it.remove();
					}
				} else if ("住宅".equals(jsLandParams.getLandUse())) {
					if (!jslmd.getLandUse().contains("住")) {
						it.remove();
					}
				} else if ("其他".equals(jsLandParams.getLandUse())) {
					if (jslmd.getLandUse().contains("住")
							|| (jslmd.getLandUse().contains("业") && !jslmd.getLandUse().contains("工业"))) {
						it.remove();
					}
				}

//				if (!jslmd.getLandUse().contains(jsLandParams.getLandUse())) {
//					if ("商住".equals(jsLandParams.getLandUse())) {
//						if (!jslmd.getLandUse().contains("住")) {
//							it.remove();
//						}
//					} else {
//						it.remove();
//					}
//
//				}
			}

		}
	}
}
