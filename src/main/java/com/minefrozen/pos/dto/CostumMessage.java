package com.minefrozen.pos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CostumMessage {

    public CostumMessage(HttpStatus status, String message) {
        this.message = message;
        this.status = status.value();
        this.title = status.getReasonPhrase();
    }

    private String timestamp = LocalDateTime.now().toString();
    private String message;
    private Integer status;
    private String title;
}
