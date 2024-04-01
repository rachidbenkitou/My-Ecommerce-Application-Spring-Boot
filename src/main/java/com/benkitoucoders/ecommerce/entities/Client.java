package com.benkitoucoders.ecommerce.entities;

import com.benkitoucoders.ecommerce.entities.quiz.QuizSubCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    private String firstName;
    private String lastName;
    private String address;
    @JsonProperty("username")
    private String email;
    private String phoneNumber;
    @Column(name = "STATUS_ID")
    private Long statusId;

    private LocalDateTime dateCreation;
    @ManyToOne
    @JoinColumn(name = "STATUS_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private ClientStatus clientStatus;

    @ManyToMany(mappedBy = "clients")
    private Set<QuizSubCategory> quizSubCategories = new HashSet<>();


}
