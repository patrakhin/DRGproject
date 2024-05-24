package com.drgproject.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "storage")
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "storage_name")
    private String name;

    @Column(name = "date_create", updatable = false)
    private LocalDate dateCreate;

    @PrePersist
    protected void onCreate() {
        dateCreate = LocalDate.now();
    }

    @OneToMany(mappedBy = "storage")
    private List<LocoBlock> locoBlocks;

    public Storage(){}

    public Storage(String name, LocalDate dateCreate) {
        this.name = name;
        this.dateCreate = dateCreate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LocoBlock> getLocoBlocks() {
        return locoBlocks;
    }

    public void setLocoBlocks(List<LocoBlock> locoBlocks) {
        this.locoBlocks = locoBlocks;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

}
