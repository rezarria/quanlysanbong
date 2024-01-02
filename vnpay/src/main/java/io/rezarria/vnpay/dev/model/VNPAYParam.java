package io.rezarria.vnpay.dev.model;


import io.rezarria.vnpay.dev.annotation.StartName;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;


@StartName(name = "vnp")
public class VNPAYParam {
    @Getter
    @Setter
    private String secureHash;
    @Getter
    @Setter
    private String version;
    @Getter
    @Setter
    private String command;
    @Getter
    @Setter
    private String orderInfo;
    @Getter
    @Setter
    private String orderType;
    @Getter
    @Setter
    private String txnRef;
    @Getter
    @Setter
    private String ipAddr;
    @Getter
    @Setter
    private String tmnCode;
    private long amount;
    @Getter
    @Setter
    private String currCode;
    @Getter
    @Setter
    private String bankCode;
    @Getter
    @Setter
    private String locale;
    @Getter
    @Setter
    private String returnUrl;
    @Getter
    @Setter
    private Calendar createDate;
    @Getter
    @Setter
    private Calendar expireDate;
    @Getter
    @Setter
    private VNPAYBill bill;
    @Getter
    @Setter
    private VNPAYInv inv;

    public VNPAYParam(String version, String command, String orderInfo, String orderType, String txnRef, String ipAddr, String tmnCode, long amount, String currCode, String locale, String returnUrl, Calendar createDate) {
        this.version = version;
        this.command = command;
        this.orderInfo = orderInfo;
        this.txnRef = txnRef;
        this.ipAddr = ipAddr;
        this.tmnCode = tmnCode;
        this.amount = amount;
        this.currCode = currCode;
        this.locale = locale;
        this.returnUrl = returnUrl;
        this.createDate = createDate;
        this.orderType = orderType;
    }

    public long getAmount() {
        return amount * 100;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
