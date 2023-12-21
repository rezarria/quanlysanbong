package io.rezarria.sanbong.api.system;

import io.rezarria.sanbong.file.FileService;
import io.rezarria.sanbong.response.FileResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@RestController
@RequestMapping("/api/files")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/download")
    public ResponseEntity<?> download(@RequestParam String file) {
        var resource = fileService.loadAsResource(file);
        return ResponseEntity.ok().header("Content-Type", "image/webp")
                .body(resource);
    }

    @PostMapping()
    public ResponseEntity<?> upload(@RequestPart Collection<MultipartFile> file) {
        Collection<FileResponse> result = file.stream().map(fileService::store)
                .map(i -> FileResponse.builder().name(i.name()).url("/api/files/download?file=" + i.path()).thumbnailUrl("/api/files/download?file=" + i.path())
                        .size(i.size()).build())
                .toList();
        return ResponseEntity.ok(result);
    }
}
