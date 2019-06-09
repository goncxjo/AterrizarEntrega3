package com.aterrizar.model.aerolinea;

import com.aterrizar.exception.AsientoNoDisponibleException;
import com.aterrizar.exception.ParametroVacioException;
import com.aterrizar.model.usuario.Usuario;
import com.aterrizar.model.vueloasiento.VueloAsiento;
import com.aterrizar.model.vueloasiento.VueloAsientoFiltro;

import java.util.ArrayList;
import java.util.List;

public abstract class Aerolinea {
    protected String codigo;
    protected String nombre;
    protected List<VueloAsiento> asientos = new ArrayList();

    public Aerolinea() {}

    public Aerolinea(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() { return codigo; }

    public String getNombre() { return nombre; }

    public List<VueloAsiento> getAsientos() { return asientos; }

    public abstract Aerolinea filtrarAsientos(VueloAsientoFiltro filtro, Usuario usuario) throws ParametroVacioException;

    public Aerolinea buscarSuperOfertas(Usuario usuario) {
        for (VueloAsiento vueloAsiento : this.asientos) {
            if (usuario.puedeVerSuperOferta(vueloAsiento.getAsiento())) {
                vueloAsiento.getAsiento().marcarComoSuperOferta();
            }
        }

        return this;
    }

    public abstract void comprar(String codigoAsiento, Integer numeroAsiento, Usuario usuario) throws AsientoNoDisponibleException;

    protected void validarParametros(VueloAsientoFiltro filtro) throws ParametroVacioException {};
}
