package org.gpaulo.springdemo.controllers;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.gpaulo.springdemo.Application;
import org.gpaulo.springdemo.models.User;
import org.gpaulo.springdemo.repos.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@RunWith(SpringRunner.class)
public class UserControllerIT {
    @Autowired
    private UserRepository userRepo;

    @LocalServerPort
    private int port;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Autowired
    ObjectMapper json;
    private final User gus = new User("Gustavo", "Paulo", 17);
    private User jh = new User("Jhonny", "Paulo");
    private final String USERS_PATH = "/users";
    private final String USER_PATH = "/users/{id}";

    private RequestSpecification spec;

    /**
     * Descriptor of a User field
     */
    private final FieldDescriptor[] USER_DOC = {
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID of the user"),
            fieldWithPath("firstName").type(JsonFieldType.STRING).description("First name of the user"),
            fieldWithPath("lastName").type(JsonFieldType.STRING).description("Last name of the user"),
            fieldWithPath("age").type(JsonFieldType.NUMBER).description("Age of the user") };

    @Before
    public void setUp() {
        userRepo.deleteAll();
        jh.setId(1);
        this.jh = userRepo.save(jh);
        this.spec = new RequestSpecBuilder()
                .addFilter(document("{method-name}/{step}", preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .addFilter(documentationConfiguration(this.restDocumentation)).build().contentType(ContentType.JSON)
                .port(this.port).accept(ContentType.JSON);
    }

    @After
    public void cleanUp() {
        userRepo.deleteAll();
    }

    @Test
    public void postUserShouldInsertUser() throws Exception {
        // given
        JsonPath res = given(this.spec)
                .filter(document("user-create", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        responseFields(USER_DOC)))

                // when
                .when().body(gus).post(USERS_PATH)

                // then
                .then().assertThat().statusCode(is(200)).contentType(ContentType.JSON)

                // check json
                .body("firstName", is(gus.getFirstName())).body("lastName", is(gus.getLastName()))
                .body("age", is(gus.getAge())).body("id", not(is(gus.getId())))

                // extract
                .extract().jsonPath();

        // should exist
        assertThat(userRepo.existsById((long) res.getInt("id"))).isTrue();
    }

    @Test
    public void getUsersShouldReturnAllUsers() throws Exception {
        // init
        userRepo.save(gus);

        // given
        User[] res = given(this.spec)
                .filter(document("user-list", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        responseFields(fieldWithPath("[]").description("An array of users")).andWithPrefix("[].",
                                USER_DOC)))

                // when
                .when().get(USERS_PATH)

                // then
                .then().assertThat().statusCode(is(200)).contentType(ContentType.JSON)

                // check json
                .extract().as(User[].class);

        // assert
        assertThat(res.length).isEqualTo(2);
    }

    @Test
    public void getUsersWithQueryShouldSearchResults() throws Exception {
        // user doc

        // given
        User expected = userRepo.save(gus);
        User[] res = given(this.spec)
                .filter(document("user-search", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        responseFields(fieldWithPath("[]").description("An array of users")).andWithPrefix("[].",
                                USER_DOC),
                        requestParameters(parameterWithName("query").description("Search query"))))

                // when
                .when().param("query", "gus").get(USERS_PATH)

                // then
                .then().assertThat().statusCode(is(200)).contentType(ContentType.JSON)

                // check json
                .extract().as(User[].class);

        // assert
        assertThat(res.length).isEqualTo(1);
        assertThat(res[0].getId()).isEqualTo(expected.getId());
    }

    @Test
    public void getUserShouldReturnIt() throws Exception {
        // given
        given(this.spec)
                .filter(document("user-get", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        responseFields(USER_DOC), pathParameters(parameterWithName("id").description("The user's ID"))))

                // when
                .when().get(USER_PATH, jh.getId())

                // then
                .then().assertThat().statusCode(is(200)).contentType(ContentType.JSON)

                // check json
                .body("firstName", is(jh.getFirstName())).body("lastName", is(jh.getLastName()))
                .body("age", is(jh.getAge())).body("id", is((int) jh.getId()));
    }

    @Test
    public void deleteUserShouldRemoveIt() throws Exception {
        // given
        given(this.spec)
                .filter(document("user-delete", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("id").description("The user's ID"))))

                // when
                .when().delete(USER_PATH, jh.getId())

                // then
                .then().assertThat().statusCode(is(204));

        // should not exist
        assertThat(userRepo.existsById(jh.getId())).isFalse();
    }

    @Test
    public void updateUserShouldUpdateIt() throws Exception {
        // given
        User ros = new User("Rossmy", "Segura");
        given(this.spec)
                .filter(document("user-update", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                        responseFields(USER_DOC), pathParameters(parameterWithName("id").description("The user's ID"))))

                // when
                .when().body(ros).put(USER_PATH, jh.getId())

                // then
                .then().assertThat().statusCode(is(200)).contentType(ContentType.JSON)

                // check response
                .body("firstName", is(ros.getFirstName())).body("lastName", is(ros.getLastName()))
                .body("id", not(is(ros.getId())));

        // should be updated in database
        Optional<User> fin = userRepo.findById(jh.getId());
        assertThat(fin).isPresent();
        assertThat(fin.get().getFirstName()).isNotEqualTo(jh.getFirstName());
        assertThat(fin.get().getLastName()).isNotEqualTo(jh.getLastName());
    }
}
