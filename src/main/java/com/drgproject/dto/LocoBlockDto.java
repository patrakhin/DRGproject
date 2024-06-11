package com.drgproject.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.drgproject.entity.LocoBlock}
 */
public class LocoBlockDto implements Serializable {
    @NotNull(message = "NotNull")
    private Long id;
    @NotNull
    private String systemType;
    @NotNull
    private String blockName;
    @NotNull
    private String blockNumber;
    @NotNull
    private Long uniqueId;

    @NotNull
    private LocalDate dateCreate;

    protected void onCreate() {
        dateCreate = LocalDate.now();
    }

    public LocoBlockDto(){}

    public LocoBlockDto(String systemType,
                        String blockName, String blockNumber, Long uniqueId) {
        this.systemType = systemType;
        this.blockName = blockName;
        this.blockNumber = blockNumber;
        this.uniqueId = uniqueId;
    }

    public Long getId() {
        return id;
    }

    public String getSystemType() {
        return systemType;
    }

    public String getBlockName() {
        return blockName;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setId(@NotNull(message = "NotNull") Long id) {
        this.id = id;
    }

    public void setSystemType(@NotNull String systemType) {
        this.systemType = systemType;
    }

    public void setBlockName(@NotNull String blockName) {
        this.blockName = blockName;
    }

    public void setBlockNumber(@NotNull String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public void setDateCreate() {
        onCreate();
    }

    public LocalDate getDateCreate() {
        onCreate();
        return dateCreate;
    }

    public Long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Long uniqueId) {
        this.uniqueId = uniqueId;
    }
}