package com.example.demo.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardResDto {
    private final Long boardIdx;
    private final String name;
}
