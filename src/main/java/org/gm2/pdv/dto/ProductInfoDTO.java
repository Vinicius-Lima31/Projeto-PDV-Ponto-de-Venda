package org.gm2.pdv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfoDTO {
    private long id;
    private String description;
    private int quantity;
}
