package org.gm2.pdv.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "product")
public class Product {

    // um id de auto incremento
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    @NotBlank(message = "O campo descrição é obrigatório")
    private String description;

    @Column(length = 20, precision = 20, scale = 2, nullable = false)
    @NotBlank(message = "O campo preço é obrigatório")
    private BigDecimal price;

    @Column(nullable = false)
    @NotBlank(message = "O campo quantidade é obrigatório")
    private int quantity;
}
