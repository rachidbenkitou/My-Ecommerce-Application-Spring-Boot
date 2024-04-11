package com.benkitoucoders.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@Builder
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    private String name;

    private String description;

    private Double price;

    private Double comparePrice;

    private Integer quantity;

    private Integer productOrder;

    private String visibility;
    private String productImagePath;

    @Column(name = "CATEGORY_ID")
    private Long categoryId;



    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate dateCreated;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDate dateUpdated;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private Category category;



}
