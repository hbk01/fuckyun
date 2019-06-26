package fuckyun.network;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * HTTP工具类，提供GET和POST请求的便捷方法。
 * <p>
 * 方法概览：
 * get: 发送GET请求
 * post: 发送POST请求
 * doGet: 在新线程中发送GET请求
 * doPost: 在新线程中发送POST请求
 * parseCookies: 解析cookie字符串
 * parseParams: 解析参数字符串
 * parseHeaders: 解析请求头字符串
 * </p>
 * @author hbk(3243430237 @ qq.com)
 * @version 1.1
 * @see HttpURLConnection
 */
public class Http {
    /* - Get - */

    /**
     * 执行GET请求
     * @param url 请求链接
     * @param ok 回调
     */
    public static void get(String url, Ok ok) {
        get(url, "", null, null, ok);
    }

    /**
     * 执行GET请求
     * @param url 请求链接
     * @param params 参数
     * @param ok 回调
     */
    public static void get(String url, Map<String, String> params, Ok ok) {
        get(url, params, null, null, ok);
    }

    /**
     * 执行GET请求
     * @param url 请求链接
     * @param params 参数，key1=value1&key2=value2形式
     * @param ok 回调
     */
    public static void get(String url, String params, Ok ok) {
        get(url, parseParams(params), null, null, ok);
    }

    /**
     * 执行GET请求
     * @param url 请求链接
     * @param params 参数
     * @param headers 请求头
     * @param ok 回调
     */
    public static void get(String url, Map<String, String> params, Map<String, String> headers, Ok ok) {
        get(url, params, headers, null, ok);
    }

    /**
     * 执行GET请求
     * @param url 请求链接
     * @param params 参数，key1=value1&key2=value2形式
     * @param headers 请求头
     * @param ok 回调
     */
    public static void get(String url, String params, Map<String, String> headers, Ok ok) {
        get(url, parseParams(params), headers, null, ok);
    }

    /**
     * 执行GET请求
     * @param url 请求链接
     * @param params 参数
     * @param headers 请求头
     * @param cookies 携带的cookie
     * @param ok 回调
     */
    public static void get(String url, Map<String, String> params, Map<String, String> headers, Map<String, String> cookies, Ok ok) {
        try {
            // 拼接正确的请求url
            StringBuffer buffer = new StringBuffer(url + "?");
            if (params != null && params.size() != 0) {
                params.forEach((key, value) -> {
                    buffer.append(key);
                    buffer.append("=");
                    buffer.append(value);
                    buffer.append("&");
                });
            }
            // 移除url的最后一个字符("&")
            // 18-12-11： 移到if外面，若param为空时可移除"?"
            buffer.delete(buffer.length() - 1, buffer.length());

            url = buffer.toString();
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            // 使用cookie
            if (cookies != null && cookies.size() != 0) {
                String k = "Cookie";
                StringBuffer v = new StringBuffer();
                cookies.forEach((key, value) -> {
                    v.append(key);
                    v.append("=");
                    v.append(value);
                    v.append(";");
                });
                headers.put(k, v.toString());
            }

            // 设置请求头
            if (headers != null && headers.size() != 0) {
                headers.forEach((key, value) -> {
                    conn.setRequestProperty(key, value);
                });
            }

            int code = conn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == code) {
                InputStream is = conn.getInputStream();
                ok.ok(readInputStream(is));
            } else {
                ok.ok(conn.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行GET请求
     * @param url 请求链接
     * @param params 参数，key1=value1&key2=value2形式
     * @param headers 请求头
     * @param cookies 携带的cookie
     * @param ok 回调
     */
    public static void get(String url, String params, Map<String, String> headers, Map<String, String> cookies, Ok ok) {
        get(url, parseParams(params), headers, cookies, ok);
    }

    /**
     * -
     * 新建一个线程执行GET请求任务。
     * @param url 请求链接
     * @param ok 回调
     */
    public static void doGet(String url, Ok ok) {
        doGet(url, null, null, null, ok);
    }

    /**
     * 新建一个线程执行GET请求任务。
     * @param url 请求链接
     * @param params 参数
     * @param ok 回调
     */
    public static void doGet(String url, Map<String, String> params, Ok ok) {
        doGet(url, params, null, null, ok);
    }

    /**
     * 新建一个线程执行GET请求任务。
     * @param url 请求链接
     * @param params 参数
     * @param headers 请求头
     * @param ok 回调
     */
    public static void doGet(String url, Map<String, String> params, Map<String, String> headers, Ok ok) {
        doGet(url, params, headers, null, ok);
    }

    /**
     * 新建一个线程执行GET请求任务。
     * @param url 请求链接
     * @param params 参数
     * @param headers 请求头
     * @param cookies 携带的cookie
     * @param ok 回调
     */
    public static void doGet(String url, Map<String, String> params, Map<String, String> headers, Map<String, String> cookies, Ok ok) {
        new Thread(() -> get(url, params, headers, cookies, ok)).start();
    }

    /* - Post - */

    /**
     * 向目标发送POST连接
     * @param url 目标
     * @param ok 完成时的回调
     */
    public static void post(String url, Ok ok) {
        post(url, null, null, null, ok);
    }

    /**
     * 向目标发送POST连接
     * @param url 目标
     * @param params 参数
     * @param ok 完成时的回调
     */
    public static void post(String url, Map<String, String> params, Ok ok) {
        post(url, params, null, null, ok);
    }

    /**
     * 向目标发送POST连接
     * @param url 目标
     * @param params 参数
     * @param headers 请求头
     * @param ok 完成时的回调
     */
    public static void post(String url, Map<String, String> params, Map<String, String> headers, Ok ok) {
        post(url, params, headers, null, ok);
    }

    /**
     * 向目标发送POST连接
     * @param url 目标
     * @param params 参数
     * @param headers 请求头
     * @param cookies cookie
     * @param ok 完成时的回调
     */
    public static void post(String url, Map<String, String> params, Map<String, String> headers, Map<String, String> cookies, Ok ok) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            // 拼接请求参数
            StringBuffer buffer = new StringBuffer();
            if (params != null && params.size() != 0) {
                params.forEach((key, value) -> {
                    buffer.append(key);
                    buffer.append("=");
                    buffer.append(value);
                    buffer.append("&");
                });
            }
            // 移除url的最后一个字符("&")
            // 18-12-11： 移到if外面，若param为空时可移除"?"
            buffer.delete(buffer.length() - 1, buffer.length());
            String param = buffer.toString();

            // 使用cookie
            if (cookies != null && cookies.size() != 0) {
                String k = "Cookie";
                StringBuffer v = new StringBuffer();
                cookies.forEach((key, value) -> {
                    v.append(key);
                    v.append("=");
                    v.append(value);
                    v.append(";");
                });
                headers.put(k, v.toString());
            }

            // 设置请求头
            if (headers != null && headers.size() != 0) {
                headers.forEach(conn::setRequestProperty);
            }

            // 写入参数
            conn.getOutputStream().write(param.getBytes());

            int code = conn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == code) {
                InputStream is = conn.getInputStream();
                ok.ok(readInputStream(is));
            } else {
                ok.ok(conn.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新建一个线程执行POST请求任务。
     * @param url 请求链接
     * @param ok 回调
     */
    public static void doPost(String url, Ok ok) {
        doPost(url, null, null, null, ok);
    }

    /**
     * 新建一个线程执行POST请求任务。
     * @param url 请求链接
     * @param params 参数
     * @param ok 回调
     */
    public static void doPost(String url, Map<String, String> params, Ok ok) {
        doPost(url, params, null, null, ok);
    }

    /**
     * 新建一个线程执行POST请求任务。
     * @param url 请求链接
     * @param params 参数
     * @param headers 请求头
     * @param ok 回调
     */
    public static void doPost(String url, Map<String, String> params, Map<String, String> headers, Ok ok) {
        doPost(url, params, headers, null, ok);
    }

    /**
     * 新建一个线程执行POST请求任务。
     * @param url 请求链接
     * @param params 参数
     * @param headers 请求头
     * @param cookies 携带的cookie
     * @param ok 回调
     */
    public static void doPost(String url, Map<String, String> params, Map<String, String> headers, Map<String, String> cookies, Ok ok) {
        new Thread(() -> post(url, params, headers, cookies, ok)).start();
    }

    // TODO： 优化doPostFile方法的异常处理
    /**
     * 上传文件，文件大小超过php限制时将会发生错误
     * @param url 目标地址
     * @param params post的参数
     * @param files 要上传的文件
     * @param ok 回调
     */
    public static void doPostFile(String url, Map<String, String> params, LinkedList<File> files, Ok ok) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                // boundary就是request头和上传文件内容的分隔符
                String BOUNDARY = "------------FUCKYUN------------";
                try {
                    URL urll = new URL(url);
                    connection = (HttpURLConnection) urll.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(30000);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setRequestProperty("User-Agent", "FuckYun");
                    connection.setRequestProperty("Content-Type",
                            "multipart/form-data; boundary=" + BOUNDARY);
                    OutputStream out = new DataOutputStream(connection.getOutputStream());
                    // 发送参数
                    if (params != null) {
                        StringBuffer strBuf = new StringBuffer();
                        Iterator iter = params.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            String inputName = (String) entry.getKey();
                            String inputValue = (String) entry.getValue();
                            if (inputValue == null) {
                                continue;
                            }
                            strBuf.append("\r\n").append("--").append(BOUNDARY)
                                    .append("\r\n");
                            strBuf.append("Content-Disposition: form-data; name=\""
                                    + inputName + "\"\r\n\r\n");
                            strBuf.append(inputValue);
                        }
                        out.write(strBuf.toString().getBytes());
                    }

                    // 发送文件
                    if (files != null) {
                        for (File value : files) {
                            if (value == null) {
                                continue;
                            }
                            String filename = value.getName();

                            //没有传入文件类型，同时根据文件获取不到类型，默认采用application/octet-stream
//                    contentType = new MimetypesFileTypeMap().getContentType(file);
                            //contentType非空采用filename匹配默认的图片类型

                            String contentType = "";
                            if (!"".equals(contentType)) {
                                if (filename.endsWith(".png")) {
                                    contentType = "image/png";
                                } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".jpe")) {
                                    contentType = "image/jpeg";
                                } else if (filename.endsWith(".gif")) {
                                    contentType = "image/gif";
                                } else if (filename.endsWith(".ico")) {
                                    contentType = "image/image/x-icon";
                                }
                            }
                            if (contentType == null || "".equals(contentType)) {
                                contentType = "application/octet-stream";
                            }
                            StringBuffer strBuf = new StringBuffer();
                            strBuf.append("\r\n");
                            strBuf.append("--").append(BOUNDARY).append("\r\n");
                            strBuf.append("Content-Disposition: form-data; " +
                                    "name=\"file[]\"; filename=\"" + filename + "\"\r\n");
                            strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
                            out.write(strBuf.toString().getBytes());
                            DataInputStream in = new DataInputStream(
                                    new FileInputStream(value));
                            int bytes = 0;
                            byte[] bufferOut = new byte[1024];
                            while ((bytes = in.read(bufferOut)) != -1) {
//                                System.out.println(new String(bufferOut));
                                out.write(bufferOut, 0, bytes);
                            }
                            in.close();
                        }
                    }
                    byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
                    out.write(endData);
                    out.flush();
                    out.close();

                    // 读取返回数据
                    StringBuffer strBuf = new StringBuffer();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        strBuf.append(line).append("\n");
                    }
                    ok.ok(strBuf.toString());
                    reader.close();
                    reader = null;
                } catch (Exception e) {
                    ok.ok(e.getMessage());
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                        connection = null;
                    }
                }
            }
        }).start();
    }

    /* - Parser - */

    /**
     * 解析cookie字符串，通常这个字符串是从浏览器或抓包工具中复制出来的，一般格式如下：
     * Cookie: key1=value1; key2=value2
     * 该格式能被正确解析并返回解析后的Map对象。没有“Cookie: ”开头也是能正常解析的，解析器会忽略它。
     * @param cookie cookie字符串
     * @return 解析结果
     */
    public static HashMap<String, String> parseCookies(String cookie) {
        return parseCookies(cookie, "; ");
    }

    /**
     * 解析cookie字符串，这个字符串通常是从浏览器或抓包工具中复制出来的，一般格式如下：<br>
     * Cookie: key1=value1; key2=value2
     * <br>该格式能被正确解析并返回解析后的Map对象。没有“Cookie: ”开头也是能正常解析的，解析器会忽略它。
     * @param cookie cookie字符串
     * @param regex 分隔每一条key-value的表达式，即上面格式中的“; ”
     * @return 解析结果
     */
    public static HashMap<String, String> parseCookies(String cookie, String regex) {
        HashMap<String, String> map = new HashMap<>();
        // 忽略 "Cookie: "
        if (cookie.toLowerCase().startsWith("cookie: ")) cookie = cookie.substring("cookie: ".length());
        // 除非regex有内容，否则按照默认格式处理
        regex = "".equals(regex) || regex == null ? "; " : regex;
        String[] cookies = cookie.split(regex);
        for (String str : cookies) {
            String[] kv = str.split("=");
            map.put(kv[0], kv[1]);
        }
        return map;
    }

    /**
     * 解析参数字符串，这个字符串与GET请求的参数格式一样。格式：<br>
     * key1=value1&key2=value2
     * @param param 参数字符串
     * @return 解析结果
     */
    public static HashMap<String, String> parseParams(String param) {
        HashMap<String, String> map = new HashMap<>();
        String[] params = param.split("&");
        for (String p : params) {
            String[] kv = p.split("=");
            map.put(kv[0], kv[1]);
        }
        return map;
    }

    /**
     * 解析请求头，请求头字符串格式如下：<br>
     * key1: value1$key2: value2
     * @param header 请求头字符串
     * @return 解析结果
     */
    public static HashMap<String, String> parseHeaders(String header) {
        HashMap<String, String> map = new HashMap<>();
        String[] headers = header.split("$");

        for (String h : headers) {
            System.out.println(h);
            String[] kv = h.split(": ");
            map.put(kv[0], kv[1]);
        }
        return map;
    }

    /* - Other - */

    /**
     * 从输入流中读取信息返回
     * @param is 输入流
     * @return 流中的信息
     */
    private static String readInputStream(InputStream is) {
        StringBuffer buf = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                buf.append(line);
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 回调接口
     */
    public interface Ok {
        /**
         * 当获取内容完成时被调用
         * @param msg 内容
         */
        void ok(String msg);
    }
}