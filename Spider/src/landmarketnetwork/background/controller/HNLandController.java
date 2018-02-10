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
import landmarketnetwork.background.utils.JSLandCommonUtils;
import landmarketnetwork.prosceniumgui.entity.JSLandParams;
import suzhouhouse.background.entity.HouseFileMessage;
import suzhouhouse.background.entity.houseshow.HouseShowMessage;

public class HNLandController {
	public JSLandCommonUtils jslcu = new JSLandCommonUtils();
	public JSLandService jsls = new JSLandService();

	public ReturnJSLandMessage JSLandController(JSLandParams jsLandParams)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		long startTime = System.currentTimeMillis();
		ReturnJSLandMessage rjslm = new ReturnJSLandMessage();
		// 先获取条数,页数
		int[] pageNum = jslcu.getPageAndNum(jsLandParams);
		if (Integer.parseInt(jsLandParams.getPageNum()) > pageNum[1]) {
			jsLandParams.setPageNum(String.valueOf(pageNum[1]));
		}
		if (pageNum[0] == 0) {
			// 没数据则返回
			return null;
		}
		int num = Integer.parseInt(jsLandParams.getPageNum());
		// 存入数据
		List<JSLandMessageDetail> jsLandList = new ArrayList<JSLandMessageDetail>();
		try {
			for (int i = 0; i < num; i++) {
				jsls.getJSLandDetail(jsLandParams, jsLandList, i);
			}
		} catch (Exception e) {
			e.printStackTrace();
			//筛选List
			//为防止意外，筛选前先存到缓存
			jsls.writeExcelFile(jsls.createExcelFile(jsLandList), "cacheFile\\JSLandcacheFile");
			//筛选
			jsls.checkJSLandList(jsLandList,jsLandParams);
			// 创建Excel
			XSSFWorkbook workbook = jsls.createExcelFile(jsLandList);
			// 写入Excel
			JSLandFileMessage jslfm = jsls.writeExcelFile(workbook, jsLandParams.getFilePath());
			jsLandList.clear();
			workbook.close();
			long endTime = System.currentTimeMillis();
			rjslm.setJslfm(jslfm);
			rjslm.setYNMessage("出了点小问题！未完全完成！");
			rjslm.setYNsuccess("否");
			rjslm.setDate(jslcu.turnHouseDate2(startTime, endTime));
			return rjslm;
		}
		
		//筛选List
		//为防止意外，筛选前先存到缓存
		jsls.writeExcelFile(jsls.createExcelFile(jsLandList), "cacheFile\\JSLandcacheFile");
		//筛选
		jsls.checkJSLandList(jsLandList,jsLandParams);
		
		// 创建Excel
		XSSFWorkbook workbook = jsls.createExcelFile(jsLandList);
		// 写入Excel
		JSLandFileMessage jslfm = jsls.writeExcelFile(workbook, jsLandParams.getFilePath());
		jsLandList.clear();
		workbook.close();
		long endTime = System.currentTimeMillis();
		rjslm.setJslfm(jslfm);
		rjslm.setYNMessage("完成");
		rjslm.setYNsuccess("是");
		rjslm.setDate(jslcu.turnHouseDate2(startTime, endTime));
		return rjslm;
	}

	public ReturnJSLandMessage HNLandController(JSLandParams jsLandParams) {
		return null;
	}

}
