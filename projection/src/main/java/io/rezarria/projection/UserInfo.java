package io.rezarria.projection;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Date;
import java.util.UUID;

public interface UserInfo {
    UUID getId();

    String getName();

    String getAvatar();

    Date getDob();

    JsonNode getData();
}