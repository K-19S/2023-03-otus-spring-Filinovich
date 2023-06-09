package ru.otus.filinovich;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.otus.filinovich.service.testing.TestingService;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class ApplicationStartup {

    private final TestingService testingService;

    @EventListener(ApplicationReadyEvent.class)
    public void startUp() {
        testingService.startTest();
        System.exit(0);
    }
}
