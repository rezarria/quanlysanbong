package io.rezarria.sanbong.api.system;

import io.rezarria.sanbong.schedule.FieldSchedule;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/fieldSchedule")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
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

    @GetMapping("/getInterval")
    public long getInterval() {
        return myScheduledService.getFixedRate();
    }

    @PostMapping("/setInterval")
    public void setInterval(@RequestParam long interval) {
        myScheduledService.setFixedRate(interval);
    }
}
