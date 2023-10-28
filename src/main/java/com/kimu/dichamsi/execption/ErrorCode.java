package com.kimu.dichamsi.execption;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USEREMAIL_DUPLICATED(""),
    USEREMAIL_NOT_FOUND(""),
    INVALID_PASSWORD("");
    private String message;

}
