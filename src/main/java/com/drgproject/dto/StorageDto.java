package com.drgproject.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.drgproject.entity.Storage}
 */
public class StorageDto implements Serializable {

    @NotNull(message = "NotNull")
    private Long id;

    @NotNull(message = "NotNull")
    private String storageName;

    @NotNull(message = "NotNull")
    private String storageRegion;

    @NotNull(message = "NotNull")
    private LocalDate dateCreate;

    protected void onCreate() {
        dateCreate = LocalDate.now();
    }

    public StorageDto(){}

    public StorageDto(String storageName, String storageRegion /*LocalDate dateCreate*/) {
        this.storageName = storageName;
        this.storageRegion = storageRegion;
        /*this.dateCreate = dateCreate;*/
    }

    public Long getId() {
        return id;
    }

    public String getStorageName() {
        return storageName;
    }

    public String getStorageRegion() {
        return storageRegion;
    }

    public LocalDate getDateCreate() {
        onCreate();
        return dateCreate;
    }

    public void setId(@NotNull(message = "NotNull") Long id) {
        this.id = id;
    }

    public void setDateCreate() {
        onCreate();
    }

    public void setStorageRegion(String storageRegion) {
        this.storageRegion = storageRegion;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }
}