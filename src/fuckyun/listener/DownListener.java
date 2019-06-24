package fuckyun.listener;

import fuckyun.main.Result;

/**
 * 查询数据
 * 2019/6/8 11:18
 */
public interface DownListener<T> {
    void onDown(Result<T> result);
}
