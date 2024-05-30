package com.edu.pucp.dp1.redex.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "country")
@SQLDelete(sql = "UPDATE country SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Country extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "shortname", nullable = false)
    private String shortname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "continent_id", nullable = false)
    private Continent continent;

    // ToString method for debugging purposes
    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", shortname='" + shortname + '\'' +
                ", continent=" + (continent != null ? continent.getName() : "null") +
                '}';
    }
}
