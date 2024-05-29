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
    private String storageName;

    @Column(name = "date_create", updatable = false)
    private LocalDate dateCreate;

    @PrePersist
    protected void onCreate() {
        dateCreate = LocalDate.now();
    }

    @OneToMany(mappedBy = "storage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocoBlock> locoBlock;

    @OneToMany(mappedBy = "storage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocoBlockTransaction> transactions;

    public Storage(){}

    public Storage(String storageName, LocalDate dateCreate) {
        this.storageName = storageName;
        this.dateCreate = dateCreate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public List<LocoBlock> getLocoBlock() {
        return locoBlock;
    }

    public void setLocoBlock(List<LocoBlock> locoBlock) {
        this.locoBlock = locoBlock;
    }

    public List<LocoBlockTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<LocoBlockTransaction> transactions) {
        this.transactions = transactions;
    }
}
