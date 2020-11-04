package com.hotmail.flowerimsnow.java.app.downloader.async;

import com.hotmail.flowerimsnow.java.app.downloader.config.Config;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadThread extends Thread {
    public final String url;
    public final int index;
    public final long start,end;
    public boolean stop;
    public DownloadThread(String url,long start,long end,int index) {
        this.url = url.replace("/","\\");
        this.start = start;
        this.end = end;
        this.index = index;
        this.stop = false;
    }
    @Override
    public void run() {
        HttpsURLConnection conn = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            File dir = new File(Config.instance.saveDir);
            if (!dir.exists()) dir.mkdirs();
            String name = this.url.split("\\\\")[url.split("\\\\").length - 1];
            File file = new File(dir,name + ".tmp" + index);
            if (!file.exists()) file.createNewFile();
            URL url = new URL(this.url);
            conn = (HttpsURLConnection) url.openConnection();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"线程" + index + "发生异常 程序正在退出");
            System.exit(-1);
        } finally {
            close(out,in);
            disconnect(conn);
        }
    }
    public static void close(AutoCloseable... cs) {
        for (AutoCloseable c : cs) {
            try {
                c.close();
            } catch (Exception ignored) {
            }
        }
    }
    public static void disconnect(HttpURLConnection... cs) {
        for (HttpURLConnection c : cs) {
            try {
                c.disconnect();
            } catch (Exception ignored) {
            }
        }
    }
}
