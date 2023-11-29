package io.rezarria.sanbong.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FileResponse {
    private String name;
    private String url;
    @JsonProperty("download_url")
    private String downloadUrl;
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;
    private long size;
}
