package io.rezarria.sanbong.dto;

import java.util.List;

import lombok.Data;

@Data
public class FieldPost {
    private String name;
    private List<String> pictures;
    private String description;
    private double price;
}
