package io.rezarria.sanbong.file;

import lombok.Builder;

@Builder
public record FileInfo(String name, String path, long size) {
}
