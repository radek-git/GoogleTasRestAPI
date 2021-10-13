package org.example;


import org.apache.http.HttpStatus;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.google.common.truth.Truth.assertThat;
import static org.example.utils.TestUtils.givenJsonRequestWithToken;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskTests {

    private static final String token = "ya29.a0AfH6SMBE5CQtiE7y31trHT5VK_3HgSrdA-44NDLLiBgUii6Mg68b03ksUA86ftzY0vMqm6w" +
            "MBfZKnEdkD2KD9MIYIM0fFsqNJEypxfMUsmDkKohXhPxpv-WLuze4b8guMyngvQXRMHB4OJLT-huVRVsnL4fPSFbU08_WZ86BnAQZ";
    public static final String ADD_TASK_TO_LIST_BY_LIST_ID = "https://tasks.googleapis.com/tasks/v1/lists/%s/tasks";
    public static final String GET_TASK_BY_ID = "https://tasks.googleapis.com/tasks/v1/lists/%s/tasks/%s";
    public static final String TEST_TASKS_LIST_ID = "WWFNODJ4YUx1bVpOSWVwcQ";
    public static final String GET_LIST_OF_TASKS_BY_TASK_LIST_ID = "https://tasks.googleapis.com/tasks/v1/lists/%s/tasks";
    public static final String DELETE_TASK_BY_ID_FROM_TASK_LIST_BY_ID = "https://tasks.googleapis.com/tasks/v1/lists/%s/tasks/%s";
    public static final String UPDATE_TASK_BY_ID_FROM_TASK_LIST_BY_ID = "https://tasks.googleapis.com/tasks/v1/lists/%s/tasks/%s";

    public static String taskId;


    @Test
    @Order(1)
    public void shouldAddNewTask() {
        Task task = givenJsonRequestWithToken(token)
                .body(new CreateTaskRequest("dupa", "costam"))
                .when()
                .post(String.format(ADD_TASK_TO_LIST_BY_LIST_ID, TEST_TASKS_LIST_ID))
                .then()
                .extract().as(Task.class);

        taskId = task.getId();

        String[] elements = task.getSelfLink().split("/");
        String taskListIdFromUrl = elements[6];
        System.out.println(taskListIdFromUrl);

        assertThat(task.getTitle()).isEqualTo("dupa");
        assertThat(task.getNotes()).isEqualTo("costam");
        assertThat(task.getId()).isNotNull();
        assertThat(taskListIdFromUrl).isEqualTo(TEST_TASKS_LIST_ID);
    }


    @Test
    @Order(2)
    public void shouldGetTaskById() {
        Task task = givenJsonRequestWithToken(token)
                .when()
                .get(String.format(GET_TASK_BY_ID, TEST_TASKS_LIST_ID, taskId))
                .then()
                .extract().as(Task.class);

        assertThat(task.getId()).isEqualTo(taskId);
        assertThat(task.getTitle()).isEqualTo("dupa");
        assertThat(task.getNotes()).isEqualTo("costam");
    }

    @Test
    @Order(3)
    public void getListOfTaskByTaskListId() {
        Tasks tasks = givenJsonRequestWithToken(token)
                .when()
                .get(String.format(GET_LIST_OF_TASKS_BY_TASK_LIST_ID, TEST_TASKS_LIST_ID))
                .then()
                .extract().as(Tasks.class);

        assertThat(tasks.getItems().size()).isEqualTo(12);
    }

    @Test
    @Order(4)
    public void shouldDeleteTaskByIdFromTaskListById() {
        givenJsonRequestWithToken(token)
                .when()
                .delete(String.format(DELETE_TASK_BY_ID_FROM_TASK_LIST_BY_ID, TEST_TASKS_LIST_ID, taskId))
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }


    @Test
    @Order(5)
    public void shouldUpdateTaskByIdFromTaskListById() {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .withZone(ZoneId.of("UTC"));

        Task task = givenJsonRequestWithToken(token)
                .body(new PatchTaskRequest("completed", formatter.format(LocalDateTime.now())))
                .when()
                .patch(String.format(UPDATE_TASK_BY_ID_FROM_TASK_LIST_BY_ID, TEST_TASKS_LIST_ID, taskId))
                .then()
                .extract().as(Task.class);

        LocalDate localDate = LocalDateTime.parse(task.getCompleted(), formatter).toLocalDate();

        assertThat(task.getStatus()).isEqualTo("completed");
        assertThat(localDate).isEqualTo(LocalDate.now());
    }


    //do zrobienia move i clear
}


