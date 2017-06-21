package com.linyuting.luistest;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by A on 17-4-2017.
 */

public class LUISHelper {

    private String endPint = "https://westus.api.cognitive.microsoft.com/luis/v2.0/apps/14911e8a-864f-4f1a-89ce-7cf817dbdb79?subscription-key=a97a1502e9c64b44a297a296dfc52acd&verbose=true&timezoneOffset=0.0&q=";

    public LUISModel Query(String keyword){
        LUISModel luisModel = null;
        try
        {
            String jsonData = Get(keyword);
            if(jsonData!=null){
                luisModel = new LUISModel(new JSONObject(jsonData));
            }
        }
        catch (Exception e){
            Log.e("err", e.getMessage());
        }
        return luisModel;

    }

    private String Get(String keyword){

        try {
            keyword = URLEncoder.encode(keyword, "utf-8");
            URL uri = new URL(endPint + keyword);
            HttpURLConnection connection = (HttpURLConnection)uri.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if(responseCode == 200){
                InputStream in = connection.getInputStream();
                StringBuffer  out = new StringBuffer();
                byte[] b = new byte[4096];
                for   (int n;(n=in.read(b))!=-1;)   {
                    out.append(new String(b,0,n));
                }
                return out.toString();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

}
