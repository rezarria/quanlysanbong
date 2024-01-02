package io.rezarria.service.exceptions;

import io.rezarria.model.FieldHistory;
import io.rezarria.service.status.FieldOrderServiceStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FieldOrderServiceException extends Exception {
    private FieldOrderServiceStatus status;
    private FieldHistory order;

    public static FieldOrderServiceException NotFit(FieldHistory order) {
        return FieldOrderServiceException.builder().order(order).status(FieldOrderServiceStatus.NOFIT).build();
    }
}
