package com.mobile.hotel;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class TopGatewayUtil {
    private static final String SIGN_METHOD_MD5 = "md5";
    private static final String SIGN_METHOD_HMAC = "hmac";
    private static final String CHARSET_UTF8 = "utf-8";
    private static final String CONTENT_ENCODING_GZIP = "gzip";
    private String ak = "34731404";
    private String sk = "153bc5ca98cb00043e9724b5977dbf2b";
    private String host = "http://gw.api.taobao.com/top/router/rest";
    private String method = "yunos.ebanma.right.platform.call";

    public String searchList(String sortType,String keyWords) {
        Map<String, Object> map = new HashMap<>();
        map.put("api","searchHotel");
        Map<String, Object> param = new HashMap<>();
        param.put("keyWords",keyWords);
        param.put("cityName","武汉");
        param.put("cityCode","420100");
//        param.put("price","300-400");
//        param.put("grade","5");
//        param.put("longitude","114.407040");
//        param.put("latitude","30.464379");
        param.put("sortType",sortType);
        param.put("offset","0");
        param.put("pageNo","1");
        param.put("pageSize","10");
        map.put("params",param);

        return callApi(map,method);
    }

    public String hotelDetail(String id){
        Map<String, Object> map = new HashMap<>();
        map.put("api","queryHotelDetail");
        Map<String, Object> param = new HashMap<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        param.put("checkIn", df.format(today));
        param.put("adultNum","1");
        param.put("shid",id);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_YEAR, 1); // 加1天
        Date tomorrow = calendar.getTime();
        param.put("checkOut", df.format(tomorrow));
        map.put("params",param);

        return callApi(map,method);
    }

    public String callApi(Map<String, Object> map,String method) {
        try{
            Map<String, String> params = new HashMap<String, String>();
            // 公共参数
            params.put("method", method);
            params.put("app_key", ak);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            params.put("timestamp", df.format(new Date()));
            params.put("format", "json");
            params.put("v", "2.0");
            params.put("sign_method", "hmac");
            // 业务参数
            params.put("param", JSON.toJSONString(map));
            Log.e("TAG", "callApi: "+JSON.toJSONString(map) );
            // 签名参数
            params.put("sign", signTopRequest(params, sk, SIGN_METHOD_HMAC));
//        Log.e("TAG", "callApi: "+signTopRequest(params, sk, SIGN_METHOD_HMAC));
            // 请用API
            return callApi(new URL(host), params);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
    /**
     * 对TOP请求进行签名。
     */
    private static String signTopRequest(Map<String, String> params, String secret, String signMethod) throws IOException {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        if (SIGN_METHOD_MD5.equals(signMethod)) {
            query.append(secret);
        }
        for (String key : keys) {
            String value = params.get(key);
            Log.e("TAG", "signTopRequest: "+key+"value"+value);
            if (isNotEmpty(key) && isNotEmpty(value)) {
                query.append(key).append(value);
            }
        }

        // 第三步：使用MD5/HMAC加密
        byte[] bytes;
        if (SIGN_METHOD_HMAC.equals(signMethod)) {
            bytes = encryptHMAC(query.toString(), secret);
        } else {
            query.append(secret);
            bytes = encryptMD5(query.toString());
        }
        // 第四步：把二进制转化为大写的十六进制
        return byte2hex(bytes);
    }
    /**
     * 对字节流进行HMAC_MD5摘要。
     */
    private static byte[] encryptHMAC(String data, String secret) throws IOException {
        byte[] bytes = null;
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(CHARSET_UTF8), "HmacMD5");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes(CHARSET_UTF8));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse.toString());
        }
        return bytes;
    }
    /**
     * 对字符串采用UTF-8编码后，用MD5进行摘要。
     */
    private static byte[] encryptMD5(String data) throws IOException {
        return encryptMD5(data.getBytes(CHARSET_UTF8));
    }
    /**
     * 对字节流进行MD5摘要。
     */
    private static byte[] encryptMD5(byte[] data) throws IOException {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytes = md.digest(data);
        } catch (GeneralSecurityException gse) {

            throw new IOException(gse.toString());
        }
        return bytes;
    }
    /**
     * 把字节流转换为十六进制表示方式。
     */
    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }
    private static String callApi(URL url, Map<String, String> params) throws IOException {
        String query = buildQuery(params, CHARSET_UTF8);
        byte[] content = {};
        if (query != null) {
            content = query.getBytes(CHARSET_UTF8);
        }
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Host", url.getHost());
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "android-client-side");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + CHARSET_UTF8);
            out = conn.getOutputStream();
            out.write(content);
            rsp = getResponseAsString(conn);
        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        Log.e("TAG", "callApi: "+rsp );
        return rsp;
    }
    private static String buildQuery(Map<String, String> params, String charset) throws IOException {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuilder query = new StringBuilder();
        Set<Map.Entry<String, String>> entries = params.entrySet();
        boolean hasParam = false;
        for (Map.Entry<String, String> entry : entries) {
            String name = entry.getKey();
            String value = entry.getValue();
            // 忽略参数名或参数值为空的参数
            if (isNotEmpty(name) && isNotEmpty(value)) {
                if (hasParam) {
                    query.append("&");
                } else {
                    hasParam = true;
                }
                query.append(name).append("=").append(URLEncoder.encode(value, charset));
            }
        }
        return query.toString();
    }
    private static String getResponseAsString(HttpURLConnection conn) throws IOException {
        String charset = getResponseCharset(conn.getContentType());
        if (conn.getResponseCode() < 400) {
            String contentEncoding = conn.getContentEncoding();
            if (CONTENT_ENCODING_GZIP.equalsIgnoreCase(contentEncoding))
            {
                return getStreamAsString(new GZIPInputStream(conn.getInputStream()), charset);
            } else {
                return getStreamAsString(conn.getInputStream(), charset);
            }
        } else {// Client Error 4xx and Server Error 5xx
            throw new IOException(conn.getResponseCode() + " " + conn.getResponseMessage());
        }
    }
    private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            Reader reader = new InputStreamReader(stream, charset);
            StringBuilder response = new StringBuilder();
            final char[] buff = new char[1024];
            int read = 0;
            while ((read = reader.read(buff)) > 0) {
                response.append(buff, 0, read);
            }
            return response.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
    private static String getResponseCharset(String ctype) {
        String charset = CHARSET_UTF8;
        if (isNotEmpty(ctype)) {
            String[] params = ctype.split(";");
            for (String param : params) {
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2) {
                        if (isNotEmpty(pair[1])) {
                            charset = pair[1].trim();
                        }
                    }
                    break;
                }
            }
        }
        return charset;
    }
    private static boolean isNotEmpty(String value) {
        int strLen;
        if (value == null || (strLen = value.length()) == 0) {
            return false;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(value.charAt(i)) == false)) {
                return true;
            }
        }
        return false;
    }
}
