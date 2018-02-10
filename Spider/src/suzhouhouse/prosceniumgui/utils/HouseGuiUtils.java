package suzhouhouse.prosceniumgui.utils;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import suzhouhouse.prosceniumgui.entity.AreaMap;
import suzhouhouse.prosceniumgui.entity.HouseQueryParams;
import suzhouhouse.prosceniumgui.entity.HouseShowParams;
import suzhouhouse.prosceniumgui.entity.HouseUseMap;
import suzhouhouse.prosceniumgui.entity.PermitPresaleParams;

public class HouseGuiUtils {

	public Object[] getAreaType() {
		List<String> list = AreaMap.model;
		Object[] houseArea = list.toArray();
		return houseArea;
	}

	public Object[] getHouseUseType() {
		List<String> list = HouseUseMap.model;
		Object[] houseUse = list.toArray();
		return houseUse;
	}

	public boolean checkHouseQueryParams(HouseQueryParams houseQueryParams) {

		return true;
	}

	public boolean checkPermitPresaleParams(PermitPresaleParams permitPresaleParams) {
		// 页数要是数字且不能0开头
		if (!permitPresaleParams.getPageNumber().matches("^[0-9]*$")) {
			return false;
		} else if ("0".equals(permitPresaleParams.getPageNumber().substring(0, 1))) {
			return false;
		}
		return true;
	}

	// 可售楼盘展示参数校验
	public boolean checkHouseShowParams(HouseShowParams houseShowParams) {
		// 字符不能过长
		if (houseShowParams.getProjectName().length() > 100 || houseShowParams.getCompanyName().length() > 100) {
			return false;
		}
		// 页数要是数字且不能0开头
		if (!houseShowParams.getPageNumber().matches("^[0-9]*$")) {
			return false;
		} else if ("0".equals(houseShowParams.getPageNumber().substring(0, 1))) {
			return false;
		}
		return true;
	}

	// 系统默认浏览器打开网址
	public void openUrlByBrowser(String url) {
		URI uri = null;
		try {
			uri = new URI(url);
			Desktop.getDesktop().browse(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
