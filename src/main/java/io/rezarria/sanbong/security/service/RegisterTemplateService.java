package io.rezarria.sanbong.security.service;

import io.rezarria.sanbong.model.RegisterTemplate;
import io.rezarria.sanbong.repository.RegisterTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegisterTemplateService {
    private final RegisterTemplateRepository registerTemplateRepository;

    public Optional<RegisterTemplate> getNewest() {
        return registerTemplateRepository.getNewest();
    }
}
