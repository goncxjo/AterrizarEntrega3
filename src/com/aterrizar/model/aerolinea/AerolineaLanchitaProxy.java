package com.aterrizar.model.aerolinea;

import com.aterrizar.enumerator.asiento.Estado;
import com.aterrizar.exception.AsientoLanchitaNoDisponibleException;
import com.aterrizar.exception.AsientoNoDisponibleException;
import com.aterrizar.model.asiento.*;
import com.aterrizar.enumerator.Ubicacion;
import com.aterrizar.model.usuario.Usuario;
import com.aterrizar.model.vueloasiento.VueloAsientoFiltro;

import java.util.*;

public class AerolineaLanchitaProxy extends Aerolinea {
    private AerolineaLanchita aerolineaLanchita;

    public AerolineaLanchitaProxy(AerolineaLanchita aerolineaLanchita) {
        super("LCH", "Lanchita");
        this.aerolineaLanchita = aerolineaLanchita;
    }

    @Override
    protected List getAsientosDisponiblesPorAerolinea(VueloAsientoFiltro filtro) {
        return this.aerolineaLanchita.asientosDisponibles(
                filtro.getOrigen().name()
                , filtro.getFecha()
                , filtro.getDestino().name()
                , null
        );
    }

    protected Asiento generarAsiento(Object asiento, Usuario usuario) {
        List<String> asientoGenerado = (List<String>) asiento;

        switch (asientoGenerado.get(2)) {
            case "E":
                return new Ejecutivo(
                        asientoGenerado.get(0)
                        , Double.parseDouble(asientoGenerado.get(1)) + usuario.getRecargo()
                        , getUbicacionPorCodigo(asientoGenerado.get(3))
                        , getEstadoPorCodigo(asientoGenerado.get(4))
                );
            case "P":
                return new PrimeraClase(
                        asientoGenerado.get(0)
                        , Double.parseDouble(asientoGenerado.get(1)) + usuario.getRecargo()
                        , getUbicacionPorCodigo(asientoGenerado.get(3))
                        , getEstadoPorCodigo(asientoGenerado.get(4))
                );
            case "T":
                return new Turista(
                        asientoGenerado.get(0)
                        , Double.parseDouble(asientoGenerado.get(1)) + usuario.getRecargo()
                        , getUbicacionPorCodigo(asientoGenerado.get(3))
                        , getEstadoPorCodigo(asientoGenerado.get(4))
                );
            default:
                return null;
        }
    }

    private Estado getEstadoPorCodigo(String inicial) {
        switch (inicial) {
            case "D":
                return Estado.Disponible;
            case "R":
                return Estado.Reservado;
            default:
                return Estado.valueOf(inicial);
        }
    }

    private Ubicacion getUbicacionPorCodigo(String inicial) {
        switch (inicial) {
            case "C":
                return Ubicacion.Centro;
            case "P":
                return Ubicacion.Pasillo;
            case "V":
                return Ubicacion.Ventanilla;
            default:
                return Ubicacion.valueOf(inicial);
        }
    }

    @Override
    public void comprar(String codigoAsiento, Usuario usuario) throws AsientoNoDisponibleException {
        try {
            this.aerolineaLanchita.comprar(codigoAsiento);
            usuario.agregarVueloComprado(getVueloAsiento(codigoAsiento));
            eliminarSobreReservas(codigoAsiento);
        } catch (AsientoLanchitaNoDisponibleException e) {
            throw new AsientoNoDisponibleException(this.nombre + ": " + e.getMessage());
        }
    }

    @Override
    public void reservar (Asiento asiento, Usuario usuario) {
        if (asiento.getEstado().estaDisponible()) {
            this.aerolineaLanchita.reservar(asiento.getCodigoAsiento(), Integer.toString(usuario.getDNI()));
        } else {
            this.asientosSobreReservados.add(new AsientoSobreReservado(asiento.getCodigoAsiento(), Integer.toString(usuario.getDNI())));
        }
    }

    @Override
    public void transferenciaDeReserva (String codigoAsiento) {
        List<AsientoSobreReservado> proximosAsientos = (List<AsientoSobreReservado>) asientosSobreReservados
                .stream()
                .filter(asiento -> codigoAsiento.equals(asiento.getCodigoAsiento())
        );
        if (!proximosAsientos.isEmpty()) {
            this.aerolineaLanchita.reservar(proximosAsientos.get(0).getCodigoAsiento(), proximosAsientos.get(0).getDNI());
            this.asientosSobreReservados.remove(proximosAsientos.get(0));
        }
    }

    @Override
    protected void eliminarSobreReservas (String codigoAsiento) {
        this.asientosSobreReservados.removeIf(asiento -> codigoAsiento.equals(asiento.getCodigoAsiento()));
    }
}
