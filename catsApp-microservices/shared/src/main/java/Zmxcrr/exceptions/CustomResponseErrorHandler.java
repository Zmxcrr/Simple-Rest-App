package Zmxcrr.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CustomResponseErrorHandler extends DefaultResponseErrorHandler {
    private final Logger logger = LoggerFactory.getLogger(CustomResponseErrorHandler.class);
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        byte[] body = getResponseBody(response);
        Charset charset = getCharset(response);
        String message = new String(body, charset == null ? StandardCharsets.UTF_8 : charset);
        logger.error(response.getStatusText() + ":" + message);
    }
}