package dev.gutemberg.comment.api.config.web;

import dev.gutemberg.comment.api.client.BadGatewayException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.channels.ClosedChannelException;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({
            ConnectException.class,
            ClosedChannelException.class,
            SocketTimeoutException.class
    })
    public ProblemDetail gatewayTimeout(final IOException exception) {
        final var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.GATEWAY_TIMEOUT, exception.getMessage());
        problemDetail.setTitle("Gateway timeout");
        problemDetail.setType(URI.create("/errors/gateway-timeout"));
        return problemDetail;
    }

    @ExceptionHandler(BadGatewayException.class)
    public ProblemDetail badGatewayException() {
        final var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);
        problemDetail.setTitle("Bad gateway");
        problemDetail.setType(URI.create("/errors/bad-gateway"));
        return problemDetail;
    }
}
