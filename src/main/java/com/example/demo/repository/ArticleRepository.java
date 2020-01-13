package com.example.demo.repository;

import com.example.demo.domain.Article;
import com.example.demo.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.Valid;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAllByBoard(@Valid Board board, Pageable pageable);
}
