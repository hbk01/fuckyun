package fuckyun.main;

import java.util.ArrayList;

/**
 * 服务器返回的数据
 * 2019/6/15 18:55
 */
public class Result<T> {
    private int code;
    private String msg;
    private ArrayList<T> data;

    public Result(int code, String msg, ArrayList<T> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(code);
        buffer.append(": ");
        buffer.append(msg);
        buffer.append(System.lineSeparator());
        buffer.append(data);
        buffer.append(System.lineSeparator());
        return buffer.toString();
    }
}


