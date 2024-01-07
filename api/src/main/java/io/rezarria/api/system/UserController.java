package io.rezarria.api.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rezarria.api.action.Model;
import io.rezarria.dto.PatchDTO;
import io.rezarria.dto.post.UserPostDTO;
import io.rezarria.dto.update.UserUpdateDTO;
import io.rezarria.mapper.UserMapper;
import io.rezarria.mapper.UserUpdateDTOMapper;
import io.rezarria.model.User;
import io.rezarria.projection.UserInfo;
import io.rezarria.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;
    private final UserUpdateDTOMapper userUpdateDTOMapper;
    private final ObjectMapper objectMapper;

    @GetMapping("getName")
    public ResponseEntity<?> getName(@RequestParam UUID id) {
        return ResponseEntity.ok(userService.getRepo().findById(id).orElseThrow().getName());
    }

    @GetMapping(produces = "application/json", name = "/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> getAll(@PathVariable @RequestParam @Nullable UUID id,
                                    @RequestParam @Nullable String name,
                                    @RequestParam @Nullable Integer size,
                                    @RequestParam @Nullable Integer page) {
        if (name != null) {
            var data = userService.getRepo().findAllByNameContaining(name, UserInfo.class);
            return ResponseEntity.ok(data.stream());
        }
        if (id != null) {
            return ResponseEntity.ok(userService.getByIdProjection(id, UserInfo.class)
                    .orElseThrow());
        }
        if (size != null && page != null) {
            return ResponseEntity.ok(userService.getPage(Pageable.ofSize(size).withPage(page), UserInfo.class));
        }

        return ResponseEntity.ok(userService.getRepo().findAllStream(UserInfo.class).stream());
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
        Model.update(data.id(), data.patch(), objectMapper, userService.getRepo()::findByIdForUpdate, userService.getRepo()::findById, userUpdateDTOMapper::patch, UserUpdateDTO.class);
        return ResponseEntity.ok().build();
    }

    @GetMapping("beforeUpdate")
    public ResponseEntity<?> getDataBeforeUpdate(@RequestParam UUID id) {
        var data = userService.getRepo().findByIdForUpdate(id);
        return ResponseEntity.ok(data.orElseThrow());
    }

    @DeleteMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> delete(@RequestBody Set<UUID> ids) {
        userService.removeIn(ids);
        return ResponseEntity.ok().build();
    }

}
