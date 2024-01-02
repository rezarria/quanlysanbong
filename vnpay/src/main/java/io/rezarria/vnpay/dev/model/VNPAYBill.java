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
@StartName(name = "Bill")
public class VNPAYBill {
    private String mobile;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String country;
    private String state;
}
