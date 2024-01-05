package io.rezarria.api.system;

import io.rezarria.dto.post.OrderDetailPostDTO;
import io.rezarria.dto.post.OrderPostDTO;
import io.rezarria.mapper.OrderMapper;
import io.rezarria.repository.ConsumerProductRepository;
import io.rezarria.repository.ProductPriceRepository;
import io.rezarria.service.BillService;
import io.rezarria.service.FieldHistoryService;
import io.rezarria.service.exceptions.FieldOrderServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/bill")
@RestController
@RequiredArgsConstructor
public class BillController {
    @Lazy
    private final BillService billService;
    @Lazy
    private final FieldHistoryService historyService;
    @Lazy
    private final OrderMapper orderMapper;
    @Lazy
    private final ProductPriceRepository productPriceRepository;
    private final ConsumerProductRepository consumerProductRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody OrderPostDTO dto) throws FieldOrderServiceException {
        var history = historyService.order(dto.customerId(), dto.fieldId(), dto.from(), dto.to());

        var consumerProducts = consumerProductRepository.findAllById(
                dto.details().stream().map(OrderDetailPostDTO::consumerProductId).toList()
        );
        var prices = productPriceRepository.findAllById(
                dto.details().stream().map(OrderDetailPostDTO::priceId).toList()
        );

        var info = dto.details().stream().map(d -> {
            var builder = BillService.OrderInfo.builder();
            builder.price(prices.stream().filter(p -> p.getId().equals(d.priceId())).findFirst().orElseThrow());
            builder.product(consumerProducts.stream().filter(c -> c.getId().equals(d.consumerProductId())).findFirst().orElseThrow());
            builder.count(d.count());
            return builder.build();
        }).toList();


        billService.createOrder(
                info,
                history.getCustomer(),
                dto.paymentMethod(),
                dto.description()
        );

        return ResponseEntity.ok().build();
    }
}
