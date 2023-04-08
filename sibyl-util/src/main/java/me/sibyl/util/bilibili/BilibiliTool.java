package me.sibyl.util.bilibili;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.deepoove.poi.util.ByteUtils;
import me.sibyl.util.file.FileUtil;
import me.sibyl.util.stream.StreamUtil;
import org.apache.commons.io.FileUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author dyingleaf3213
 * @Classname BilibiliTool
 * @Description 20230409
 * @Create 2023/04/08 21:28
 */

public class BilibiliTool {

    public static void main(String[] args) {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();) {
            String bvid = "BV1GL411o7Fb";
            System.err.println("bvid => " + bvid);
            HttpGet get = new HttpGet("https://api.bilibili.com/x/web-interface/view?bvid=" + bvid);
            CloseableHttpResponse response = httpClient.execute(get);
            InputStream content = response.getEntity().getContent();
            String string = StreamUtil.copyToString(content, Charset.forName("utf-8"));
            System.err.println("bvstring => " + string);
            StreamUtil.close(content);
            JSONObject bvJson = JSONObject.parseObject(string);
            JSONObject data = bvJson.getJSONObject("data");
            String title = data.getString("title");
            String cid = data.getString("cid");//
            System.err.println("cid => " + cid);

            HttpGet vGet = new HttpGet("https://api.bilibili.com/x/player/playurl?cid=" + cid + "&bvid=" + bvid + "&qn=80");
//            vGet.setHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36");
//            vGet.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.ALL.getType());
//            vGet.setHeader("Referer", "https://www.bilibili.com"); // 设置协议
//            vGet.setHeader("Sec-Fetch-Mode", "no-cors");
//            vGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
            vGet.setHeader("cookie", "buvid3=FB0D9A25-F265-4664-8B7F-98AC45C5D743167642infoc; LIVE_BUVID=AUTO9216307616704348; CURRENT_BLACKGAP=0; buvid_fp_plain=undefined; CURRENT_FNVAL=4048; blackside_state=1; hit-dyn-v2=1; i-wanna-go-feeds=-1; DedeUserID=11885873; DedeUserID__ckMd5=1c1d7cd5a933ec09; b_ut=5; _uuid=7D6A710DA-F152-46EE-1AC3-E4D43CDF477A60250infoc; i-wanna-go-back=2; b_nut=100; rpdid=|(JRuY~mkkYl0J'uYY)~RR)Rk; buvid4=E1055CD8-564A-51CF-38E9-24171148CF9382090-022012118-+QGnwQAgbpqebIu3OwJxFA==; hit-new-style-dyn=1; go-back-dyn=1; nostalgia_conf=2; fingerprint=f3b71061c297b34d09f4f35e693f70e9; CURRENT_PID=d74fee80-cde8-11ed-a8d9-4b5bbdd32469; CURRENT_QUALITY=64; buvid_fp=f3b71061c297b34d09f4f35e693f70e9; SESSDATA=625f979f,1696506703,59c6d*42; bili_jct=805e8638f3bc44d815b31e24454fda10; bp_video_offset_11885873=782170242697658611; bsource=search_baidu; innersign=1; sid=70ipqe3r; b_lsid=22229E92_18761990D75; PVID=1; _dfcaptcha=900d5c330f1eda2849583cfcaa180d45");
            CloseableHttpResponse urlResponse = httpClient.execute(vGet);

            InputStream urlContent = urlResponse.getEntity().getContent();
            String urlStr = StreamUtil.copyToString(urlContent, Charset.forName("utf-8"));
            System.err.println("urlStr => " + urlStr);
            StreamUtil.close(urlContent);

            JSONObject urlJson = JSONObject.parseObject(urlStr);
            JSONObject urlData = urlJson.getJSONObject("data");
            JSONArray durls = urlData.getJSONArray("durl");
            JSONObject durl = durls.getJSONObject(0);
            String url = durl.getJSONArray("backup_url").getString(0);
            System.err.println("url => " + url);

            downloadNew(url, "D:\\" + title + ".mp4");

            System.err.println();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //这个类使用了commons-io
    public static void downloadNew(String urlPath, String descFileName) {
        try {
            long begin = System.currentTimeMillis();
            URL url = new URL(urlPath);
            URLConnection urlConnection = url.openConnection();

            urlConnection.setRequestProperty("Referer", "https://www.bilibili.com"); // 设置协议
//            urlConnection.setRequestProperty("Sec-Fetch-Mode", "no-cors");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
//            urlConnection.setConnectTimeout(10 * 1000);

            System.err.println("共:" + (urlConnection.getContentLength() / 1024) + "Kb");
            System.err.println("开始下载...");
            InputStream input = urlConnection.getInputStream();

            byte[] bytes = ByteUtils.toByteArray(input);
            //FileUtils.copyInputStreamToFile(input, new File(descFileName));

            FileOutputStream imageOutput = new FileOutputStream(descFileName);
            imageOutput.write(bytes, 0, bytes.length);//将byte写入硬盘
            imageOutput.close();

            long end = System.currentTimeMillis();
            System.err.println("耗时：" + (end - begin) / 1000 + "秒");
            System.err.println("下载完成！");
        } catch (Exception e) {
            System.err.println("异常中止: " + e);
        }
    }

}
