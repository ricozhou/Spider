package landmarketnetwork.background.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import landmarketnetwork.background.entity.JSLandFileMessage;
import landmarketnetwork.background.entity.JSLandMessageDetail;
import landmarketnetwork.background.entity.ReturnJSLandMessage;
import landmarketnetwork.background.service.JSLandService;
import landmarketnetwork.background.service.ZJLandService;
import landmarketnetwork.background.utils.JSLandCommonUtils;
import landmarketnetwork.prosceniumgui.entity.JSLandParams;
import landmarketnetwork.prosceniumgui.maingame.JSLandMainGUI;
import suzhouhouse.background.entity.HouseFileMessage;
import suzhouhouse.background.entity.houseshow.HouseShowMessage;

public class ZJLandController {
	public JSLandCommonUtils jslcu = new JSLandCommonUtils();
	public ZJLandService zjls = new ZJLandService();

	public ReturnJSLandMessage ZJLandController(JSLandParams jsLandParams)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 验证状态
		if (!JSLandMainGUI.ZJLandFlag) {
			return null;
		}
		long startTime = System.currentTimeMillis();
		ReturnJSLandMessage rjslm = new ReturnJSLandMessage();
		// 先获取条数,页数
		// int[] pageNum = jslcu.getPageAndNum(jsLandParams);
		// if (Integer.parseInt(jsLandParams.getPageNum()) > pageNum[1]) {
		// jsLandParams.setPageNum(String.valueOf(pageNum[1]));
		// }
		// if (pageNum[0] == 0) {
		// // 没数据则返回
		// return null;
		// }
		int num = Integer.parseInt(jsLandParams.getPageNum());
		int minNum = Integer.parseInt(jsLandParams.getMinpageNum());
		if (minNum < 1) {
			minNum = 1;
		}
		// 存入数据
		List<JSLandMessageDetail> jsLandList = new ArrayList<JSLandMessageDetail>();
		try {
			for (int i = minNum - 1; i < num; i++) {
				zjls.getZJLandDetail(jsLandParams, jsLandList, i + 1);
			}
			ZJLandService.landNum = 0;
		} catch (Exception e) {
			e.printStackTrace();
			// 筛选List
			// 为防止意外，筛选前先存到缓存
			zjls.writeExcelFile(zjls.createExcelFile(jsLandList), "cacheFile\\ZJLandcacheFile");
			// 筛选
			zjls.checkJSLandList(jsLandList, jsLandParams);
			// 创建Excel
			XSSFWorkbook workbook = zjls.createExcelFile(jsLandList);
			// 写入Excel
			JSLandFileMessage jslfm = zjls.writeExcelFile(workbook, jsLandParams.getFilePath());
			jsLandList.clear();
			workbook.close();
			long endTime = System.currentTimeMillis();
			rjslm.setJslfm(jslfm);
			rjslm.setYNMessage("出了点小问题！未完全完成！");
			rjslm.setYNsuccess("否");
			rjslm.setDate(jslcu.turnHouseDate2(startTime, endTime));
			return rjslm;
		}

		// 筛选List
		// 为防止意外，筛选前先存到缓存
		zjls.writeExcelFile(zjls.createExcelFile(jsLandList), "cacheFile\\ZJLandcacheFile");
		// 筛选
		zjls.checkJSLandList(jsLandList, jsLandParams);

		// 创建Excel
		XSSFWorkbook workbook = zjls.createExcelFile(jsLandList);
		// 写入Excel
		JSLandFileMessage jslfm = zjls.writeExcelFile(workbook, jsLandParams.getFilePath());
		jsLandList.clear();
		workbook.close();
		long endTime = System.currentTimeMillis();
		rjslm.setJslfm(jslfm);
		rjslm.setYNMessage("完成");
		rjslm.setYNsuccess("是");
		rjslm.setDate(jslcu.turnHouseDate2(startTime, endTime));
		return rjslm;
	}

}
