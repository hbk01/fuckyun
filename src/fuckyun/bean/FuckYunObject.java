package fuckyun.bean;

import fuckyun.listener.UpListener;
import fuckyun.main.FuckYun;
import fuckyun.network.NetWork;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * 2019/6/8 11:53
 */
public class FuckYunObject implements Serializable {
    public void save(String table, UpListener listener){
        ArrayList<String> keys = FuckYun.getKeys(this.getClass());
        ArrayList<String> values = FuckYun.getValues(this);
        NetWork.insert(table, keys, values, listener);
    }

    // TODO：完成删除，修改，不做查询
}
