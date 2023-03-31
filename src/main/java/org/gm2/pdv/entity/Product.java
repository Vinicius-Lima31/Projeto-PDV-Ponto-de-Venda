package org.gm2.pdv.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private String description;

    @Column(length = 20, precision = 20, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int quantity;
}
