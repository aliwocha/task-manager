package com.github.aliwocha.taskmanager.config;

import com.github.aliwocha.taskmanager.entity.Task;
import com.github.aliwocha.taskmanager.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class TaskConfig {

    private static final Logger LOG = LoggerFactory.getLogger(TaskConfig.class);
    private final SimpleDateFormat dateFormat;
    private final TaskRepository taskRepository;

    public TaskConfig(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        dateFormat = new SimpleDateFormat("HH:mm:ss");
    }

    @PostConstruct
    @Scheduled(cron = "0 0 0 1/1 * ?")
    private void updateAllTasksStatus() {
        taskRepository.findAll()
                .forEach(this::updateStatus);

        LOG.info("All tasks status updated - " + dateFormat.format(new Date()));
    }

    private void updateStatus(Task task) {
        if (task.getDeadline() != null) {
            if (task.isOverdue() && task.getStatus() != Task.Status.COMPLETED) {
                task.setStatus(Task.Status.OVERDUE);
                taskRepository.save(task);
            }
        }
    }
}
