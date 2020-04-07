package org.scheduler.example.schedular;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.scheduler.example.domain.Task;
import org.scheduler.example.repository.TaskRepository;
import org.scheduler.example.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.scheduler.example.domain.TaskStatus.OPEN;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@RunWith(SpringRunner.class)
@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Sql(scripts = "/sql/delete-all.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/sql/delete-all.sql")
public class TaskSchedulerServiceIntegrationTest {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskService taskService;

    private TaskSchedulerService taskSchedulerService;

    @Before
    public void setUp(){
        taskSchedulerService = new TaskSchedulerService(taskService);
    }

    @Test
    public void spawnNewTask() {
        assertTrue(repository.findAll().isEmpty());
        taskSchedulerService.spawnNewTask();
        List<Task> tasks = repository.findAll();
        assertFalse(tasks.isEmpty());
        assertEquals(tasks.size(), 1);
        tasks.forEach(conversionRate -> {
            assertNotNull(conversionRate.getId());
            assertNotNull(conversionRate.getStatus());
            assertEquals(conversionRate.getStatus(), OPEN);
            assertNotNull(conversionRate.getDueDate());
            assertNotNull(conversionRate.getPriority());
            assertNotNull(conversionRate.getDescription());
            assertNotNull(conversionRate.getTitle());
            assertNotNull(conversionRate.getCreatedAt());
        });

    }
}
