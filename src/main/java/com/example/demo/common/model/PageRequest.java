package com.example.demo.common.model;

import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public final class PageRequest {
    private static final int DEFAULT_SIZE = 15;
    private static final String PROPERTIES = "createdDate";

    private int page;
    private int size = DEFAULT_SIZE;
    private Sort.Direction direction = Sort.Direction.DESC;

    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
    }

    public void setSize(int size) {
        int MAX_SIZE = 50; // TODO Max size를 지정할 필요가 있을까?
        this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }

    public org.springframework.data.domain.PageRequest of() {
        return org.springframework.data.domain.PageRequest.of(page - 1, size, direction, PROPERTIES);
    }
}
