package com.benkitoucoders.ecommerce.entities.quiz;
import com.benkitoucoders.ecommerce.entities.Client;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizSubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private QuizCategory category;

    @ManyToMany
    @JoinTable(
            name = "quiz_subcategory_client", // The name of the join table
            joinColumns = @JoinColumn(name = "subcategory_id"), // The foreign key column for QuizSubCategory
            inverseJoinColumns = @JoinColumn(name = "client_id") // The foreign key column for Client
    )
    private Set<Client> clients = new HashSet<>();


}
