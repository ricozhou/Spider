package taobaojud.prosceniumgui.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import taobaojud.prosceniumgui.entity.CityMap;
import taobaojud.prosceniumgui.entity.SubjectStatusMap;
import taobaojud.prosceniumgui.entity.SubjectTypeMap;

public class GuiUtils {
	// 获取标的物状态
	public Object[] getSubjectStatus() {
		List<String> list = SubjectStatusMap.model;
		Object[] subjectStatus = list.toArray();
		return subjectStatus;
	}

	// 获取标的物类型
	public Object[] getSubjectType() {
		List<String> list = SubjectTypeMap.model;
		Object[] subjectType = list.toArray();
		return subjectType;
	}

	/**
	 * 获取省、直辖市，自治区
	 */
	public Object[] getProvince() {
		Map<String, String[]> map = CityMap.model;// 获取省份信息保存到Map中
		Set<String> set = map.keySet(); // 获取Map集合中的键，并以Set集合返回
		Object[] province = set.toArray(); // 转换为数组
		return province; // 返回获取的省份信息
	}

	/**
	 * 获取指定省对应的市/县
	 */
	public String[] getCity(String selectProvince) {
		Map<String, String[]> map = CityMap.model; // 获取省份信息保存到Map中
		String[] arrCity = map.get(selectProvince); // 获取指定键的值
		return arrCity; // 返回获取的市/县
	}
}
