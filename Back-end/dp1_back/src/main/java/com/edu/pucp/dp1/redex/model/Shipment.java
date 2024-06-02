package com.edu.pucp.dp1.redex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "shipment")
@SQLDelete(sql = "UPDATE shipment SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Shipment extends BaseEntity {

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origen_id", nullable = false)
    private Airport origen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destino_id", nullable = false)
    private Airport destino;

    @Column(name = "tipo", nullable = false)
    private int tipo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    private LocalTime horaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    private LocalTime horaFin;

    @Column(name = "tiempo_activo", nullable = false)
    private double tiempoActivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_sender_id", nullable = false)
    private Client clientSender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_receiver_id", nullable = false)
    private Client clientReceiver;

    @OneToMany(mappedBy = "shipment", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Paquete> paquetes;

    public Shipment(int id) {
        this.setId(id);
    }

    // ToString method for debugging purposes
    @Override
    public String toString() {
        return "Shipment{" +
                "id=" + getId() +
                ", cantidad=" + cantidad +
                ", origen=" + (origen != null ? origen.getCodigoIATA() : "null") +
                ", destino=" + (destino != null ? destino.getCodigoIATA() : "null") +
                ", tipo=" + tipo +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", tiempoActivo=" + tiempoActivo +
                ", clientSender=" + (clientSender != null ? clientSender.getFullName() : "null") +
                ", clientReceiver=" + (clientReceiver != null ? clientReceiver.getFullName() : "null") +
                '}';
    }

    public long calc_max_additional_days() {
    	
    	long max_time = 0;
    	
    	if(this.origen.getCity().getCountry().getContinent().getName().equals(this.destino.getCity().getCountry().getContinent().getName())){ //Es el mismo continente
    		max_time=24*60*60*1000;
    	}
    	else {
    		max_time=48*60*60*1000;
    	}
    	
    	return max_time; 
    }

    public long calc_max_possible_time() {
    	
    	long max_time = 0;
    	
    	if(this.origen.getCity().getCountry().getContinent().getName().equals(this.destino.getCity().getCountry().getContinent().getName())){ //Es el mismo continente
    		max_time=24*60*60*1000;
    	}
    	else {
    		max_time=48*60*60*1000;
    	}
    	
    	LocalDateTime dateOfInterest = LocalDateTime.now();
    	//long difference = ChronoUnit.MINUTES.between(dateOfInterest.atZone(ZoneId.of(this.destino.getTime_zone())),dateOfInterest.atZone(ZoneId.of(this.origen().getTime_zone())));
        long difference = ChronoUnit.MINUTES.between(dateOfInterest.atOffset(ZoneOffset.ofHours(this.destino.getCity().getZonahoraria())).toZonedDateTime(), dateOfInterest.atOffset(ZoneOffset.ofHours(this.origen.getCity().getZonahoraria())).toZonedDateTime());

    	max_time -= difference*60*1000;
    	
    	return max_time; 
    }

}
