package ar.com.ada.hoteltresvagos.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.NaturalId;

import ar.com.ada.hoteltresvagos.excepciones.*;

@Entity
@Table(name = "huesped")
public class Huesped {
    @Id
    @Column(name = "huesped_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // aviso que huespedId es autoINCREMENTAL
    private int huespedId;
    private String nombre; //si se llama igual a la tabla no hace falta el @Column
    @NaturalId
    private int dni;
    private String domicilio;
    @Column(name = "domicilio_alternativo")
    private String domicilioAlternativo;

    @OneToMany(mappedBy = "huesped", cascade = CascadeType.ALL) // un huesped puede tener muchas reservas
    List <Reserva> reservas = new ArrayList<>();

    //CONTRUCTORES

    public Huesped(String nombre) {
        this.nombre = nombre;}

    public Huesped() {}

    //GETTERS AND SETTERS 

    public int getHuespedId() {
        return huespedId;
    }

    public void setHuespedId(int huespedId) {
        this.huespedId = huespedId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre; 
    }

    public void setDni(int dni) throws HuespedDNIException {

        if (dni < 0) {
            // no se ejecuta nada mas despues del throw
            throw new HuespedDNIException(this, "ocurrio un error con el DNI");

        }
        this.dni = dni;
    }

    public int getDni() {
        return dni;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio; 
    }

    public String getDomicilioAlternativo() {
        return domicilioAlternativo; 
    }

    public void setDomicilioAlternativo(String domicilioAlternativo) {
        this.domicilioAlternativo = domicilioAlternativo; 
    }

    public List<Reserva> getReservas() {
        return reservas; 
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

     @Override
    public String toString() {
        return "Huesped [dni=" + dni + ", nombre=" + nombre + "]";
    }

}