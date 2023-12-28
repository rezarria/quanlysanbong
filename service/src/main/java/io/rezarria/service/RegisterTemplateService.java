package io.rezarria.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import io.rezarria.model.RegisterTemplate;
import io.rezarria.repository.RegisterTemplateRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterTemplateService {
    private final RegisterTemplateRepository registerTemplateRepository;

    public Optional<RegisterTemplate> getNewest() {
        return registerTemplateRepository.getNewest();
    }
}
