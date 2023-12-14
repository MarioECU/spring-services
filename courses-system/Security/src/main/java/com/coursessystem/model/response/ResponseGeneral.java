package com.coursessystem.model.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseGeneral {
    private int code;
    private HttpStatus status;
    private String message;

    public ResponseGeneral(HttpStatus status, String message) {
        this.code = status.value();
        this.status = status;
        this.message = message;
    }
}
