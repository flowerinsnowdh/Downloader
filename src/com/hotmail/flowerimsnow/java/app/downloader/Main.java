package com.hotmail.flowerimsnow.java.app.downloader;

import com.hotmail.flowerimsnow.java.app.downloader.async.DownloadThread;
import com.hotmail.flowerimsnow.java.app.downloader.config.Config;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main extends JFrame {
    public static Main instance;
    private JMenuBar menuBar;

    private JMenu file;
    private JMenuItem exit;

    private JMenu options;
    private JMenu saveDir;
    private JMenuItem openSaveDir;
    private JMenuItem inputDir;
    private JMenuItem browse;

    private JTextField inputUrl;
    private JButton start;

    private Main() {
    }
    private void initComp() {
        menuBar = new JMenuBar();

        // 文件菜单
        file = new JMenu("文件(F)");
        file.setMnemonic(KeyEvent.VK_C);
        menuBar.add(file);
        // 文件菜单组件
        exit = new JMenuItem("退出(C)");
        exit.setMnemonic(KeyEvent.VK_X);
        file.add(exit);

        // 选项组件
        options = new JMenu("选项(O)");
        options.setMnemonic(KeyEvent.VK_O);
        saveDir = new JMenu("保存目录(S)");
        saveDir.setMnemonic(KeyEvent.VK_S);
        options.add(saveDir);
        openSaveDir = new JMenuItem("打开(O)");
        openSaveDir.setMnemonic(KeyEvent.VK_O);
        saveDir.add(openSaveDir);
        inputDir = new JMenuItem("输入(I)");
        inputDir.setMnemonic(KeyEvent.VK_I);
        saveDir.add(inputDir);
        browse = new JMenuItem("浏览(B)");
        browse.setMnemonic(KeyEvent.VK_B);
        saveDir.add(browse);
        menuBar.add(options);

        inputUrl = new JTextField();
        start = new JButton("开始");
    }
    private void addListener() {
        exit.addActionListener(e -> System.exit(0));
        openSaveDir.addActionListener(e -> {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(new File(Config.instance.saveDir));
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null,"该路径不存在 无法打开","错误",JOptionPane.ERROR_MESSAGE);
            }
        });
        inputDir.addActionListener(e -> {
            String s = JOptionPane.showInputDialog(this,"输入保存路径");
            if (s != null) Config.instance.saveDir = s;
            Config.instance.saveToFile();
        });
        browse.addActionListener(e -> {
            JFileChooser f = new JFileChooser();
            f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (f.showOpenDialog(Main.this) == JFileChooser.APPROVE_OPTION) {
                File file = f.getSelectedFile();
                if (file != null) {
                    Config.instance.saveDir = file.getParent();
                    Config.instance.saveToFile();
                }
            }
        });
        start.addActionListener(e -> {
            HttpsURLConnection conn = null;
            try {
                URL url = new URL(this.inputUrl.getText());
                conn = (HttpsURLConnection) url.openConnection();
            } catch (Exception e1) {

            } finally {
                DownloadThread.disconnect(conn);
            }
        });
    }

    public static void main(String[] args) {
        Config.saveDefaultConfig();
        Main main = new Main();
        instance = main;
        main.setSize(500,300);
        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        main.setLocationRelativeTo(null);
        main.initComp();
        main.addListener();
        main.setJMenuBar(main.menuBar);
        main.setVisible(true);
//        Config.saveDefaultConfig();
//        Config.read();
//        System.out.println(Config.instance.saveDir);
    }
}
