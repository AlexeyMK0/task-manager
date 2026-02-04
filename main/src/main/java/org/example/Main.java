package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication(scanBasePackages = "org.example")
public class Main {

    @Value("${app.task.max-active-count:5}")
    private int userMaxTasks;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    @Transactional
    public TaskService taskService(TaskRepository repository) {
        return new DefaultTaskService(repository, userMaxTasks);
    }
}
