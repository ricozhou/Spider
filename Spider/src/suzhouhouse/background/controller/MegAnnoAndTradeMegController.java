package suzhouhouse.background.controller;

import suzhouhouse.background.entity.HouseFileMessage;
import suzhouhouse.background.entity.ReturnHouseMessage;
import suzhouhouse.background.entity.MegAnnoAndTradeModel.MessageAnnoModel;
import suzhouhouse.background.entity.MegAnnoAndTradeModel.TradeMessageModel;
import suzhouhouse.background.service.MessageAnnoService;
import suzhouhouse.background.service.TradeMessageService;
import suzhouhouse.background.utils.HouseCommonUtils;
import suzhouhouse.prosceniumgui.entity.MegAnnoAndTradeMegParams;
import suzhouhouse.prosceniumgui.maingame.SZHmainGui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

public class MegAnnoAndTradeMegController {
	public static final String MEAAAGE_ANNOA_URL = "http://spf.szfcweb.com/szfcweb/DataSerach/CanSaleHouseGroIndex.aspx";
	public static final String TRADE_MESSAGE_URL = "http://www.szfcweb.com/szfcweb/DataSerach/XSFWINFO.aspx";
	public MessageAnnoService mas = new MessageAnnoService();
	public TradeMessageService tms = new TradeMessageService();
	public HouseCommonUtils hsu = new HouseCommonUtils();

	// 可售信息公示查询主方法
	public ReturnHouseMessage megAnnoController(MegAnnoAndTradeMegParams maatmp)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		long startTime = System.currentTimeMillis();
		// 返回值
		ReturnHouseMessage rhm = new ReturnHouseMessage();
		// 信息列表存储
		List<MessageAnnoModel> messList = new ArrayList<MessageAnnoModel>();
		mas.getMessageAnnoDetail(MegAnnoAndTradeMegController.MEAAAGE_ANNOA_URL, messList);
		// 创建Excel
		XSSFWorkbook workbook = mas.createExcelFile(messList);
		// 写入Excel
		HouseFileMessage hfm = mas.writeExcelFile(workbook, maatmp);
		long endTime = System.currentTimeMillis();
		rhm.setHfm(hfm);
		rhm.setYNMessage("完成");
		rhm.setYNsuccess("是");
		rhm.setDate(hsu.turnHouseDate2(startTime, endTime));
		return rhm;
	}

	// 即时信息查询主方法
	public ReturnHouseMessage tradeMegController(MegAnnoAndTradeMegParams maatmp) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		long startTime = System.currentTimeMillis();
		// 返回值
		ReturnHouseMessage rhm = new ReturnHouseMessage();
		// 信息列表存储
		TradeMessageModel tmm = new TradeMessageModel();
		tms.getTradeMessageDetail(MegAnnoAndTradeMegController.TRADE_MESSAGE_URL, tmm);
		// 创建Excel
		XSSFWorkbook workbook = tms.createExcelFile(tmm);
		// 写入Excel
		HouseFileMessage hfm = tms.writeExcelFile(workbook, maatmp,tmm.getTradeName());
		long endTime = System.currentTimeMillis();
		rhm.setHfm(hfm);
		rhm.setYNMessage("完成");
		rhm.setYNsuccess("是");
		rhm.setDate(hsu.turnHouseDate2(startTime, endTime));
		return rhm;
	}
}
