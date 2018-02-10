package taobaojud.prosceniumgui.entity;

import java.util.ArrayList;
import java.util.List;

public class SubjectStatusMap {
	 /**
     * 标的物拍卖状态
     */
    public static List<String> model=new ArrayList<String>();
    static{
        model.add("不限");
        model.add("正在进行");
        model.add("即将开始");
        model.add("已结束");
        model.add("中止");
        model.add("撤回");
    }
}
