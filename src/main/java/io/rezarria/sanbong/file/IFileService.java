package io.rezarria.sanbong.file;

import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    default FileInfo store(MultipartFile file) {
        var newName = UUID.randomUUID().toString();
        return store(file, newName);
    }

    FileInfo store(MultipartFile file, String... path);

    Stream<Path> loadAll();

    Path load(String first, String... path);

    Path load(String filename);

    Resource loadAsResource(String first, String... path);

    Resource loadAsResource(String filename);
}
