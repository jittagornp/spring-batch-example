package me.jittagornp.example.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Price {

    private Integer productId;

    private BigDecimal price;

}
