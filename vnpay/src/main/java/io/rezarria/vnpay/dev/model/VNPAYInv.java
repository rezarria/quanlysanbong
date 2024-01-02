package io.rezarria.vnpay.dev.model;

import io.rezarria.vnpay.dev.annotation.Nested;
import io.rezarria.vnpay.dev.annotation.StartName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Nested
@Builder
@StartName(name = "Inv")
public class VNPAYInv {
    private String phone;
    private String emai;
    private String customer;
    private String address;
    private String company;
    private String textcode;
    private String type;
}
