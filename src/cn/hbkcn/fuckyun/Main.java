package cn.hbkcn.fuckyun;

import fuckyun.listener.DownListener;
import fuckyun.listener.UpListener;
import fuckyun.main.FuckYun;
import fuckyun.network.Http;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 测试
 */
public class Main {
    private static String url = "http://39.108.149.133/test/fuckyun.php";
    private static String id = "1";
    private static String table = User.class.getSimpleName();

    public static void main(String[] args) {
        FuckYun fuckYun = new FuckYun(id, url, table);

        UpListener upListener = (code, msg) -> System.out.println(code + ": " + msg);
        DownListener<User> downListener = System.out::println;

//        LinkedList<File> files = new LinkedList<>();
//        files.add(new File("C:\\Users\\hbk\\Desktop\\ssr.txt"));
//        files.add(new File("C:\\Users\\hbk\\Desktop\\宝塔.txt"));
//        fuckYun.uploadFile(files, upListener);


        LinkedList<File> files = new LinkedList<>();
        files.add(new File("C:\\Users\\hbk\\Desktop\\ssr.txt"));
        files.add(new File("C:\\Users\\hbk\\Pictures\\视频项目\\Windows7.png"));
        files.add(new File("D:\\students.json"));

        fuckYun.uploadFile(files, upListener);


//

//        Where where = new Where();
//        // username LIKE 0004%
//        where.add("username", "0004%", Where.Type.LIKE);
//
//        // LIMIT 5
//        Limit limit = new Limit(5);
//
//        // 查询
//        fuckYun.query(where, limit, User.class, (DownListener<User>) result -> {
//            int code = result.getCode();
//            String message = result.getMessage();
//            System.out.println(code + ": " + message);
//            ArrayList<User> data = result.getData();
//            for (User user : data) {
//                System.out.println(user.getUsername() + user.getPassword());
//            }
//        });

//         删除对象
//        fuckYun.delete(where, listener);
//         删除所有数据
//        fuckYun.deleteAllData(listener);
//         删除表
//        fuckYun.deleteTable(listener);
//
//         更新
//        fuckYun.update(user, where, listener);

//         创建表
//        fuckYun.createTable(User.class, listener);

//         保存
//        fuckYun.save(user, listener);

//         保存
//        user.save(table, listener);
    }

}
