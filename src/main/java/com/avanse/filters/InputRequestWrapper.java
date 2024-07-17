package com.avanse.filters;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

public class InputRequestWrapper extends HttpServletRequestWrapper {


private byte[] rawData;
private HttpServletRequest request;
private ResettableServletInputStream servletStream;

public InputRequestWrapper(HttpServletRequest request) {
    super(request);
    this.request = request;
    this.servletStream = new ResettableServletInputStream();
}


public void resetInputStream(byte[] newRawData) {
    servletStream.stream = new ByteArrayInputStream(newRawData);
}

@SuppressWarnings("deprecation")
@Override
public ServletInputStream getInputStream() throws IOException {
    if (rawData == null) {
        rawData = IOUtils.toByteArray(this.request.getReader());
        servletStream.stream = new ByteArrayInputStream(rawData);
    }
    return servletStream;
}

@SuppressWarnings("deprecation")
@Override
public BufferedReader getReader() throws IOException {
    if (rawData == null) {
        rawData = IOUtils.toByteArray(this.request.getReader());
        servletStream.stream = new ByteArrayInputStream(rawData);
    }
    return new BufferedReader(new InputStreamReader(servletStream));
}

private class ResettableServletInputStream extends ServletInputStream {

    private InputStream stream;

    @Override
    public int read() throws IOException {
        return stream.read();
     }

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setReadListener(ReadListener listener) {
		// TODO Auto-generated method stub
		
	}
   }
 }
