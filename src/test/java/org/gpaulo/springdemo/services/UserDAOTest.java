package org.gpaulo.springdemo.services;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Optional;

import org.gpaulo.springdemo.models.User;
import org.gpaulo.springdemo.repos.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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

    User gus = new User("Gustavo", "Paulo", 17);
    User jh = new User("Jhonny", "Paulo", 39);
    User john = new User("John", "Doe", 30);

    @Before
    public void setUp() {
        // save
        Mockito.when(userRepo.save(gus)).thenReturn(gus);

        // list
        Mockito.when(userRepo.findAll()).thenReturn(Arrays.asList(gus, john));
        Mockito.when(userRepo.search("gus")).thenReturn(Arrays.asList(gus));

        // get
        Mockito.when(userRepo.findById(1L)).thenReturn(Optional.of(gus));
        Mockito.when(userRepo.findById(100L)).thenReturn(Optional.empty());

        // update
        Mockito.when(userRepo.existsById(2L)).thenReturn(true);
        Mockito.when(userRepo.existsById(100L)).thenReturn(false);
        Mockito.when(userRepo.save(jh)).thenReturn(jh);
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
