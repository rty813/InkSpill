package com.example.zhang.inkspill;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by zhang on 2016/12/22.
 */

public class UpdateinfoService {
    public Updateinfo getUpdateinfo(){
        StringBuilder builder = new StringBuilder();
        try {
            String serviceUrl = "http://zhangjinyang.com.cn/update";
            URL url = new URL(serviceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }
            reader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (builder.toString().equals("")){
            Updateinfo updateinfo = new Updateinfo();
            updateinfo.setVersion("Unknown Version!");
            return updateinfo;
        }
        String info = builder.toString();
        Updateinfo updateinfo = new Updateinfo();
        updateinfo.setVersion(info.split("&")[1]);
        updateinfo.setDescription(info.split("&")[2]);
        updateinfo.setDownloadUrl(info.split("&")[3] + "1");
        return updateinfo;
    }
}
