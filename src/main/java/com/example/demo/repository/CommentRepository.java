package com.example.demo.repository;

import com.example.demo.domain.Article;
import com.example.demo.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByArticle(Article article, Pageable pageable);
}
