package com.benkitoucoders.ecommerce.entities.quiz;

import com.benkitoucoders.ecommerce.entities.Client;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private LocalDate expiration_date;
    private Double discount_value;
    private LocalDateTime created_at;

    @ManyToOne
    @JoinColumn(name = "client_id" , referencedColumnName = "ID", insertable = false, updatable = false)
    private Client client;
}
