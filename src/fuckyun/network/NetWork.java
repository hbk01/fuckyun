package fuckyun.network;

import com.google.gson.*;
import fuckyun.bean.FuckYunObject;
import fuckyun.listener.DownListener;
import fuckyun.listener.UpListener;
import fuckyun.main.Result;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 所有的网络请求从这里发送出去
 * 2019/6/8 11:29
 */
public class NetWork {
    /**
     * UA
     */
    private static final String USER_AGENT = "FuckYun";
    /**
     * id，此id须在id池中存在
     */
    public static String id = "";
    /**
     * fuckyun.php所在的链接
     */
    public static String url = "";
    /**
     * 操作的表名
     */
    public static String table = "";
    /**
     * 请求头
     */
    private static final HashMap<String, String> header = new HashMap<>();
    /**
     * 请求参数
     */
    private static final HashMap<String, String> params = new HashMap<>();

    /**
     * 初始化请求头
     */
    static {
        header.put("User-Agent", USER_AGENT);
    }

    /**
     * 所有的动作
     */
    private static class Method {
        /**
         * 创建表
         */
        static final String CREATE_TABLE = "create_table";

        /**
         * 插入数据
         */
        static final String INSERT = "insert";

        /**
         * 更新数据
         */
        static final String UPDATE = "update";

        /**
         * 删除数据
         */
        static final String DELETE = "delete";

        /**
         * 删除所有数据
         */
        static final String DELETE_ALL_DATA = "delete_all_data";

        /**
         * 删除表
         */
        static final String DELETE_TABLE = "delete_table";

        /**
         * 查询表
         */
        static final String QUERY = "query";

        /**
         * 上传文件
         */
        static final String UPLOAD_FILE = "upload_file";
    }

    // 以下的字段是在参数设置中经常用到的
    private static final String METHOD = "method";
    private static final String TABLE = "table";
    private static final String WHERE = "where";
    private static final String LIMIT = "limit";
    private static final String KEYS = "keys";
    private static final String VALUES = "values";

    public static void insert(String table, ArrayList<String> keys, ArrayList<String> values, UpListener listener) {
        resetParams();
        params.put(METHOD, Method.INSERT);
        params.put(TABLE, table);
        params.put(KEYS, keys.toString());
        params.put(VALUES, values.toString());
        Http.doPost(url, params, header, new Http.Ok() {
            @Override
            public void ok(String msg) {
                listener.onUp(getCode(msg), getMsg(msg));
            }
        });
    }

    public static void createTable(String table, ArrayList<String> keys, UpListener listener) {
        resetParams();
        params.put(METHOD, Method.CREATE_TABLE);
        params.put(TABLE, table);
        params.put(KEYS, keys.toString());
        Http.doPost(url, params, header, msg -> {
            listener.onUp(getCode(msg), getMsg(msg));
        });
    }

    public static void update(String table, ArrayList<String> keys, ArrayList<String> values, String where, UpListener listener) {
        resetParams();
        params.put(METHOD, Method.UPDATE);
        params.put(TABLE, table);
        params.put(KEYS, keys.toString());
        params.put(VALUES, values.toString());
        params.put(WHERE, where);
        Http.doPost(url, params, header, msg -> {
            listener.onUp(getCode(msg), getMsg(msg));
        });
    }

    public static void delete(String table, String where, UpListener listener) {
        resetParams();
        params.put(METHOD, Method.DELETE);
        params.put(TABLE, table);
        params.put(WHERE, where);
        Http.doPost(url, params, header, msg -> {
            listener.onUp(getCode(msg), getMsg(msg));
        });
    }

    public static void deleteAllData(String table, UpListener listener) {
        resetParams();
        params.put(METHOD, Method.DELETE_ALL_DATA);
        params.put(TABLE, table);
        Http.doPost(url, params, header, msg -> {
            listener.onUp(getCode(msg), getMsg(msg));
        });
    }

    public static void deleteTable(String table, UpListener listener) {
        resetParams();
        params.put(METHOD, Method.DELETE_TABLE);
        params.put(TABLE, table);
        Http.doPost(url, params, header, msg -> {
            listener.onUp(getCode(msg), getMsg(msg));
        });
    }

    public static <T> void query(String table, String where, String limit, Class<? extends FuckYunObject> type, DownListener<T> listener) {
        resetParams();
        params.put(METHOD, Method.QUERY);

        if (!(table == null || "".equals(table))){
            params.put(TABLE, table);
        } else {
            params.put(TABLE, NetWork.table);
        }

        if (!(where == null || "".equals(where)))
            params.put(WHERE, where);

        if (!(limit == null || "".equals(limit)))
            params.put(LIMIT, limit);

        Http.doPost(url, params, header, msg -> {
            System.out.println(msg);
            JsonElement parser = new JsonParser().parse(msg);
            JsonObject object = parser.getAsJsonObject();
            // 获取code和msg
            int code = object.get("code").getAsInt();
            String string = object.get("msg").getAsString();
            // 获取data
            ArrayList<T> list = new ArrayList<>();
            JsonArray data = object.getAsJsonArray("data");
            for (JsonElement element : data) {
                T t = new Gson().fromJson(element, (Type) type);
                list.add(t);
            }
            Result<T> result = new Result<T>(code, string, list);
            listener.onDown(result);
        });
    }

    public static void uploadFile(LinkedList<File> files, UpListener listener){
        resetParams();
        params.put(METHOD, Method.UPLOAD_FILE);
        Http.doPostFile(url, params, files, msg -> {
            listener.onUp(getCode(msg), getMsg(msg));
        });
    }

    private static int getCode(String msg) {
        // {"code":0,"msg":"信息"}
        try {
            int index = msg.indexOf("\"code\":") + 7;
            String code = msg.substring(index, msg.indexOf(",\"msg\""));
            return Integer.parseInt(code);
        } catch (StringIndexOutOfBoundsException e) {
            return -1;
        }
    }

    private static String getMsg(String msg) {
        try {
            int index = msg.indexOf("\"msg\":\"") + 7;
            byte[] result = msg.substring(index, msg.indexOf("\"}")).getBytes();
            return new String(result);
        } catch (StringIndexOutOfBoundsException e) {
            return msg;
        }
    }

    private static void resetParams() {
        params.clear();
        params.put("initID", id);
    }

}
