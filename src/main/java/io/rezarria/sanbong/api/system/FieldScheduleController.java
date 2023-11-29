package io.rezarria.sanbong.api.system;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.rezarria.sanbong.schedule.FieldSchedule;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/fieldSchedule")
@RequiredArgsConstructor
public class FieldScheduleController {
    private final FieldSchedule myScheduledService;

    @GetMapping("/lastExecutionTime")
    public ResponseEntity<Instant> getLastExecutionTime() {
        return ResponseEntity.ok(myScheduledService.getLastExecutionTime());
    }

    @GetMapping("/pauseTask")
    public void pauseTask() {
        myScheduledService.pauseTask();
    }

    @GetMapping("/resumeTask")
    public void resumeTask() {
        myScheduledService.resumeTask();
    }

    @PostMapping("/setInterval")
    public void setInterval(@RequestParam long interval) {
        myScheduledService.setFixedRate(interval);
    }

    @GetMapping("/getInterval")
    public long getInterval() {
        return myScheduledService.getFixedRate();
    }
}
