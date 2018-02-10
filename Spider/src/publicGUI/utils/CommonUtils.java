package publicGUI.utils;

import java.util.List;

import publicGUI.entity.InterfaceStyleMap;

public class CommonUtils {
	// 获取界面风格类型
		public Object[] getInterfaceStyle() {
			List<String> list = InterfaceStyleMap.model;
			Object[] interfaceStyleMap = list.toArray();
			return interfaceStyleMap;
		}
}
