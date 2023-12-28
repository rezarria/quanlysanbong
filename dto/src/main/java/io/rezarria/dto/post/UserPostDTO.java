package io.rezarria.post;

import jakarta.annotation.Nullable;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserPostDTO {
    protected String name;
    protected String avatar;
    @Nullable
    protected UUID account;
    protected Date dob;
}
