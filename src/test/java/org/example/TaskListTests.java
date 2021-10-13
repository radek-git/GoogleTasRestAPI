package org.example;


import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static com.google.common.truth.Truth.assertThat;
import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskListTests {

    private static final String token = "ya29.a0AfH6SMD2EPT1ML6Wt5rLaXiEutYiciWEMn1gsTvRZXMHJc-CDbrkbdPmM1h-nYI7RuKo" +
            "BHHxdBj8tZTFGc01jcQhU10OqonxVkHag2Q9VYNS2m7mHuPkf4YUfpJubWYbgL_ZJQa6ceWJa5HlJKJ4sV5DDoiKVsyms_aJYYvcI4na";
    public static final String GET_TASK_LISTS = "https://tasks.googleapis.com/tasks/v1/users/@me/lists";
    public static final String INSERT_NEW_LIST = "https://tasks.googleapis.com/tasks/v1/users/@me/lists";
    public static final String UPDATE_TASK_LIST = "https://tasks.googleapis.com/tasks/v1/users/@me/lists/%s";
    public static final String GET_LIST_BY_ID = "https://tasks.googleapis.com/tasks/v1/users/@me/lists/%s";
    public static final String DELETE_LIST_BY_ID = "https://tasks.googleapis.com/tasks/v1/users/@me/lists/%s";

    public static String taskListId;

    @Test
    @Order(1)
    public void shouldGetTaskLists() {
        TaskLists response = given()
                .urlEncodingEnabled(false)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when().get(GET_TASK_LISTS)
                .then()
                .extract()
                .as(TaskLists.class);

        System.out.println(response);

        assertThat(response.getItems().size()).isEqualTo(8);
    }

    @Test
    @Order(2)
    public void shouldCreateNewList() {
        TaskList taskList = given()
                .urlEncodingEnabled(false)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(new TaskList(null, null, null, "dupa jasiu karuzela 1", null, null))
                .when()
                .post(INSERT_NEW_LIST)
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(TaskList.class);

        taskListId = taskList.getId();

        assertThat(taskList.getTitle()).isEqualTo("dupa jasiu karuzela 1");
    }

    @Test
    @Order(3)
    public void shouldUpdateTaskList() {
        TaskList taskList = given()
                .urlEncodingEnabled(false)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(new TaskList("tasks#taskList", "WWFNODJ4YUx1bVpOSWVwcQ", "\"NjIxMjkwODUx\"",
                        "jasiu karuzela 15", null, "https://www.googleapis.com/tasks/v1/users/@me/lists/WWFNODJ4YUx1bVpOSWVwcQ"))
                .when().put(String.format(UPDATE_TASK_LIST, "WWFNODJ4YUx1bVpOSWVwcQ"))
                .then()
                .extract().as(TaskList.class);

        assertThat(taskList.getTitle()).isEqualTo("jasiu karuzela 15");
        assertThat(taskList.getId()).isEqualTo("WWFNODJ4YUx1bVpOSWVwcQ");
        assertThat(taskList.getKind()).isEqualTo("tasks#taskList");
        assertThat(taskList.getSelfLink()).isEqualTo("https://www.googleapis.com/tasks/v1/users/@me/lists/WWFNODJ4YUx1bVpOSWVwcQ");

    }

    @Test
    @Order(4)
    public void shouldGetListById() {
        TaskList response = given()
                .urlEncodingEnabled(false)
                .header("Authorization", "Bearer " + token)
                .when().get(String.format(GET_LIST_BY_ID, "WWFNODJ4YUx1bVpOSWVwcQ"))
                .then()
                .extract().as(TaskList.class);

        assertThat(response.getTitle()).isEqualTo("jasiu karuzela 15");
        assertThat(response.getId()).isEqualTo("WWFNODJ4YUx1bVpOSWVwcQ");

    }

    @Test
    @Order(5)
    public void shouldDeleteTaskListById() {
        given()
                .urlEncodingEnabled(false)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(String.format(DELETE_LIST_BY_ID, taskListId))
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);

    }
}
