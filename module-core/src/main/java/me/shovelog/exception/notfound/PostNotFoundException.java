package me.shovelog.exception.notfound;

import me.shovelog.exception.ShovelogException;

public class PostNotFoundException extends ShovelogException {
    public static final String MESSAGE = "존재하지 않거나 접근 권한이 없는 게시글입니다.";
    public static final int STATUS_CODE = 404;

    public PostNotFoundException() {
        super(MESSAGE);
    }

    public PostNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return STATUS_CODE;
    }
}
