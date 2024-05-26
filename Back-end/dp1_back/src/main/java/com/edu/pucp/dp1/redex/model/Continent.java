package com.edu.pucp.dp1.redex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "continent")
@SQLDelete(sql = "UPDATE continent SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Continent extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    // ToString method for debugging purposes
    @Override
    public String toString() {
        return "Continent{" +
                "name='" + name + '\'' +
                '}';
    }
}
