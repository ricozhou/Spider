package landmarketnetwork.background.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import landmarketnetwork.background.entity.JSLandFileMessage;
import landmarketnetwork.background.entity.JSLandMessageDetail;
import landmarketnetwork.background.utils.JSLandCommonUtils;
import landmarketnetwork.prosceniumgui.entity.JSLandParams;
import suzhouhouse.background.entity.HouseFileMessage;

public class HNLandService {
	public static int landNum = 0;
	public JSLandCommonUtils jslcu = new JSLandCommonUtils();

	// 获取详细
	public void getJSLandDetail(JSLandParams jsLandParams, List<JSLandMessageDetail> jsLandList, int num)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
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
		// 但要区分需要第几页，由于直接点击select不起作用，所以只能循环点击
		for (int i = 0; i < num; i++) {
			System.out.println(i);
			page = page.getElementById("AspNetPager1").getElementsByTagName("div").get(1).getElementsByTagName("a")
					.get(3).click();
		}
		// 这下彻底获取目标页的html了
		// 传递page循环取数据
		boolean isSuccess = setJSLandMeg(jsLandList, page);

	}

	// 取数据
	private boolean setJSLandMeg(List<JSLandMessageDetail> jsLandList, HtmlPage page)
			throws IndexOutOfBoundsException, IOException {
		HtmlTable ht = (HtmlTable) page.getElementById("GridView1");
		int n = ht.getRowCount();
		HtmlPage pg;
		String notice;
		String noticeLink;
		for (int i = 1; i < n; i++) {
			// 获取第一个公告（里面可能包含好几个）
			pg = ht.getRow(i).getCell(0).getElementsByTagName("a").get(0).click();
			notice = ht.getRow(i).getCell(0).getElementsByTagName("a").get(0).getTextContent().trim().toString();
			noticeLink = pg.getBaseURI();
			HtmlTable ht2 = (HtmlTable) pg.getElementById("GridView1");
			int n2 = ht2.getRowCount();
			// 循环取出里面的公告
			for (int j = 1; j < n2; j++) {
				// 点击进入详情页面
				HtmlPage pg2 = ht2.getRow(j).getCell(3).getElementsByTagName("a").get(0).click();
				// 获取详细信息存入实体类
				getJSLandDetail(jsLandList, pg2, notice, noticeLink);
			}
		}
		return true;
	}

	// 获取详细信息
	private void getJSLandDetail(List<JSLandMessageDetail> jsLandList, HtmlPage pg, String notice, String noticeLink) {
		JSLandMessageDetail jslmd = new JSLandMessageDetail();
		landNum++;
		// 编号
		jslmd.setId(landNum);
		// 公告名称
		jslmd.setNoticeName(notice);
		// 公告链接
		jslmd.setNoticeLink(noticeLink);
		// 公告地块编号
		jslmd.setParcelNum(pg.getElementById("PARCEL_NO").getTextContent().trim().toString());
		// 公告编号
		jslmd.setAfficheNum(pg.getElementById("AFFICHE_NO").getTextContent().trim().toString());
		// 挂牌起始时间
		jslmd.setBidStartTime(pg.getElementById("BID_STARTTIME").getTextContent().trim().toString());
		// 挂牌截止时间
		jslmd.setBidEndTime(pg.getElementById("BID_ENDTIME").getTextContent().trim().toString());
		// 报名开始时间
		jslmd.setSignStartTime(pg.getElementById("SIGN_STARTTIME").getTextContent().trim().toString());
		// 报名截止时间
		jslmd.setSignEndTime(pg.getElementById("SIGN_ENDTIME").getTextContent().trim().toString());
		// 保证金交纳开始时间
		jslmd.setPaymentStartTime(pg.getElementById("PAYMENT_STARTTIME").getTextContent().trim().toString());
		// 保证金到账截止时间
		jslmd.setPaymentEndTime(pg.getElementById("PAYMENT_ENDTIME").getTextContent().trim().toString());
		// 土地权属单位
		jslmd.setRemiseUnit(pg.getElementById("REMISE_UNIT").getTextContent().trim().toString());
		// 交易方式
		jslmd.setRemiseType(pg.getElementById("REMISE_TYPE").getTextContent().trim().toString());
		// 地块名称
		jslmd.setParcelName(pg.getElementById("PARCEL_NAME").getTextContent().trim().toString());
		// 土地位置
		jslmd.setLandPosition(pg.getElementById("LAND_POSITION").getTextContent().trim().toString());
		// 竞价规则
		jslmd.setBidRules(pg.getElementById("BID_RULES").getTextContent().trim().toString());
		// 所属行政区
		jslmd.setXzqDm(pg.getElementById("XZQ_DM").getTextContent().trim().toString()
				+ pg.getElementById("XZQ_DM0").getTextContent().trim().toString());
		// 出让面积（平方米）
		jslmd.setRemiseArea(pg.getElementById("REMISE_AREA").getTextContent().trim().toString());
		// 建筑面积（平方米）
		jslmd.setConstructArea(pg.getElementById("CONSTRUCT_AREA").getTextContent().trim().toString());
		// 规划用途
		jslmd.setLandUse(pg.getElementById("LAND_USE").getTextContent().trim().toString());
		// 出让年限（年）
		jslmd.setUseYear(pg.getElementById("USE_YEAR").getTextContent().trim().toString());
		// 竞买保证金（万元）
		jslmd.setBail(pg.getElementById("BAIL").getTextContent().trim().toString());
		// 起始价
		jslmd.setStartPrice(pg.getElementById("START_PRICE").getTextContent().trim().toString());
		// 竞价幅度
		jslmd.setBidScope(pg.getElementById("BID_SCOPE").getTextContent().trim().toString());
		// 出价方式
		jslmd.setCjfs(pg.getElementById("CJFS").getTextContent().trim().toString());
		// 出价单位
		jslmd.setCjdw(pg.getElementById("CJDW").getTextContent().trim().toString());
		// 特定竞价方式
		jslmd.setCrxzfs(pg.getElementById("CRXZFS").getTextContent().trim().toString());
		// 最高限价
		jslmd.setZgxj(pg.getElementById("ZGXJ").getTextContent().trim().toString());
		// 容积率
		jslmd.setRjl(pg.getElementById("rjl").getTextContent().trim().toString());
		// 绿化率
		jslmd.setLhl(pg.getElementById("lhl").getTextContent().trim().toString());
		// 建筑密度
		jslmd.setJzmd(pg.getElementById("jzmd").getTextContent().trim().toString());
		// 建筑限高（米）
		jslmd.setJzxg(pg.getElementById("jzxg").getTextContent().trim().toString());
		// 办公与服务设施用地比例（%）
		jslmd.setSharePercent(pg.getElementById("SHAREPERCENT").getTextContent().trim().toString());
		// 投资强度
		jslmd.setLowestInvest(pg.getElementById("LOWESTINVEST").getTextContent().trim().toString());
		// 建设内容
		jslmd.setBuildMatter(pg.getElementById("BUILDMATTER").getTextContent().trim().toString());
		jsLandList.add(jslmd);

		// 处理下载附件

	}

	public XSSFWorkbook createExcelFile(List<JSLandMessageDetail> jsLandList) {
		String[] title = { "序号", "编号", "公告名称", "公告地块编号", "公告编号", "挂牌起始时间", "挂牌截止时间", "报名开始时间", "报名截止时间", "保证金交纳开始时间",
				"保证金到账截止时间", "土地权属单位", "交易方式", "地块名称", "土地位置", "竞价规则", "所属行政区", "出让面积（平方米）", "建筑面积（平方米）", "规划用途",
				"出让年限（年）", "竞买保证金（万元）", "起始价", "竞价幅度", "出价方式", "出价单位", "特定竞价方式", "最高限价", "容积率", "绿化率", "建筑密度",
				"建筑限高（米）", "办公与服务设施用地比例（%）", "投资强度", "建设内容", "公告链接" };
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
		sheet.setColumnWidth(10, 25 * 256);
		sheet.setColumnWidth(11, 25 * 256);
		sheet.setColumnWidth(12, 10 * 256);
		sheet.setColumnWidth(13, 30 * 256);
		sheet.setColumnWidth(14, 30 * 256);
		sheet.setColumnWidth(15, 10 * 256);
		sheet.setColumnWidth(16, 15 * 256);
		sheet.setColumnWidth(17, 10 * 256);
		sheet.setColumnWidth(18, 10 * 256);
		sheet.setColumnWidth(19, 10 * 256);
		sheet.setColumnWidth(20, 10 * 256);
		sheet.setColumnWidth(21, 10 * 256);
		sheet.setColumnWidth(22, 20 * 256);
		sheet.setColumnWidth(23, 15 * 256);
		sheet.setColumnWidth(24, 15 * 256);
		sheet.setColumnWidth(25, 15 * 256);
		sheet.setColumnWidth(26, 10 * 256);
		sheet.setColumnWidth(27, 10 * 256);
		sheet.setColumnWidth(28, 20 * 256);
		sheet.setColumnWidth(29, 15 * 256);
		sheet.setColumnWidth(30, 15 * 256);
		sheet.setColumnWidth(31, 15 * 256);
		sheet.setColumnWidth(32, 10 * 256);
		sheet.setColumnWidth(33, 10 * 256);
		sheet.setColumnWidth(34, 10 * 256);
		sheet.setColumnWidth(35, 80 * 256);

		// sheet.setColumnWidth(0, 6 * 256);
		// sheet.setColumnWidth(1, 6 * 256);
		// sheet.setColumnWidth(2, 18 * 256);
		// sheet.setColumnWidth(3, 10 * 256);
		// sheet.setColumnWidth(4, 30 * 256);
		// sheet.setColumnWidth(5, 50 * 256);
		// sheet.setColumnWidth(6, 20 * 256);
		// sheet.setColumnWidth(7, 16 * 256);
		// sheet.setColumnWidth(8, 20 * 256);
		// sheet.setColumnWidth(9, 15 * 256);
		// sheet.setColumnWidth(10, 50 * 256);
		// sheet.setColumnWidth(11, 20 * 256);
		// sheet.setColumnWidth(12, 20 * 256);
		// sheet.setColumnWidth(13, 20 * 256);
		// sheet.setColumnWidth(14, 50 * 256);
		// sheet.setColumnWidth(15, 20 * 256);
		// sheet.setColumnWidth(16, 20 * 256);
		// sheet.setColumnWidth(17, 20 * 256);
		// sheet.setColumnWidth(18, 15 * 256);
		// sheet.setColumnWidth(19, 15 * 256);
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
			cell2.setCellValue(jsLandList.get(i - 1).getPaymentStartTime());
			cell2 = row2.createCell(10);
			cell2.setCellValue(jsLandList.get(i - 1).getPaymentEndTime());
			cell2 = row2.createCell(11);
			cell2.setCellValue(jsLandList.get(i - 1).getRemiseUnit());
			cell2 = row2.createCell(12);
			cell2.setCellValue(jsLandList.get(i - 1).getRemiseType());
			cell2 = row2.createCell(13);
			cell2.setCellValue(jsLandList.get(i - 1).getParcelName());
			cell2 = row2.createCell(14);
			cell2.setCellValue(jsLandList.get(i - 1).getLandPosition());
			cell2 = row2.createCell(15);
			cell2.setCellValue(jsLandList.get(i - 1).getBidRules());
			cell2 = row2.createCell(16);
			cell2.setCellValue(jsLandList.get(i - 1).getXzqDm());
			cell2 = row2.createCell(17);
			cell2.setCellValue(jsLandList.get(i - 1).getRemiseArea());
			cell2 = row2.createCell(18);
			cell2.setCellValue(jsLandList.get(i - 1).getConstructArea());
			cell2 = row2.createCell(19);
			cell2.setCellValue(jsLandList.get(i - 1).getLandUse());
			cell2 = row2.createCell(20);
			cell2.setCellValue(jsLandList.get(i - 1).getUseYear());
			cell2 = row2.createCell(21);
			cell2.setCellValue(jsLandList.get(i - 1).getBail());
			cell2 = row2.createCell(22);
			cell2.setCellValue(jsLandList.get(i - 1).getStartPrice());
			cell2 = row2.createCell(23);
			cell2.setCellValue(jsLandList.get(i - 1).getBidScope());
			cell2 = row2.createCell(24);
			cell2.setCellValue(jsLandList.get(i - 1).getCjfs());
			cell2 = row2.createCell(25);
			cell2.setCellValue(jsLandList.get(i - 1).getCjdw());
			cell2 = row2.createCell(26);
			cell2.setCellValue(jsLandList.get(i - 1).getCrxzfs());
			cell2 = row2.createCell(27);
			cell2.setCellValue(jsLandList.get(i - 1).getZgxj());
			cell2 = row2.createCell(28);
			cell2.setCellValue(jsLandList.get(i - 1).getRjl());
			cell2 = row2.createCell(29);
			cell2.setCellValue(jsLandList.get(i - 1).getLhl());
			cell2 = row2.createCell(30);
			cell2.setCellValue(jsLandList.get(i - 1).getJzmd());
			cell2 = row2.createCell(31);
			cell2.setCellValue(jsLandList.get(i - 1).getJzxg());
			cell2 = row2.createCell(32);
			cell2.setCellValue(jsLandList.get(i - 1).getSharePercent());
			cell2 = row2.createCell(33);
			cell2.setCellValue(jsLandList.get(i - 1).getLowestInvest());
			cell2 = row2.createCell(34);
			cell2.setCellValue(jsLandList.get(i - 1).getBuildMatter());
			cell2 = row2.createCell(35);
			cell2.setCellValue(jsLandList.get(i - 1).getNoticeLink());
		}

		// 写入文件
		return workbook;
	}

	public JSLandFileMessage writeExcelFile(XSSFWorkbook workbook, String path) {
		JSLandFileMessage jsfm = new JSLandFileMessage();
		FileOutputStream fos = null;
		String date = jslcu.turnHouseDate();
		jslcu.createFolder(path);
		String lastFilePath = path + "\\江苏土地市场网经营性用地出让公告" + date + ".xls";
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
			String paymentEndTime = jslcu.formatPaymentTime(jslmd.getPaymentEndTime());
			// 格式化起始价格
			String startPrice = jslcu.formatStartPrice(jslmd.getStartPrice());
			if (Integer.parseInt(jsLandParams.getMinDate()) > Integer.parseInt(paymentEndTime)
					|| Integer.parseInt(jsLandParams.getMaxDate()) < Integer.parseInt(paymentEndTime)
					|| Double.parseDouble(jsLandParams.getMinPrice()) > Double.parseDouble(startPrice)
					|| Double.parseDouble(jsLandParams.getMaxPrice()) < Double.parseDouble(startPrice)
					|| Double.parseDouble(jsLandParams.getMinLandArea()) > Double.parseDouble(jslmd.getRemiseArea())
					|| Double.parseDouble(jsLandParams.getMaxLandArea()) < Double.parseDouble(jslmd.getRemiseArea())) {
				it.remove();
				continue;
			}
			if (!"不限".equals(jsLandParams.getLandUse())) {
				if (!jslmd.getLandUse().contains(jsLandParams.getLandUse())) {
					it.remove();
				}
			}

		}
	}
}
