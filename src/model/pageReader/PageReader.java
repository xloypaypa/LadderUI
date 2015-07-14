package model.pageReader;

import java.io.*;

/**
 * Created by xlo on 15-7-14.
 * it's page reader
 */
public class PageReader {
    public static String readPage(String page) {
        try {
            InputStream inputStream = PageReader.class.getResourceAsStream(page);
            String ans = "";
            byte[] bytes = new byte[1024];
            int len;

            while (true) {
                len = inputStream.read(bytes);
                if (len < 0) break;
                ans += new String(bytes, 0 ,len);
            }
            return ans;
        } catch (IOException e) {
            return null;
        }
    }
}
