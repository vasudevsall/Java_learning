package crawler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WebCrawler extends JFrame {
    public WebCrawler() {
        super("Sample Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
        setVisible(true);
        setLayout(null);

        JTextField urlArea = new JTextField();
        urlArea.setName("UrlTextField");
        urlArea.setBounds(20, 20, 450, 30);
        add(urlArea);

        JButton button = new JButton("Get Text!");
        button.setName("RunButton");
        button.setBounds(480, 20, 100, 30);
        add(button);

        JTextArea htmlArea = new JTextArea();
        htmlArea.setName("HtmlTextArea");
        htmlArea.setBounds(20, 70, 560, 700);
//        add(htmlArea);

        JScrollPane scroll = new JScrollPane(htmlArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBounds(20, 70, 560, 700);
        add(scroll);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlArea.getText();
                String siteData = getSiteData(url);
                htmlArea.setText(siteData);
            }
        });
    }

    public String getSiteData(String url){
        try {
            final InputStream inputStream = new URL(url).openStream();
            final BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            final StringBuilder stringBuilder = new StringBuilder();
            final String LINE_SEPARATOR = System.getProperty("line.separator");
            String nextLine;
            while((nextLine = reader.readLine()) != null){
                stringBuilder.append(nextLine);
                stringBuilder.append(LINE_SEPARATOR);
            }

            return stringBuilder.toString();
        } catch (Exception E){
            return "Check URL";
        }
    }
}