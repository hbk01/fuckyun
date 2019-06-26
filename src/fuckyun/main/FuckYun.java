package fuckyun.main;

import fuckyun.bean.FuckYunObject;
import fuckyun.bean.Name;
import fuckyun.listener.DownListener;
import fuckyun.listener.UpListener;
import fuckyun.network.NetWork;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;

// TODO: lambda转换成常规方式，兼容jdk1.8以下

/**
 * 操蛋云主类，将请求封装并发送
 * 2019/6/8 10:07
 */
public class FuckYun {
    private String table;

    /**
     * 初始化FuckYun
     * @param initID 初始化id
     * @param url fuckyun.php的url连接
     */
    public FuckYun(String initID, String url){
        NetWork.id = initID;
        NetWork.url = url;
    }

    /**
     * 初始化FuckYun，并确定默认使用的表
     * @param initID 初始化id
     * @param url fuckyun.php的url连接
     * @param table 表名
     */
    public FuckYun(String initID, String url, String table) {
        NetWork.id = initID;
        NetWork.url = url;
        NetWork.table = table;
        this.table = table;
    }

    /**
     * 创建一个数据表
     * @param objectClass 继承FuckYunObject的类，依据此类的字段来创建表的字段
     * @param listener 监听器
     */
    public void createTable(Class<? extends FuckYunObject> objectClass, UpListener listener){
        ArrayList<String> keys = getKeys(objectClass);
        NetWork.createTable(table, keys, listener);
    }

    /**
     * 保存一个对象
     * @param object 对象
     * @param listener 监听器
     */
    public void save(FuckYunObject object, UpListener listener){
        ArrayList<String> keys = getKeys(object.getClass());
        ArrayList<String> values = getValues(object);
        NetWork.insert(table, keys, values, listener);
    }

    /**
     * 更新一个对象的数据
     * @param object 服务器的数据将更新为此对象的数据
     * @param where 选择器，确定哪些数据要被更新
     * @param listener 监听器
     */
    public void update(FuckYunObject object, Where where, UpListener listener){
        ArrayList<String> keys = getKeys(object.getClass());
        ArrayList<String> list = getValues(object);
        ArrayList<String> values = new ArrayList<>();
        // 不往两边加引号会出错
        for (String value : list) {
            values.add("\"" + value + "\"");
        }
        NetWork.update(table, keys, values, where.toString(), listener);
    }

    /**
     * 删除表中的数据。若要删除表中所有的数据，应当使用deleteAllData方法
     * @param where 选择器，确定哪些数据将被删除
     * @param listener 监听器
     */
    public void delete(Where where, UpListener listener){
        if (where == null){
            listener.onUp(100, "Unsafe Operation.");
            return;
        }
        NetWork.delete(table, where.toString(), listener);
    }

    /**
     * 删除表中所有的数据
     * @param listener 监听器
     */
    public void deleteAllData(UpListener listener){
        NetWork.deleteAllData(table, listener);
    }

    /**
     * 删除表结构，与此同时该表的所有数据将被删除
     * @param listener 监听器
     */
    public void deleteTable(UpListener listener){
        NetWork.deleteTable(table, listener);
    }

    /* 查询数据的泛型弄得我发懵 */

    /**
     * 查询数据
     * @param where 选择器，确定返回哪些数据
     * @param limit 数量限定
     * @param type 将返回此类型的对象
     * @param listener 监听器
     * @param <T> 需与type类型相同
     */
    public <T> void query(Where where, Limit limit, Class<? extends FuckYunObject> type, DownListener<T> listener){
        NetWork.query(table, where.toString(), limit.toString(), type, listener);
    }

    /**
     * 查询数据
     * @param where 选择器，确定返回哪些数据
     * @param type 将返回此类型的对象
     * @param listener 监听器
     * @param <T> 需与type类型相同
     */
    public <T> void query(Where where, Class<? extends FuckYunObject> type, DownListener<T> listener){
        NetWork.query(table, where.toString(), "", type, listener);
    }

    /**
     * 查询数据
     * @param limit 数量限定
     * @param type 将返回此类型的对象
     * @param listener 监听器
     * @param <T> 需与type类型相同
     */
    public <T> void query(Limit limit, Class<? extends FuckYunObject> type, DownListener<T> listener){
        NetWork.query(table, "", limit.toString(), type, listener);
    }

    /**
     * 查询数据
     * @param type 将返回此类型的对象
     * @param listener 监听器
     * @param <T> 需与type类型相同
     */
    public <T> void query(Class<? extends FuckYunObject> type, DownListener<T> listener){
        NetWork.query(table, "", "", type, listener);
    }

    /**
     * 上传一个文件
     * @param file 文件
     * @param listener 监听器
     */
    public void uploadFile(File file, UpListener listener){
        LinkedList<File> list = new LinkedList<>();
        list.add(file);
        NetWork.uploadFile(list, listener);
    }

    /**
     * 上传多个文件
     * @param files 文件列表
     * @param listener 监听器
     */
    public void uploadFile(LinkedList<File> files, UpListener listener){
        NetWork.uploadFile(files, listener);
    }

    /**
     * 通过反射获取所有字段
     * @param objectClass 继承FuckYunObject的类
     * @return 字段
     */
    public static ArrayList<String> getKeys(Class<? extends FuckYunObject> objectClass){
        Field[] fields = objectClass.getDeclaredFields();
        ArrayList<String> keys = new ArrayList<>();

        // 获取所有的key
        for (Field field : fields) {
            field.setAccessible(true);
            Name annotation = field.getAnnotation(Name.class);
            // 如果使用了注解就用注解的值
            if (annotation != null) {
                keys.add(annotation.value());
            }
            keys.add(field.getName());
        }
        return keys;
    }

    /**
     * 通过反射获取所有字段的值
     * @param object 已被实例化的对象
     * @return 字段的值，若object为null则返回空集
     */
    public static ArrayList<String> getValues(FuckYunObject object){
        if (object == null) {
            return new ArrayList<>();
        }
        ArrayList<String> values = new ArrayList<>();
        Class<? extends FuckYunObject> aClass = object.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField != null) {
                declaredField.setAccessible(true);
                try {
                    String value = String.valueOf(declaredField.get(object));
                    values.add(value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return values;
    }

    /**
     * 使用此表
     * @param table 表名
     */
    public void useTable(String table) {
        NetWork.table = table;
        this.table = table;
    }
}
