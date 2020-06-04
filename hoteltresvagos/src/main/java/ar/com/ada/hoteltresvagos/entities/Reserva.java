package ar.com.ada.hoteltresvagos.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="reserva")
public class Reserva {

    @Id
    @Column(name="reserva_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AVISO QUE ES AUTOINCREMENTAL   
    private int reservaId;
    @Column(name="fecha_reserva")
    private Date fechaReserva;
    @Column(name="fecha_ingreso")
    private Date fechaIngreso;
    @Column(name="fecha_egreso")
    private Date fechaEgreso;
    private Integer habitacion; // al llamarse igual no hace falta el @Column
    @Column(name="importe_reserva")
    private BigDecimal importeReserva;
    @Column(name="importe_total")
    private BigDecimal importeTotal;
    @Column(name="importe_pagado")
    private BigDecimal importePagado;
    @Column(name="tipo_estadopago_id")
    private int tipoEstadoPagoId; // por ahora vamos a crear como int
    
    @ManyToOne
    @JoinColumn(name = "huesped_id", referencedColumnName = "huesped_id") // es como declarar la foreing key
    private Huesped huesped;

  

    //GETTERS Y SETTERS

    public int getReservaId() {
        return reservaId;
    }

    public void setReservaId(int reservaId) {
        this.reservaId = reservaId;
    }

    public Date getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(Date fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaEgreso() {
        return fechaEgreso;
    }

    public void setFechaEgreso(Date fechaEgreso) {
        this.fechaEgreso = fechaEgreso;
    }

    public Integer getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Integer habitacion) {
        this.habitacion = habitacion;
    }

    public BigDecimal getImporteReserva() {
        return importeReserva;
    }

    public void setImporteReserva(BigDecimal importeReserva) {
        this.importeReserva = importeReserva;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }

    public BigDecimal getImportePagado() {
        return importePagado;
    }

    public void setImportePagado(BigDecimal importePagado) {
        this.importePagado = importePagado;
    }

    public int getTipoEstadoPagoId() {
        return tipoEstadoPagoId;
    }

    public void setTipoEstadoPagoId(int tipoEstadoPagoId) {
        this.tipoEstadoPagoId = tipoEstadoPagoId;
    }

    public Huesped getHuesped() {
        return huesped;
    }

    public void setHuesped(Huesped huesped) {
        this.huesped = huesped;
        this.huesped.getReservas().add(this);  // al crear un huesped la agrega a la lista de reservas del huesped
    }
    
}