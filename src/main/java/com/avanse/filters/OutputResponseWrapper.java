package com.avanse.filters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class OutputResponseWrapper extends HttpServletResponseWrapper {

    //private final Logger LOG = LoggerFactory.getLogger(getClass());

private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
private PrintWriter writer = new PrintWriter(outputStream);

public OutputResponseWrapper(HttpServletResponse response)
        throws IOException {
    super(response);
}

@Override
public ServletOutputStream getOutputStream() throws IOException {
    //LOG.info("getOutputStream");

    return new ServletOutputStream() {
        @Override
        public void write(int b) throws IOException {
           // LOG.info("write int");

            outputStream.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            //LOG.info("write byte[]");

            outputStream.write(b);
        }

		@Override
		public boolean isReady() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setWriteListener(WriteListener listener) {
			// TODO Auto-generated method stub
			
		}
    };
}

@Override
public PrintWriter getWriter() throws IOException {
    //LOG.info("getWriter");
    return writer;
}

@Override
public void flushBuffer() throws IOException {
    if (writer != null) {
        writer.flush();
    } else if (outputStream != null) {
        outputStream.flush();
    }
}

public String getResponseData() {
    return outputStream.toString();
}

}
