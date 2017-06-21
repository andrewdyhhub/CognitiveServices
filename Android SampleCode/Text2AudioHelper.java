package com.linyuting.luistest;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by A on 18-4-2017.
 */

public class Text2AudioHelper {

    private String key = "26aa4f31d4b7406da13921e98b6d39ad";
    private String url = "https://api.cognitive.microsoft.com/sts/v1.0/issueToken";
    private String recognizeURL = "https://speech.platform.bing.com/synthesize";

    public String getAccessToken() {
        try {
            URL targetUrl = new URL(url);
            HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.addRequestProperty("Ocp-Apim-Subscription-Key", this.key);
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == 200) {
                //请求成功 获得返回的流
                InputStream in = httpConnection.getInputStream();
                StringBuffer out = new StringBuffer();
                byte[] b = new byte[4096];
                for (int n; (n = in.read(b)) != -1; ) {
                    out.append(new String(b, 0, n));
                }
                httpConnection.disconnect();
                return out.toString();
            }
            httpConnection.disconnect();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public byte[] convertToAodio() {

        String host = "speech.platform.bing.com";
        String contentType = "application/ssml+xml";

        try {
            URL targetUrl = new URL(recognizeURL);
            HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", contentType);
            httpConnection.addRequestProperty("Host", host);
            httpConnection.addRequestProperty("X-Microsoft-OutputFormat","audio-16khz-128kbitrate-mono-mp3");
            httpConnection.addRequestProperty("X-Search-AppId", "565D69FFE9284B7E87DA9A750B96D9E3");
            httpConnection.addRequestProperty("X-Search-ClientID", java.util.UUID.randomUUID().toString().replace("-",""));
            httpConnection.addRequestProperty("User-Agent","LUISTest");

            OutputStream outputStream = httpConnection.getOutputStream();
            outputStream.write(("VoiceType=Male&VoiceName=Microsoft Server Speech Text to Speech Voice (zh-CN, Kangkang, Apollo)&Locale=zh-CN&OutputFormat=Audio16khz128kbitrateMonoMp3&AuthorizationToken="+getAccessToken()+"&Text=你是谁").getBytes());
            outputStream.flush();


            int responseCode = httpConnection.getResponseCode();
            if (responseCode == 200) {
                InputStream in = httpConnection.getInputStream();
                byte[] results = new byte[in.available()];
                in.read(results);
                httpConnection.disconnect();
                return results;
            }
            httpConnection.disconnect();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

}
