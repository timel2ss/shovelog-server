package me.shovelog.exception.notfound;

import me.shovelog.exception.ShovelogException;

public class CategoryNotFoundException extends ShovelogException {
    public static final String MESSAGE = "존재하지 않는 카테고리입니다.";
    public static final int STATUS_CODE = 404;

    public CategoryNotFoundException() {
        super(MESSAGE);
    }

    public CategoryNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return STATUS_CODE;
    }
}
