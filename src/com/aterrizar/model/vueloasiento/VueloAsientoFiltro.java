package com.aterrizar.model.vueloasiento;

import com.aterrizar.enumerator.Destino;
import com.aterrizar.enumerator.vueloasiento.TipoOrden;
import com.aterrizar.model.asiento.Asiento;
import com.aterrizar.enumerator.Ubicacion;

import java.util.ArrayList;
import java.util.List;

public class VueloAsientoFiltro {
    private Destino origen;
    private Destino destino;
    private String fecha;
    private List<Asiento> tipoAsientos = new ArrayList();
    private Ubicacion ubicacion;
    private double precioMinimo = Double.MIN_VALUE;
    private double precioMaximo = Double.MAX_VALUE;
    private TipoOrden tipoOrden;

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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<Asiento> getTipoAsientos() {
        return tipoAsientos;
    }

    public void setTipoAsientos(List<Asiento> tipoAsientos) {
        this.tipoAsientos = tipoAsientos;
    }

    public void agregarTipoAsiento(Asiento asiento) {
        this.tipoAsientos.add(asiento);
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public double getPrecioMinimo() {
        return precioMinimo;
    }

    public void setPrecioMinimo(double precioMinimo) {
        this.precioMinimo = precioMinimo;
    }

    public double getPrecioMaximo() {
        return precioMaximo;
    }

    public void setPrecioMaximo(double precioMaximo) {
        this.precioMaximo = precioMaximo;
    }

    public TipoOrden getTipoOrden() {
        return tipoOrden;
    }

    public void setTipoOrden(TipoOrden tipoOrden) {
        this.tipoOrden = tipoOrden;
    }
}
