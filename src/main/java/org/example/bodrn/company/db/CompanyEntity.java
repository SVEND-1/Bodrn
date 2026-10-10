package org.example.bodrn.company.db;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "company")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 3000)
    private String description;

    @Column(length = 512)
    private String address;

    @Column(length = 64)
    private String phone;

    @Column(name = "work_hours_weekdays", length = 128)
    private String workHoursWeekdays;

    @Column(name = "work_hours_weekends", length = 128)
    private String workHoursWeekends;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}