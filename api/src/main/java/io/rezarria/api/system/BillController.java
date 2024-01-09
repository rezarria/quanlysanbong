package io.rezarria.api.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rezarria.api.action.Model;
import io.rezarria.api.system.AccountController.UpdateDTO;
import io.rezarria.dto.post.OrderDetailPostDTO;
import io.rezarria.dto.post.OrderPostDTO;
import io.rezarria.dto.update.BillUpdateDTO;
import io.rezarria.mapper.BillUpdateDTOMapper;
import io.rezarria.projection.BillInfo;
import io.rezarria.repository.ConsumerProductRepository;
import io.rezarria.repository.ProductPriceRepository;
import io.rezarria.service.BillService;
import io.rezarria.service.FieldHistoryService;
import io.rezarria.service.exceptions.FieldOrderServiceException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.UUID;

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
    @Lazy
    private final ObjectMapper objectMapper;
    @Lazy
    private final BillUpdateDTOMapper billUpdateDTOMapper;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody OrderPostDTO dto) throws FieldOrderServiceException {
        var history = historyService.order(dto.customerId(), dto.fieldId(), dto.priceId(), dto.fieldUnitSettingId(), dto.from(), dto.to());
        if (history == null) throw new ResponseStatusException(HttpStatus.CONFLICT);
        var consumerProducts = dto.details() == null ? null : consumerProductRepository.findAllById(dto.details().stream().map(OrderDetailPostDTO::consumerProductId).toList());
        var prices = dto.details() == null ? null : productPriceRepository.findAllById(dto.details().stream().map(OrderDetailPostDTO::priceId).toList());
        var info = dto.details() == null ? new ArrayList<BillService.OrderInfo>() : dto.details().stream().map(d -> {
            var builder = BillService.OrderInfo.builder();
            if (prices != null)
                builder.price(prices.stream().filter(p -> p.getId().equals(d.priceId())).findFirst().orElseThrow());
            if (consumerProducts != null)
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

    @GetMapping("beforeUpdate")
    public ResponseEntity<?> beforeUpdate(@RequestParam UUID id) {
        return ResponseEntity.ok(billService.getUpdate(id).orElseThrow());
    }

    @PatchMapping
    public ResponseEntity<?> update(@RequestBody UpdateDTO dto) {
        billService.update(Model.update(dto.id(), dto.patch(), objectMapper, billService::getUpdate, billService.getRepo()::findById, billUpdateDTOMapper::apply, BillUpdateDTO.class));
        return ResponseEntity.ok().build();
    }

}
