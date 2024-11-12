package edu.java.kudagoapi.utils;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import java.io.*;

@Slf4j
public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

    private byte[] body;

    public CachedBodyHttpServletRequest(HttpServletRequest request) {
        super(request);
        try {
            InputStream requestInputStream = request.getInputStream();
            this.body = StreamUtils.copyToByteArray(requestInputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CachedBodyServletInputStream(body);
    }

    @Override
    public BufferedReader getReader() {
        ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new BufferedReader(new InputStreamReader(bais));
    }

    static class CachedBodyServletInputStream extends ServletInputStream {

        private final InputStream cachedBodyInputStream;

        CachedBodyServletInputStream(byte[] body) {
            this.cachedBodyInputStream = new ByteArrayInputStream(body);
        }

        @Override
        @SneakyThrows
        public boolean isFinished() {
            return cachedBodyInputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int read() throws IOException {
            return cachedBodyInputStream.read();
        }
    }
}
