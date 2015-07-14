package net.io;

import server.io.NormalFileIO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by xlo on 15-7-14.
 * it's the file io not append
 */
public class WriteFileIO extends NormalFileIO {
    @Override
    public boolean buildIO() {
        try {
            this.inputStream = new FileInputStream(this.file);
            this.outputStream = new FileOutputStream(this.file);
            return true;
        } catch (FileNotFoundException var2) {
            return false;
        }
    }
}
