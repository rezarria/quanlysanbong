package io.rezarria.file;

import lombok.Builder;

@Builder
public record FileInfo(String name, String path, long size) {
}
