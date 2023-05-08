package org.gm2.pdv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Em caso de dúvidas sobre DTO, olhar o Notion, ou rever vídeo

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {

    private long userid;
    private List<ProductDTO> items;
}
