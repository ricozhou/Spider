package suzhouhouse.background.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import suzhouhouse.background.entity.HouseFileMessage;
import suzhouhouse.background.entity.ReturnHouseMessage;
import suzhouhouse.background.entity.permitpresale.PermitPresaleMessage;
import suzhouhouse.background.service.PermitPresaleService;
import suzhouhouse.background.utils.HouseCommonUtils;
import suzhouhouse.prosceniumgui.entity.PermitPresaleParams;
import suzhouhouse.prosceniumgui.maingame.SZHmainGui;

public class PermitPresaleController {
	public HouseCommonUtils hsu = new HouseCommonUtils();
	public PermitPresaleService pps = new PermitPresaleService();

	// 爬取预售证主方法
	public ReturnHouseMessage permitPresaleController(PermitPresaleParams permitPresaleParams)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 验证状态
		if (!SZHmainGui.permitPresaleFlag) {
			return null;
		}
		long startTime = System.currentTimeMillis();
		// 返回值
		ReturnHouseMessage rhm = new ReturnHouseMessage();
		// 先获取条数,页数
		int[] pageNum = hsu.getPageAndNum2(permitPresaleParams);
		// 页数超过了则重置,根据模式选择
		if (Integer.parseInt(permitPresaleParams.getPageNumber()) > pageNum[1]) {
			permitPresaleParams.setPageNumber(String.valueOf(pageNum[1]));
		}
		if (pageNum[0] == 0) {
			// 没数据则返回
			return null;
		}
		int num = Integer.parseInt(permitPresaleParams.getPageNumber());

		// 开始采集数据
		List<PermitPresaleMessage> ppmList = new ArrayList<PermitPresaleMessage>();
		try {
			for (int i = 0; i < num; i++) {
				pps.getPermitPresaleDetail(permitPresaleParams, ppmList, i);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 创建Excel
			XSSFWorkbook workbook = pps.createExcelFile(ppmList);
			// 写入Excel
			HouseFileMessage hfm = pps.writeExcelFile(workbook, permitPresaleParams);
			long endTime = System.currentTimeMillis();
			rhm.setHfm(hfm);
			rhm.setYNMessage("出了点小问题！未完全完成！");
			rhm.setYNsuccess("否");
			rhm.setDate(hsu.turnHouseDate2(startTime, endTime));
			return rhm;
		}

		// 创建Excel
		XSSFWorkbook workbook = pps.createExcelFile(ppmList);
		// 写入Excel
		HouseFileMessage hfm = pps.writeExcelFile(workbook, permitPresaleParams);
		long endTime = System.currentTimeMillis();
		rhm.setHfm(hfm);
		rhm.setYNMessage("完成");
		rhm.setYNsuccess("是");
		rhm.setDate(hsu.turnHouseDate2(startTime, endTime));
		return rhm;
	}
}
