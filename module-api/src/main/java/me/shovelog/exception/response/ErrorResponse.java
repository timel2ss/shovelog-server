package me.shovelog.exception.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponse(
        String code,
        String message,
        Map<String, String> validation
) {
}
