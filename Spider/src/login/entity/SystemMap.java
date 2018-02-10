package login.entity;

import java.util.ArrayList;
import java.util.List;

public class SystemMap {
	 /**
     * 系统种类
     */
    public static List<String> model=new ArrayList<String>();
    static{
        model.add(Constants.TYPE_TAOBAOJUD);
        model.add(Constants.TYPE_SUZHOUHOUSE);
        model.add(Constants.TYPE_BAIDUWENKU);
        model.add(Constants.TYPE_BOOKMANAGE);
        model.add(Constants.TYPE_JSLANDNETWORK);
        model.add(Constants.TYPE_HNLANDTRADE);
        model.add(Constants.TYPE_ZJLANDTRADE);
    }
}
