package suzhouhouse.prosceniumgui.entity;

import java.util.ArrayList;
import java.util.List;

public class HouseUseMap {
	 /**
     * 房屋用途
     */
    public static List<String> model=new ArrayList<String>();
    static{
    	 model.add("住宅");
         model.add("非住宅");
         model.add("其他");
    }
}
