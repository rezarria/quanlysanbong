package io.rezarria.sanbong.api.system;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.rezarria.sanbong.dto.PatchDTO;
import io.rezarria.sanbong.dto.UserPostDTO;
import io.rezarria.sanbong.mapper.UserMapper;
import io.rezarria.sanbong.model.User;
import io.rezarria.sanbong.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;
    private final ObjectMapper objectMapper;

    @GetMapping(produces = "application/json", name = "/{id}")
    public ResponseEntity<?> getAll(@PathVariable @RequestParam Optional<UUID> id,
            @RequestParam Map<String, List<String>> query) {
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
