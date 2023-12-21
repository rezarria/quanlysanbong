package io.rezarria.sanbong.api.system;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rezarria.sanbong.dto.PatchDTO;
import io.rezarria.sanbong.dto.post.UserPostDTO;
import io.rezarria.sanbong.mapper.UserMapper;
import io.rezarria.sanbong.model.User;
import io.rezarria.sanbong.security.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.util.Streamable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;
    private final ObjectMapper objectMapper;

    @GetMapping("getName")
    public ResponseEntity<?> getName(@RequestParam UUID id) {
        return ResponseEntity.ok(userService.getRepo().findById(id).orElseThrow().getName());
    }

    @GetMapping(produces = "application/json", name = "/{id}")
    public ResponseEntity<?> getAll(@PathVariable @RequestParam Optional<UUID> id,
                                    @RequestParam Optional<String> name) {
        if (name.isPresent()) {
            Streamable<User> data = userService.getRepo().findAllByNameContaining(name.get(),
                    User.class);
            return ResponseEntity.ok(data.stream());
        }
        if (id.isPresent()) {
            return ResponseEntity.ok(userService.get(id.get()));
        }
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("size")
    public ResponseEntity<Long> getSize() {
        return ResponseEntity.ok(userService.getSize());
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> create(@RequestBody UserPostDTO dto) {
        User user = mapper.userPostDTOToUser(dto);
        userService.create(user);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(consumes = "application/json-patch+json", produces = "application/json")
    @SneakyThrows
    public ResponseEntity<?> update(@RequestBody PatchDTO data) {
        User user = userService.get(data.getId());
        if (user.getLastModifiedDate().equals(data.getTime())) {
            JsonNode nodePatched = data.getPatch().apply(objectMapper.convertValue(user, JsonNode.class));
            user = objectMapper.treeToValue(nodePatched, User.class);
            user = userService.update(user);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> delete(@RequestBody Set<UUID> ids) {
        userService.removeIn(ids);
        return ResponseEntity.ok().build();
    }

}
