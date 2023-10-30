package io.rezarria.sanbong.file;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface IFileService {
    default void store(MultipartFile file) {
        store(file, file.getOriginalFilename());
    }

    void store(MultipartFile file, String... path);

    Stream<Path> loadAll();

    Path load(String first, String... path);

    Path load(String filename);

    Resource loadAsResource(String first, String... path);

    Resource loadAsResource(String filename);
}
