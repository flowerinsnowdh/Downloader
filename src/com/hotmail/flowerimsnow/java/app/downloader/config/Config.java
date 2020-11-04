package com.hotmail.flowerimsnow.java.app.downloader.config;

import javax.swing.*;
import java.io.*;

public class Config implements Serializable {
    public static Config instance;
    public String saveDir;

    public Config(String saveDir) {
        instance = this;
        this.saveDir = saveDir;
    }

    public Config() {
        this("C:\\Users\\" + System.getProperty("user.name") + "\\Downloads");
    }

    public static void saveDefaultConfig() {
        File dir = new File("downloader");
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, "config.dat");
        try {
            if (!file.exists()) {
                file.createNewFile();
                Config config = new Config();
                config.saveToFile();
            }
        } catch (IOException e) {
            StringBuilder stacks = new StringBuilder("创建配置文件失败");
            for (StackTraceElement stack : e.getStackTrace()) {
                stacks.append("\n").append(stack.toString());
            }
            JOptionPane.showMessageDialog(null, stacks.toString(), "异常", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    public void saveToFile() {
        File dir = new File("downloader");
        File file = new File(dir, "config.dat");
        OutputStream out = null;
        ObjectOutputStream oops = null;
        try {
            out = new FileOutputStream(file);
            oops = new ObjectOutputStream(out);
            oops.writeObject(this);
            oops.flush();
        } catch (IOException e) {
            StringBuilder stacks = new StringBuilder("保存文件失败");
            for (StackTraceElement stack : e.getStackTrace()) {
                stacks.append("\n").append(stack.toString());
            }
            JOptionPane.showMessageDialog(null, stacks.toString(), "异常", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } finally {
            close(oops,out);
        }
    }
    public static Config read() {
        File dir = new File("downloader");
        File file = new File(dir, "config.dat");
        InputStream in = null;
        ObjectInputStream oips = null;
        try {
            in = new FileInputStream(file);
            oips = new ObjectInputStream(in);
            Config config = (Config) oips.readObject();
            instance = config;
            return config;
        } catch (Exception e) {
            StringBuilder stacks = new StringBuilder("读取文件失败");
            for (StackTraceElement stack : e.getStackTrace()) {
                stacks.append("\n").append(stack.toString());
            }
            JOptionPane.showMessageDialog(null, stacks.toString(), "异常", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
            return null;
        } finally {
            close(oips,in);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Config) {
            Config config = (Config) o;
            if (config.saveDir == null) return saveDir == null;
            else return config.saveDir.equals(saveDir);
        } else return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        return 31 * result + saveDir.hashCode();
    }

    @Override
    public String toString() {
        return "Config{" +
                "saveDir='" + saveDir + '\'' +
                '}';
    }

    public static void close(AutoCloseable... c) {
        for (AutoCloseable autoCloseable : c) {
            try {
                autoCloseable.close();
            } catch (Exception ignored) {
            }
        }
    }
}
