package net.io;

import tool.ioAble.IOAble;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by xlo on 15-7-10.
 * it's build io by a string
 */
public class StringIOBuilder implements IOAble {
    protected String message;
    protected InputStream inputStream;

    public void setMessage(String message) {
        this.message = message;
    }

    public void buildIO() {
        this.inputStream = new ByteArrayInputStream(this.message.getBytes());
    }

    @Override
    public InputStream getInputStream() {
        return this.inputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return null;
    }
}
