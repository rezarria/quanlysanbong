package io.rezarria.sanbong.api.system;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.rezarria.sanbong.service.OrganizationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
public class OrganizationController {
    private final OrganizationService service;
}
