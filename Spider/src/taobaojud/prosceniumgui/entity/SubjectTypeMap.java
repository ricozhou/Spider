package taobaojud.prosceniumgui.entity;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
 
public class SubjectTypeMap {
    /**
     * 标的物类型
     */
    public static List<String> model=new ArrayList<String>();
    static{
        model.add("不限");
        model.add("房产");
        model.add("机动车");
        model.add("其他");
        model.add("资产");
        model.add("土地");
        model.add("股权");
        model.add("林权");
        model.add("无形资产");
        model.add("船舶");
        model.add("矿权");
        model.add("工程");
    }
}