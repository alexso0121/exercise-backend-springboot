package com.Acerise.System_api.dto.Exercise;

import lombok.Builder;
import org.springframework.core.io.Resource;

@Builder
public record AccessFileDto(
        Resource exerciseFile,
        Resource audioFile
) {
}
