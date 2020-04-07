package org.scheduler.example.controller;

import com.github.fge.jsonpatch.JsonPatch;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.scheduler.example.model.PageRequestDTO;
import org.scheduler.example.model.TaskSearchCriteria;
import org.scheduler.example.repository.TaskRepository;
import org.scheduler.example.util.TestDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static java.time.LocalDateTime.now;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.scheduler.example.domain.TaskStatus.CLOSED;
import static org.scheduler.example.domain.TaskStatus.OPEN;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@RunWith(SpringRunner.class)
@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Sql(scripts = "/sql/task/insert-new-record.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/sql/delete-all.sql")
public class TaskControllerTest {

    private final String URL_SEARCH = "/v1/tasks/search";
    private final String URL_LATEST = "/v1/tasks/latest";
    private final String URL_GET_DELETE_OR_PATCH = "/v1/tasks/{id}";

    @Value("http://localhost:${local.server.port}")
    private String urlPrefix;

    @Autowired
    private TaskRepository taskRepository;

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final UUID CLOSED_TASK_ID = UUID.fromString("a07e5ffc-4ee5-47fa-a48f-cb3a6175ba17");

    @Test
    public void test_searchByFromDate() {
        PageRequestDTO<TaskSearchCriteria> request = new PageRequestDTO<>(
                new TaskSearchCriteria(OPEN, LocalDateTime.parse("2020-04-05 11:10:22", FORMATTER), null), 0, 10, null
        );

        //@formatter:off
        given()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .with()
                .body(request)
        .when()
                .post(urlPrefix + URL_SEARCH)
                .prettyPeek()
        .then()
                .statusCode(OK.value())
                .body("totalPages", is(1))
                .body("totalElements", is(1))
                .body("last", is(true))
                .body("content.dueDate", notNullValue())
                .body("content.resolvedAt", hasItem(nullValue()))
                .body("content.title", hasItem("My Title"))
                .body("content.description", hasItem("My Description"))
                .body("content.priority", hasItem(2))
                .body("content.status", hasItem(OPEN.getDescription()))
                .body("content.createdAt", notNullValue());
        //@formatter:on
    }

    @Test
    public void test_searchByBetweenDates() {
        PageRequestDTO<TaskSearchCriteria> request = new PageRequestDTO<>(
                new TaskSearchCriteria(OPEN, LocalDateTime.parse("2020-04-05 14:10:22", FORMATTER), now()), 0, 10, null
        );

        //@formatter:off
        given()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .with()
                .body(request)
        .when()
                .post(urlPrefix + URL_SEARCH)
                .prettyPeek()
        .then()
                .statusCode(OK.value())
                .body("totalPages", is(1))
                .body("totalElements", is(1))
                .body("last", is(true))
                .body("content.dueDate", notNullValue())
                .body("content.resolvedAt", hasItem(nullValue()))
                .body("content.title", hasItem("My Title"))
                .body("content.description", hasItem("My Description"))
                .body("content.priority", hasItem(2))
                .body("content.status", hasItem(OPEN.getDescription()))
                .body("content.createdAt", notNullValue());
        //@formatter:on
    }

    @Test
    public void test_searchValidationError() {
        PageRequestDTO<TaskSearchCriteria> request = new PageRequestDTO<>(
                new TaskSearchCriteria(OPEN, now(), now().minusDays(1)), 0, 10, null
        );

        //@formatter:off
        given()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .with()
                .body(request)
        .when()
                .post(urlPrefix + URL_SEARCH)
                .prettyPeek()
        .then()
                .statusCode(CONFLICT.value())
                .body("message", is("validation error"))
                .body("fieldErrors.code", hasItems("field.invalid"))
                .body("fieldErrors.defaultMessage", hasItems("To Date cannot be before From Date"))
                .body("fieldErrors.field", hasItems("criteria.dueDateTo"))
                .body("fieldErrors.rejectedValue", notNullValue());
        //@formatter:on
    }

    @Test
    public void test_latest() {
        //@formatter:off
        given()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .when()
                .get(urlPrefix + URL_LATEST)
                .prettyPeek()
        .then()
                .statusCode(OK.value())
                .body("totalPages", is(1))
                .body("totalElements", is(2))
                .body("last", is(true))
                .body("content.dueDate", notNullValue())
                .body("content.resolvedAt", hasItem(nullValue()))
                .body("content.title", hasItem("My Title"))
                .body("content.description", hasItem("My Description"))
                .body("content.priority", hasItem(2))
                .body("content.status", hasItem(OPEN.getDescription()))
                .body("content.createdAt", notNullValue());
        //@formatter:on
    }

    @Test
    public void test_patch_resolved() {
        JsonPatch patch = TestDataUtil.convertJsonFileTo("json/patch-resolved-at.json", JsonPatch.class);

        //@formatter:off
        given()
                .header(CONTENT_TYPE, "application/json-patch+json")
                .with()
                .body(patch)
        .when()
                .patch(urlPrefix + URL_GET_DELETE_OR_PATCH, CLOSED_TASK_ID)
                .prettyPeek()
        .then()
                .statusCode(OK.value())
                .body("dueDate", notNullValue())
                .body("resolvedAt", is("2020-04-05T12:56:22.805"))
                .body("title", is("My Title"))
                .body("description", is("My new Description"))
                .body("priority", is(2))
                .body("status", is(CLOSED.getDescription()))
                .body("createdAt", notNullValue());
        //@formatter:on
    }

    @Test
    public void test_get() {
        //@formatter:off
        given()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .with()
        .when()
                .get(urlPrefix + URL_GET_DELETE_OR_PATCH, CLOSED_TASK_ID)
                .prettyPeek()
        .then()
                .statusCode(OK.value())
                .body("dueDate", notNullValue())
                .body("resolvedAt", nullValue())
                .body("title", is("My Title"))
                .body("description", is("My Description"))
                .body("priority", is(2))
                .body("status", is(CLOSED.getDescription()))
                .body("createdAt", notNullValue());
        //@formatter:on
    }

    @Test
    public void delete() {
        Assert.assertTrue(taskRepository.findById(CLOSED_TASK_ID).isPresent());
        //@formatter:off
        given()
        .when()
                .delete(urlPrefix + URL_GET_DELETE_OR_PATCH, CLOSED_TASK_ID)
                .prettyPeek()
        .then()
                .statusCode(OK.value());
        //@formatter:on
        Assert.assertFalse(taskRepository.findById(CLOSED_TASK_ID).isPresent());
    }
}
