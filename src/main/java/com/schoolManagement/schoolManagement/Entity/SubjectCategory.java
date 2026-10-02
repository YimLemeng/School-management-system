package com.schoolManagement.schoolManagement.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "subject_category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectCategory extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
}
