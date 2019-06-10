package com.aterrizar.model;

import com.aterrizar.enumerator.Destino;

import java.util.Date;

public class Vuelo {
    private Destino origen;
    private Destino destino;
    private Date fecha;
    private Double tiempoVuelo;
    private double popularidad = 0;

    public Vuelo() {}

    public Vuelo(Destino origen, Destino destino, Date fecha, double tiempoVuelo) {
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.tiempoVuelo = tiempoVuelo;
    }

    public Destino getOrigen() {
        return origen;
    }

    public void setOrigen(Destino origen) {
        this.origen = origen;
    }

    public Destino getDestino() {
        return destino;
    }

    public void setDestino(Destino destino) {
        this.destino = destino;
    }

    public Date getFecha() {
        return fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha= fecha;
    }
    
    public Double getTiempoVuelo() {
        return tiempoVuelo;
    }
    
    public void setTiempoVuelo(Double tiempoVuelo) {
        this.tiempoVuelo= tiempoVuelo;
    }
    
    public Double getPopularidad() {
        return popularidad;
    }
    
    public void increasePopularidad() {
    	popularidad += 1;
    }

}
