package com.example.demo;

import com.example.demo.domain.Article;
import com.example.demo.domain.Board;
import com.example.demo.domain.Comment;
import com.example.demo.domain.User;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.IntStream;

@SpringBootApplication
@Slf4j
public class DemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
    log.info("start");
  }

  // todo 테스트 유저, security 구현 후 삭제
  @Bean
  public CommandLineRunner runner(UserRepository userRepository, BoardRepository boardRepository,
      ArticleRepository articleRepository, CommentRepository commentRepository) throws Exception {
    return (args -> {
      User user = userRepository.save(User.builder().account("testId").name("kodakyung").password("password").build());

      IntStream.rangeClosed(1, 3).forEach(index -> boardRepository.save(Board.builder().name("게시판" + index).build()));

      Board board1 = boardRepository.findById(1L).get();
      IntStream.rangeClosed(1, 70).forEach(index -> articleRepository.save(Article.builder().title("article" + index)
          .user(user).board(board1).createdIP("127.0.0.1").contents("contents").userName(user.getName()).build()));

      Board board2 = boardRepository.findById(2L).get();
      IntStream.rangeClosed(71, 250).forEach(index -> articleRepository.save(Article.builder().title("article" + index)
          .user(user).board(board2).createdIP("127.0.0.1").contents("contents").userName(user.getName()).build()));

      Board board3 = boardRepository.findById(3L).get();
      IntStream.rangeClosed(251, 300).forEach(index -> articleRepository.save(Article.builder().title("article" + index)
          .user(user).board(board3).createdIP("127.0.0.1").contents("contents").userName(user.getName()).build()));

      Article foundArticle = articleRepository.findById(1L).get();
      IntStream.rangeClosed(1, 30).forEach(index -> commentRepository
          .save(Comment.builder().contents("comment" + index).user(user).article(foundArticle).build()));
    });
  }
}
