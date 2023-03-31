package org.gm2.pdv.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    // um id de auto incremento
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // O campo name, vai ter no máximo 100 de tamanho, e não pode ser um campo null
    @Column(length = 100, nullable = false)
    private String name;

    private boolean isEnabled;

    @OneToMany(mappedBy = "user")
    private List<Sale> sales;
}