package com.aterrizar.model.aerolinea;

import com.aterrizar.enumerator.Destino;
import com.aterrizar.exception.AsientoNoDisponibleException;
import com.aterrizar.exception.AsientoYaReservadoException;
import com.aterrizar.exception.ParametroVacioException;
import com.aterrizar.model.Vuelo;
import com.aterrizar.model.asiento.Asiento;
import com.aterrizar.model.usuario.Usuario;
import com.aterrizar.model.vueloasiento.VueloAsiento;
import com.aterrizar.model.vueloasiento.VueloAsientoFiltro;
import com.aterrizar.util.date.DateHelper;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Aerolinea {
    protected String codigo;
    protected String nombre;
    protected List<VueloAsiento> vueloAsientos = new ArrayList();

    protected Aerolinea() {}

    Aerolinea(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() { return codigo; }

    public List<VueloAsiento> getVueloAsientos() { return vueloAsientos; }

    public abstract void comprar(String codigoAsiento, Usuario usuario) throws AsientoNoDisponibleException;

    public abstract void reservar(String codigoAsiento, Usuario usuario) throws AsientoYaReservadoException, AsientoNoDisponibleException;

    public Aerolinea filtrarAsientos(VueloAsientoFiltro filtro, Usuario usuario) throws ParametroVacioException {
        validarParametros(filtro);
        usuario.agregarFiltroAlHistorial(filtro);

        List asientosDisponibles = getAsientosDisponiblesPorAerolinea(filtro);

        if(!asientosDisponibles.isEmpty()) {
            this.vueloAsientos = mapear(asientosDisponibles, filtro, usuario);
        }

        return this;
    }

    public Aerolinea buscarSuperOfertas(Usuario usuario) {
        for (VueloAsiento vueloAsiento : this.vueloAsientos) {
            if (usuario.puedeVerSuperOferta(vueloAsiento.getAsiento())) {
                vueloAsiento.getAsiento().marcarComoSuperOferta();
            }
        }

        return this;
    }

    private void validarParametros(VueloAsientoFiltro filtro) throws ParametroVacioException {
        Destino origen = filtro.getOrigen();
        Destino destino = filtro.getDestino();
        String fecha = filtro.getFecha();

        if(origen == null) {
            throw new ParametroVacioException("El origen no puede estar vacío");
        }
        if(destino == null) {
            throw new ParametroVacioException("El destino no puede estar vacío");
        }
        if(fecha == null || fecha.equals("")) {
            throw new ParametroVacioException("La fecha no puede estar vacía");
        }
    }

    private List<VueloAsiento> mapear(List<Object> asientosDisponibles, VueloAsientoFiltro filtro, Usuario usuario) {
        return asientosDisponibles
                .stream()
                .map(asiento -> new VueloAsiento(
                                this.codigo
                                , this.nombre
                                , new Vuelo(filtro.getOrigen(), filtro.getDestino(), DateHelper.parseToDate(filtro.getFecha()))
                                , generarAsiento(asiento, usuario)
                        )
                )
                .collect(Collectors.toList());
    }

    protected VueloAsiento getVueloAsiento(String codigoAsiento) throws AsientoNoDisponibleException {
        Optional<VueloAsiento> vueloAsiento = this.vueloAsientos
                .stream()
                .filter(x -> x.getAsiento().getCodigoAsiento().equals(codigoAsiento))
                .findFirst();

        if(vueloAsiento.isPresent()) {
            return vueloAsiento.get();
        } else {
            throw new AsientoNoDisponibleException("El asiento no existe");
        }
    }

    protected abstract List getAsientosDisponiblesPorAerolinea(VueloAsientoFiltro filtro);

    protected abstract Asiento generarAsiento(Object asiento, Usuario usuario);
}
