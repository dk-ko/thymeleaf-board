package com.example.demo.domain;

import com.example.demo.common.JpaTest;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

@Slf4j
public class BaseEntityTest extends JpaTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  public void id값생성테스트() {
    User user = userRepository.save(generatedUser(1));
    log.info(user.toString());
    assertNotNull(user.getIdx());
  }

  @Test
  public void jpaAuditing_createdDate_테스트() {
    User user = userRepository.save(generatedUser(2));
    log.info(user.toString());
    assertNotNull(user.getCreatedDate());
  }

  @Test
  public void jpaAuditing_updatedDate_테스트() {
    // given
    User user = userRepository.saveAndFlush(generatedUser(3));
    User foundUser = userRepository.findById(user.getIdx()).orElseThrow(IllegalArgumentException::new);
    log.info("foundUser create date:{}", foundUser.getCreatedDate());
    log.info("foundUser updated date:{}", foundUser.getUpdatedDate());
    assertEquals(foundUser.getCreatedDate(), foundUser.getUpdatedDate());

    // when
    foundUser.setPassword("1234");
    User changedUser = userRepository.saveAndFlush(foundUser);

    // then
    log.info("changedUser create date:{}", changedUser.getCreatedDate());
    log.info("changedUser update date:{}", changedUser.getUpdatedDate());
    assertTrue(changedUser.getUpdatedDate().isAfter(changedUser.getCreatedDate()));
  }

  private User generatedUser(int index) {
    return User.builder().account("testId" + index).name("testUser" + index).password("password" + index).build();
  }
}