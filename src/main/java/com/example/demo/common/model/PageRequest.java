package com.example.demo.common.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Sort;

@Getter
@ToString
public final class PageRequest {
    private final int DEFAULT_SIZE = 15;

    private int page = 1;
    private int size = DEFAULT_SIZE;
    private Sort.Direction direction = Sort.Direction.DESC;

    public void setPage(int page) {
        this.page = page < 1 ? 1 : page;
    }

    public void setSize(int size) {
        int MAX_SIZE = 50; // TODO Max size를 지정할 필요가 있을까?
        int DEFAULT_SIZE = 15;
        this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }

    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(page - 1, size, direction, "createdDate");
    }
}
