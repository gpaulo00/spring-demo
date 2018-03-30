package org.gpaulo.springdemo.controllers;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.gpaulo.springdemo.models.User;
import org.gpaulo.springdemo.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    private final String path = "/users";
    private final String singlePath = this.path + "/{id}";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;

    @Autowired
    ObjectMapper json;
    private final User gus = new User("Gustavo", "Paulo", 17);
    private final User jh = new User("Jhonny", "Paulo");
    private final String invalid = "bad";

    @Before
    public void setUp() {
        // post
        given(service.save((User) notNull())).willReturn(gus);

        // get
        given(service.get(1)).willReturn(Optional.of(gus));
        given(service.get(100)).willReturn(Optional.empty());

        // delete
        given(service.delete(2)).willReturn(true);
        given(service.delete(100)).willReturn(false);

        // update
        given(service.update(eq(2L), any())).willReturn(Optional.of(jh));
        given(service.update(eq(100L), any())).willReturn(Optional.empty());
    }

    @Test
    public void whenPost_thenInsertUser() throws Exception {
        // given
        String body = json.writeValueAsString(gus);

        // assert
        mvc.perform(post(path).characterEncoding("UTF-8").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().json(body));

        // asert (invalid input)
        mvc.perform(post(path).characterEncoding("UTF-8").content(invalid).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenGetUsers_thenReturnSearchResults() throws Exception {
        // given
        String query = "Pau";
        String search = this.path + "?query={q}";

        // expected
        List<User> allUsers = Arrays.asList(gus);
        List<User> empty = Arrays.asList();
        String body = json.writeValueAsString(allUsers);
        String bad = json.writeValueAsString(empty);

        // when has query
        given(service.list(any())).willReturn(allUsers);
        mvc.perform(get(search, query).characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().json(body));

        // when has not query
        mvc.perform(get(path).characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().json(body));

        // when invalid
        given(service.list(any())).willReturn(empty);
        mvc.perform(get(search, invalid).characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().json(bad));
    }

    @Test
    public void whenGetID_thenFindUser() throws Exception {
        // given
        String body = json.writeValueAsString(gus);

        // when exists
        mvc.perform(get(singlePath, 1).characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().json(body));

        // when does not exist
        mvc.perform(get(singlePath, 100).characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteID_thenRemoveUser() throws Exception {
        // when exists
        mvc.perform(delete(singlePath, 2).characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // when does not exist
        mvc.perform(delete(singlePath, 100).characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdateID_thenUpdateUser() throws Exception {
        // given
        String body = json.writeValueAsString(jh);

        // when exists
        mvc.perform(put(singlePath, 2).characterEncoding("UTF-8").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().json(body));

        // when does not exist
        mvc.perform(
                put(singlePath, 100).characterEncoding("UTF-8").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
