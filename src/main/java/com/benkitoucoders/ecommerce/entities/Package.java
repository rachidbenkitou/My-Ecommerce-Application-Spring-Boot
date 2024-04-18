package com.benkitoucoders.ecommerce.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Package implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;
    private String packageDetails;


    @Column(name = "ACTIVE")
    private String active;

    @Column(name = "DATE_CREATION")
    private LocalDateTime dateCreation;

    @Column(name = "USER_CREATION")
    private Long userCreation;

    @Column(name = "DATE_UPDATE")
    private LocalDateTime dateUpdate;

    @Column(name = "USER_UPDATE")
    private Long userUpdate;

    @Column(name = "PRICE")
    private Double price;
    private Double comparePrice;
    private String packageImagePath;
    private String filePath;


}
