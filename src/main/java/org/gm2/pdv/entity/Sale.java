package org.gm2.pdv.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "sale")
public class Sale {

    // ID AutoIncrement
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sale_date", nullable = false)
    private LocalDate date;

    // AGORA, vamos fazer um relacionamente entre tabelas, relacionamento de chave primaria
    // Cardinalidade de muitos para um, eu deixei bem anotado no Notion em relação a isso

    // ManyToOne (Muitos para um)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // OneToMany (Um para muitos)
    // Expliquei a função do mappedBY e do fetch no Notion
    @OneToMany(mappedBy = "sale", fetch = FetchType.LAZY)
    private List<ItemSale> items;

}
