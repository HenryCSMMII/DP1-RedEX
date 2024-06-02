package com.edu.pucp.dp1.redex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "city")
@SQLDelete(sql = "UPDATE city SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class City extends BaseEntity {

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "abreviatura", nullable = false)
    private String abreviatura;

    @Column(name = "zonahoraria", nullable = false)
    private int zonahoraria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPais", nullable = false)
    private Country country;

    @OneToOne(mappedBy = "city", fetch = FetchType.LAZY)
    @JsonBackReference
    private Airport airport;

    public City(City city){
        this.nombre = city.getNombre();
        this.abreviatura = city.getAbreviatura();
        this.zonahoraria = city.getZonahoraria();
        if(city.getCountry()!=null) this.country = new Country(city.getCountry());
    }


    // ToString method for debugging purposes
    @Override
    public String toString() {
        return "City{" +
                "nombre='" + nombre + '\'' +
                ", abreviatura='" + abreviatura + '\'' +
                ", zonahoraria=" + zonahoraria +
                ", country=" + (country != null ? country.getName() : "null") +
                '}';
    }
}
