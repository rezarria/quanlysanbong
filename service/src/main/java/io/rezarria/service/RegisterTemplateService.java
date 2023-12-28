package io.rezarria.service;

import io.rezarria.model.RegisterTemplate;
import io.rezarria.repository.RegisterTemplateRepository;
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
