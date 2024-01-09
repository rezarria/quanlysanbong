package io.rezarria.api.public_access;

import io.rezarria.model.Bill;
import io.rezarria.service.BillService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/api/bill")
public class PublicBillController {
    private final BillService billService;

    @GetMapping("payment")
    public ResponseEntity<?> payment(@RequestParam UUID id, HttpServletRequest request) throws URISyntaxException {
        var status = billService.getVNPAYPaymentStatus(id);
        switch (status) {
            case NONE -> {
                var url = billService.createVNPAY(id, request);
                var headers = new HttpHeaders();
                headers.setLocation(new URI(url));
                return new ResponseEntity<>(headers, HttpStatus.FOUND);
            }
            case PENDING -> {
                var url = billService.getVNPAYUrl(id);
                return new ResponseEntity<>(url, HttpStatus.CREATED);
            }
            case DONE, CANCEL -> {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            case ERROR -> {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            default -> {
                return new ResponseEntity<>(HttpStatus.TOO_EARLY);
            }
        }
    }

    @GetMapping("cancel")
    public ResponseEntity<?> cancel() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("done")
    public ResponseEntity<?> done(HttpServletRequest request) {
        var fields = new HashMap<String, String>();
        for (var params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                fields.put(fieldName, fieldValue);
            }
        }
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");
        var billId = UUID.fromString(fields.get("vnp_TxnRef"));
        var transactionNO = fields.get("vnp_TransactionNo");
        if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
            billService.paymentByid(billId, Bill.PaymentStatus.DONE, transactionNO);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            billService.paymentByid(billId, Bill.PaymentStatus.CANCEL, transactionNO);
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }
}
