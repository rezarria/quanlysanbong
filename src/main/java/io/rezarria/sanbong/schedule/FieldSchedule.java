package io.rezarria.sanbong.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class FieldSchedule {
    private final ThreadPoolTaskScheduler scheduler;
    private ScheduledFuture<?> scheduledTask;
    private long fixedRate = 600000;
    private Instant lastExecutionTime;
    private Duration executionDuration;

    @Scheduled(fixedRateString = "${my.scheduled.fixed-rate:600000}")
    public void runTask() {
        Instant currentExecutionTime = Instant.now();
        if (lastExecutionTime != null) {
            executionDuration = Duration.between(lastExecutionTime, currentExecutionTime);
        }
        lastExecutionTime = currentExecutionTime;
    }

    public Instant getLastExecutionTime() {
        return lastExecutionTime;
    }

    public long getFixedRate() {
        return fixedRate;
    }

    public void setFixedRate(long fixedRate) {
        this.fixedRate = fixedRate;
        updateTask();
    }

    public void pauseTask() {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
        }
    }

    public void resumeTask() {
        updateTask();
    }

    private void updateTask() {
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
        }
        scheduledTask = scheduler.scheduleAtFixedRate(this::runTask, Duration.ofMillis(fixedRate));
    }
}
