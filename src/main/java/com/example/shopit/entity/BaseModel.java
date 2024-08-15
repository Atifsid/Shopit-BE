package com.example.shopit.entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseModel implements Serializable {
    @Serial private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected boolean isActive;
    protected boolean isDeleted;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User updatedBy;

    @PrePersist
    protected void prePersist() {
        createdOn = new Date();
        updatedOn = new Date();
        isActive = true;
        isDeleted = false;
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedOn = new Date();
    }
}