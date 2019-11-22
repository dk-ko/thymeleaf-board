package com.example.demo.dto.req;

import com.example.demo.domain.Board;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BoardReqDto {
    @NotEmpty
    private String name;

    @Builder
    public BoardReqDto(String name) {
        this.name = name;
    }

    public Board toEntity() {
        return Board.builder()
                .name(this.name)
                .build();
    }
}
