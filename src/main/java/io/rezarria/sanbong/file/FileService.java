package io.rezarria.sanbong.file;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.util.Strings;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.SneakyThrows;

@Service
public class FileService implements IFileService {

    private final Path root;

    @SneakyThrows
    public FileService(FileConfiguration configuration) {
        String location = configuration.getLocation();
        if (Strings.isBlank(location))
            throw new Exception("location trống");
        root = Paths.get(location).toAbsolutePath();
    }

    @SneakyThrows
    @Override
    public FileInfo store(MultipartFile file, String... path) {
        if (file.isEmpty()) {
            throw new Exception("File rỗng");
        }
        Path dest = Path.of(root.toString(), path).normalize();
        if (!dest.getParent().endsWith(root))
            throw new Exception("không thể lưu ở bên ngoài");
        try (InputStream stream = file.getInputStream(); var outputStream = Files.newOutputStream(dest)) {
            ImageIO.write(ImageIO.read(stream), "webp", outputStream);
        }
        return new FileInfo(file.getOriginalFilename(), root.relativize(dest).toString(), dest.toFile().getTotalSpace());
    }

    @SneakyThrows
    @Override
    public Stream<Path> loadAll() {
        return Files.walk(root, 1).filter(path -> !path.equals(root)).map(root::relativize);
    }

    @Override
    public Path load(String first, String... path) {
        return root.resolve(Path.of(first, path));
    }

    @Override
    public Path load(String filename) {
        return root.resolve(filename);
    }

    @SneakyThrows
    @Override
    public Resource loadAsResource(String first, String... paths) {
        Path path = load(first, paths);
        return new UrlResource(path.toUri());
    }

    @SneakyThrows
    @Override
    public Resource loadAsResource(String filename) {
        Path path = load(filename);
        return new UrlResource(path.toUri());
    }
}
