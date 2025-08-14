package com.fedeherrera.vetapp.vetapp.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "veterinary_information")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VeterinaryInformation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "license_number")
    private String licenseNumber;
    
    @Column(name = "professional_title")
    private String professionalTitle;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "veterinary_specialization",
        joinColumns = @JoinColumn(name = "veterinary_id"),
        inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    private Set<Specialization> specializations = new HashSet<>();
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Métodos de utilidad para manejar la relación bidireccional
    public void addSpecialization(Specialization specialization) {
        this.specializations.add(specialization);
        specialization.getVeterinarians().add(this);
    }
    
    public void removeSpecialization(Specialization specialization) {
        this.specializations.remove(specialization);
        specialization.getVeterinarians().remove(this);
    }
    
    public void clearSpecializations() {
        for (Specialization specialization : new HashSet<>(this.specializations)) {
            removeSpecialization(specialization);
        }
    }
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}