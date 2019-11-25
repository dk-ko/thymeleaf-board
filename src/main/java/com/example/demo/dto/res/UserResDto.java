package com.example.demo.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UserResDto {
    private final Long idx;
    private final String name;
}
