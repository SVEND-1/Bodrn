package org.example.bodrn.reviews.db;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author_name", nullable = false, length = 255)
    private String authorName;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "text", length = 3000)
    private String text;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
