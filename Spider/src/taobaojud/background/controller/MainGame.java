package taobaojud.background.controller;

import java.io.IOException;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import suzhouhouse.prosceniumgui.maingame.SZHmainGui;
import taobaojud.background.entity.FileMessage;
import taobaojud.background.entity.ReturnMessage;
import taobaojud.background.entity.SimpleJudSaleInfo;
import taobaojud.background.service.AnalyzeUrl;
import taobaojud.prosceniumgui.entity.Params;
import taobaojud.prosceniumgui.maingame.TaoBaoMainUI;

public class MainGame {
	public AnalyzeUrl au = new AnalyzeUrl();

	public ReturnMessage mainProcess(Params param) throws IOException {
		// 验证状态
		if (!TaoBaoMainUI.taoBaoJudFlag) {
			return null;
		}
		ReturnMessage rm = new ReturnMessage();
		// 获取对应网址，通过配置文件，时间短
		// String url=gp.getOneProperty(param.getProvince(),param.getCity());
		// 通过解析网站获取，时间较长
		String url = au.getOneUrl(param);
		if ("".equals(url)) {
			rm.setYNsuccess("失败");
			rm.setYNMessage("URL为空或地址不正确！");
			return rm;
		}
		// 获取简单数据
		String str = au.startAnalyzeUrl(url);
		if (str == null) {
			rm.setYNsuccess("失败");
			rm.setYNMessage("网络连接失败！请检查网络！");
			return rm;
		} else if ("".equals(str)) {
			rm.setYNsuccess("失败");
			rm.setYNMessage("抱歉！没有数据！");
			return rm;
		}
		// 存入list集合
		List<SimpleJudSaleInfo> sjsiList = au.processData(str, param);
		// 创建Excel
		XSSFWorkbook workbook = au.createExcelFile(sjsiList);
		// 输出文件
		FileMessage fm = au.writeExcelFile(workbook, param);
		if (fm.isBl() == true) {
			rm.setFm(fm);
			rm.setYNsuccess("成功");
		} else {
			rm.setFm(fm);
			rm.setYNsuccess("失败");
			rm.setYNMessage("请检查网络或者输出地址！");
		}
		return rm;
	}
}
