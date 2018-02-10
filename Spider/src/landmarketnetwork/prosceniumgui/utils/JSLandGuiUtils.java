package landmarketnetwork.prosceniumgui.utils;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import landmarketnetwork.prosceniumgui.entity.JSLandParams;

public class JSLandGuiUtils {

	public boolean checkJSLandParams(JSLandParams jsLandParams) {
		// 校验时间
		String aDate = "[0-9]{4}[0-1]{1}[0-9]{1}[0-3]{1}[0-9]{1}";

		// 只能是数字固定格式如19700205
		if (!jsLandParams.getMinDate().matches("^[0-9]*$") || !jsLandParams.getMaxDate().matches("^[0-9]*$")) {
			return false;
		} else if (Integer.parseInt(jsLandParams.getMinDate()) >= Integer.parseInt(jsLandParams.getMaxDate())) {
			return false;
		} else if (jsLandParams.getMinDate().length() != 8 || jsLandParams.getMaxDate().length() != 8) {
			return false;
		} else if (!Pattern.compile(aDate).matcher(jsLandParams.getMinDate()).matches()
				|| !Pattern.compile(aDate).matcher(jsLandParams.getMaxDate()).matches()) {
			return false;
		}
		String strMinDate = jsLandParams.getMinDate();
		String strMaxDate = jsLandParams.getMaxDate();
		if (Integer.parseInt(strMinDate.substring(4, 6)) > 12 || Integer.parseInt(strMaxDate.substring(4, 6)) > 12
				|| Integer.parseInt(strMinDate.substring(6, 8)) > 31
				|| Integer.parseInt(strMaxDate.substring(6, 8)) > 31) {
			return false;
		}

		// 校验价格
		if (!jsLandParams.getMinPrice().matches("^[0-9]*$") || !jsLandParams.getMaxPrice().matches("^[0-9]*$")) {
			return false;
		} else if (Integer.parseInt(jsLandParams.getMinPrice()) >= Integer.parseInt(jsLandParams.getMaxPrice())) {
			return false;
		} else if (jsLandParams.getMinPrice().length() > 9 || jsLandParams.getMaxPrice().length() > 9) {
			return false;
		}
		// 校验面积
		if (!jsLandParams.getMinLandArea().matches("^[0-9]*$") || !jsLandParams.getMaxLandArea().matches("^[0-9]*$")) {
			return false;
		} else if (Integer.parseInt(jsLandParams.getMinLandArea()) >= Integer.parseInt(jsLandParams.getMaxLandArea())) {
			return false;
		} else if (jsLandParams.getMinLandArea().length() > 9 || jsLandParams.getMaxLandArea().length() > 9) {
			return false;
		}
		// 校验页数
		if (!jsLandParams.getPageNum().matches("^[0-9]*$") || jsLandParams.getPageNum().length() > 9
				|| "".equals(jsLandParams.getPageNum())) {
			return false;
		}
		if (!jsLandParams.getMinpageNum().matches("^[0-9]*$") || jsLandParams.getMinpageNum().length() > 9
				|| "".equals(jsLandParams.getMinpageNum())) {
			return false;
		}
		if (Integer.parseInt(jsLandParams.getPageNum()) < Integer.parseInt(jsLandParams.getMinpageNum())) {
			return false;
		}
		return true;
	}

	// 系统默认浏览器打开网址
	public void openUrlByBrowser(String landUrl) {
		URI uri = null;
		try {
			uri = new URI(landUrl);
			Desktop.getDesktop().browse(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
