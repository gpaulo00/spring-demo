package org.gpaulo.springdemo.repos;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.gpaulo.springdemo.models.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  // write test cases here
  @Profile("test")
  @Test
  public void whenSearch_thenReturnResults() {
    // given
    User bob = new User("Bob", "__test__", 6);
    User ashley = new User("Ashley", "__test__", 40);
    entityManager.persist(bob);
    entityManager.persist(ashley);
    entityManager.flush();

    // when
    HashMap<String, List<User>> tests = new HashMap<>();
    tests.put("__test__", Arrays.asList(bob, ashley));
    tests.put("ash", Arrays.asList(ashley));

    for (Map.Entry<String, List<User>> test : tests.entrySet()) {
      List<String> result = userRepository.search(test.getKey()).stream()
        .map((User u) -> u.toString())
        .collect(Collectors.toList());
      assertThat(result)
        .isNotEmpty()
        .containsAll(
          test.getValue().stream().map((User u) -> u.toString()).collect(Collectors.toList())
        );
    }
  }
}
