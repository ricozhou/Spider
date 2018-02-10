package suzhouhouse.background.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import suzhouhouse.background.entity.HouseFileMessage;
import suzhouhouse.background.entity.ReturnHouseMessage;
import suzhouhouse.background.entity.houseshow.HouseShowMessage;
import suzhouhouse.background.service.HouseShowService;
import suzhouhouse.background.utils.HouseCommonUtils;
import suzhouhouse.prosceniumgui.entity.HouseShowParams;
import suzhouhouse.prosceniumgui.maingame.SZHmainGui;
import suzhouhouse.prosceniumgui.utils.HouseGuiUtils;

public class HouseShowController {
	public HouseCommonUtils hsu = new HouseCommonUtils();
	public HouseShowService hss = new HouseShowService();

	// 爬取可售楼盘展示主方法
	public ReturnHouseMessage houseShowController(HouseShowParams houseShowParams)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 验证状态
		if (!SZHmainGui.houseShowFlag) {
			return null;
		}
		long startTime = System.currentTimeMillis();
		// 返回值
		ReturnHouseMessage rhm = new ReturnHouseMessage();
		// 模式一模式二都一样
		// 先获取条数,页数
		int[] pageNum = hsu.getPageAndNum(houseShowParams);
		// 页数超过了则重置,根据模式选择
		if (Integer.parseInt(houseShowParams.getPageNumber()) > pageNum[1] || houseShowParams.getSelectPattern() == 0) {
			houseShowParams.setPageNumber(String.valueOf(pageNum[1]));
		}
		if (pageNum[0] == 0) {
			// 没数据则返回
			return null;
		}
		int num = Integer.parseInt(houseShowParams.getPageNumber());
		// 存入数据
		// HouseShowMessage hsm=new HouseShowMessage();
		List<HouseShowMessage> houseList = new ArrayList<HouseShowMessage>();
		String[] area = { "吴中区", "相城区", "高新区", "姑苏区", "吴江区" };
		try {
			for (int i = 0; i < num; i++) {
				hss.getHouseShowDetail(houseShowParams, houseList, i);
			}
			// 如果爬取全部，则需要更改区域
			if (houseShowParams.isAllData) {
				for (int k = 0; k < area.length; k++) {
					houseShowParams.setProjectArea(area[k]);
					for (int i = 0; i < num; i++) {
						hss.getHouseShowDetail(houseShowParams, houseList, i);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 创建Excel
			XSSFWorkbook workbook = hss.createExcelFile(houseList);
			// 写入Excel
			HouseFileMessage hfm = hss.writeExcelFile(workbook, houseShowParams);
			long endTime = System.currentTimeMillis();
			rhm.setHfm(hfm);
			rhm.setYNMessage("出了点小问题！未完全完成！");
			rhm.setYNsuccess("否");
			rhm.setDate(hsu.turnHouseDate2(startTime, endTime));
			return rhm;
		}
		// 创建Excel
		XSSFWorkbook workbook = hss.createExcelFile(houseList);
		// 写入Excel
		HouseFileMessage hfm = hss.writeExcelFile(workbook, houseShowParams);
		long endTime = System.currentTimeMillis();
		rhm.setHfm(hfm);
		rhm.setYNMessage("完成");
		rhm.setYNsuccess("是");
		rhm.setDate(hsu.turnHouseDate2(startTime, endTime));
		return rhm;
	}
}
