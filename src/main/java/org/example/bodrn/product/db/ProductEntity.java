package org.example.bodrn.product.db;

import jakarta.persistence.*;
import lombok.*;
import org.example.bodrn.category.db.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "product"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 3000)
    private String description;

    @Column(name = "price")
    private String price;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "service_photos",
            joinColumns = @JoinColumn(name = "service_id")
    )
    @Column(name = "photo_url", nullable = false, length = 1024)
    private List<String> photos = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;
}
