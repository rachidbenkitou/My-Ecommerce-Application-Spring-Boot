package com.benkitoucoders.ecommerce.entities.quiz;


import com.benkitoucoders.ecommerce.entities.Package;
import com.benkitoucoders.ecommerce.entities.Product;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
public class DiscountCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "CODE")
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DISCOUNT")
    private Double discount;

    private DiscountCouponType discountCouponType;

    @Column(name = "ACTIVE")
    private String active;

    @Column(name = "START_DATE")
    private LocalDateTime startDate;

    @Column(name = "END_DATE")
    private LocalDateTime endDate;

    @Column(name = "PACKAGE_ID")
    private Long packageId;

    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "percentage")
    private Double percentage;
    @Column(name = "amount")
    private Double amount;

    @Column(name = "DATE_CREATION")
    private LocalDateTime dateCreation;

    @Column(name = "USER_CREATION")
    private Long userCreation;

    @Column(name = "DATE_UPDATE")
    private LocalDateTime dateUpdate;

    @Column(name = "USER_UPDATE")
    private Long userUpdate;

    @JoinColumn(name = "PACKAGE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Package aPackage;
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    private Product product;

}
