package org.gpaulo.springdemo.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.gpaulo.springdemo.models.User;
import org.gpaulo.springdemo.repos.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserDAOTest {
    // skip this from spring scanning
    @TestConfiguration
    static class UserDAOTestContextConfiguration {
        private UserRepository userRepo;

        @Autowired
        public UserDAOTestContextConfiguration(UserRepository userRepo) {
            this.userRepo = userRepo;
        }

        @Bean
        public UserService userService() {
            return new UserDAO(userRepo);
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepo;

    private final User gus = new User("Gustavo", "Paulo", 17);
    private final User jh = new User("Jhonny", "Paulo", 39);
    private final User john = new User("John", "Doe", 30);

    @Before
    public void setUp() {
        // save
        given(userRepo.save(gus)).willReturn(gus);

        // list
        given(userRepo.findAll()).willReturn(Arrays.asList(gus, john));
        given(userRepo.search("gus")).willReturn(Arrays.asList(gus));

        // get
        given(userRepo.findById(1L)).willReturn(Optional.of(gus));
        given(userRepo.findById(100L)).willReturn(Optional.empty());

        // update
        given(userRepo.existsById(2L)).willReturn(true);
        given(userRepo.existsById(100L)).willReturn(false);
        given(userRepo.save(jh)).willReturn(jh);
    }

    @Test
    public void shouldSaveNewUsers() {
        assertThat(userService.save(gus).getFirstName())
            .isEqualTo(gus.getFirstName());
    }

    @Test
    public void whenNoQuery_shouldReturnAllUsers() {
        assertThat(userService.list(Optional.empty()))
            .containsOnly(gus, john);
        assertThat(userService.list())
            .containsOnly(gus, john);
    }

    @Test
    public void whenHasQuery_shouldReturnSomeUsers() {
        assertThat(userService.list(Optional.of("gus")))
            .containsOnly(gus);
    }

    @Test
    public void whenUserExists_shouldGetAUser() {
        assertThat(userService.get(1))
            .isPresent().get()
            .isEqualTo(gus);
    }

    @Test
    public void whenUserDoesNotExist_shouldGetEmpty() {
        assertThat(userService.get(100))
            .isNotPresent();
    }

    @Test
    public void whenUserExists_shouldUpdateIt() {
        assertThat(userService.update(2L, jh))
            .isPresent().get()
            .isEqualTo(jh);
    }

    @Test
    public void whenUserDoesNotExist_shouldNotUpdate() {
        assertThat(userService.update(100L, jh))
            .isNotPresent();
    }

    @Test
    public void whenUserExists_shouldDeleteIt() {
        assertThat(userService.delete(2L)).isTrue();
    }

    @Test
    public void whenUserDoesNotExist_shouldNotDelete() {
        assertThat(userService.delete(100L)).isFalse();
    }
}
