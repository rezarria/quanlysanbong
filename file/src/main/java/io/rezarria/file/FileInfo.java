package io.rezarria;

import lombok.Builder;

@Builder
public record FileInfo(String name, String path, long size) {
}
