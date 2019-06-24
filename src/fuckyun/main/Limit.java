package fuckyun.main;

/**
 * 生成SQL语句中的LIMIT子句
 * 2019/6/12 17:35
 */
public class Limit {
    private int offset = 0;
    private int num = 0;

    public Limit(int num) {
        this.num = num;
    }

    public Limit(int offset, int num) {
        this.offset = offset;
        this.num = num;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        if (offset <= 0){
            if (num > 0) return " LIMIT " + num;
        } else {
            if (num > 0) return " LIMIT " + offset + "," + num;
        }
        return "";
    }
}
