package fuckyun.main;

import java.util.*;

/**
 * 由此类生成where子句
 * 2019/6/10 21:53
 */
public class Where {
    private LinkedHashMap<String, String> map = new LinkedHashMap<>();
    private ArrayList<Type> type = new ArrayList<>();

    public enum Type{
        /**
         * LIKE
         */
        LIKE,

        /**
         * =
         */
        EQUAL
    }

    /**
     * 添加一个where条件
     * @param key 键
     * @param value 值
     * @param type 类型，equal或者like
     */
    public void add(String key, String value, Type type){
        this.map.put(key, value);
        this.type.add(type);
    }

    @Override
    public String toString() {
        if (map.size()==0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = map.keySet().iterator();
        for (int i = 0; i < type.size(); i++) {
            if (iterator.hasNext()) {
                String key = iterator.next();
                String value = map.get(key);
                builder.append(key);
                builder.append(type.get(i).equals(Type.EQUAL) ? "=" : " LIKE ");
                builder.append("\"" + value + "\"");
                builder.append(" AND ");
            }
        }
        // 删除最后一个AND
        builder.delete(builder.length() - " AND ".length(), builder.length());
        return builder.toString();
    }
}
