package io.rezarria.api.system;

import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.rezarria.dto.post.OrderDetailPostDTO;
import io.rezarria.dto.post.OrderPostDTO;
import io.rezarria.projection.BillInfo;
import io.rezarria.repository.ConsumerProductRepository;
import io.rezarria.repository.ProductPriceRepository;
import io.rezarria.service.BillService;
import io.rezarria.service.FieldHistoryService;
import io.rezarria.service.exceptions.FieldOrderServiceException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/bill")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")

public class BillController {
    @Lazy
    private final BillService billService;
    @Lazy
    private final FieldHistoryService historyService;
    @Lazy
    private final ProductPriceRepository productPriceRepository;
    @Lazy
    private final ConsumerProductRepository consumerProductRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody OrderPostDTO dto) throws FieldOrderServiceException {
        var history = historyService.order(dto.customerId(), dto.fieldId(), dto.priceId(), dto.from(), dto.to());
        var consumerProducts = consumerProductRepository.findAllById(dto.details().stream().map(OrderDetailPostDTO::consumerProductId).toList());
        var prices = productPriceRepository.findAllById(dto.details().stream().map(OrderDetailPostDTO::priceId).toList());

        var info = dto.details().stream().map(d -> {
            var builder = BillService.OrderInfo.builder();
            builder.price(prices.stream().filter(p -> p.getId().equals(d.priceId())).findFirst().orElseThrow());
            builder.product(consumerProducts.stream().filter(c -> c.getId().equals(d.consumerProductId())).findFirst().orElseThrow());
            builder.count(d.count());
            return builder.build();
        }).toList();
        billService.createOrder(info, history, dto.paymentMethod(), dto.description());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam @Nullable UUID id, @RequestParam @Nullable Integer page, @RequestParam @Nullable Integer size, @RequestParam @Nullable String name) {
        if (id != null) {
            return ResponseEntity.ok(billService.getByIdProjection(id, BillInfo.class).orElseThrow());
        }

        if (page != null && size != null) {
            var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "lastModifiedDate"));
            if (name != null) return ResponseEntity.ok(billService.getPageContainName(name, pageable, BillInfo.class));
            return ResponseEntity.ok(billService.getPage(pageable, BillInfo.class));
        }
        return ResponseEntity.ok(billService.getAll(BillInfo.class));
    }

}
