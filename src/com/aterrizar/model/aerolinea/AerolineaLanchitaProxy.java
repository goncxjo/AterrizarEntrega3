package com.aterrizar.model.aerolinea;

import com.aterrizar.enumerator.asiento.Estado;
import com.aterrizar.exception.AsientoLanchitaNoDisponibleException;
import com.aterrizar.exception.AsientoNoDisponibleException;
import com.aterrizar.exception.ParametroVacioException;
import com.aterrizar.model.Vuelo;
import com.aterrizar.model.asiento.*;
import com.aterrizar.enumerator.Ubicacion;
import com.aterrizar.model.usuario.Usuario;
import com.aterrizar.model.vueloasiento.VueloAsientoFiltro;
import com.aterrizar.model.vueloasiento.VueloAsiento;
import com.aterrizar.util.date.DateHelper;

import java.util.*;
import java.util.stream.Collectors;

public class AerolineaLanchitaProxy extends Aerolinea {
    private AerolineaLanchita aerolineaLanchita;

    public AerolineaLanchitaProxy(AerolineaLanchita aerolineaLanchita) {
        super("LCH", "Lanchita");
        this.aerolineaLanchita = aerolineaLanchita;
    }

    @Override
    public Aerolinea filtrarAsientos(VueloAsientoFiltro filtro, Usuario usuario) throws ParametroVacioException {
        ValidarParametros(filtro);
        usuario.agregarFiltroAlHistorial(filtro);

        List<List<String>> asientosDisponibles = this.aerolineaLanchita.asientosDisponibles(
                filtro.getOrigen().name()
                , filtro.getFecha()
                , filtro.getDestino().name()
                , null
        );

        if(!asientosDisponibles.isEmpty()) {
            mapearAsientos(filtro, usuario, asientosDisponibles);
        }

        return this;
    }

    @Override
    public void comprar(String codigoAsiento, Usuario usuario) throws AsientoNoDisponibleException {
        try {
            this.aerolineaLanchita.comprar(codigoAsiento);
            usuario.agregarVueloComprado(getVueloAsiento(codigoAsiento));
        } catch (AsientoLanchitaNoDisponibleException e) {
            throw new AsientoNoDisponibleException("aerolinea Lanchita: " + e.getMessage());
        }
    }

    private void ValidarParametros(VueloAsientoFiltro filtro) throws ParametroVacioException {
        String origen = filtro.getOrigen().name();
        String destino = filtro.getDestino().name();
        String fecha = filtro.getFecha();

        if(origen == null || origen.equals("")) {
            throw new ParametroVacioException("El origen no puede estar vacío");
        }
        if(destino == null || destino.equals("")) {
            throw new ParametroVacioException("El destino no puede estar vacío");
        }
        if(fecha == null || fecha.equals("")) {
            throw new ParametroVacioException("La fecha no puede estar vacía");
        }
    }

    private void mapearAsientos(VueloAsientoFiltro filtro, Usuario usuario, List<List<String>> asientosDisponibles) {
        this.asientos = asientosDisponibles
                .stream()
                .map(asiento -> generarVueloAsiento(asiento, filtro, usuario))
                .collect(Collectors.toList());
    }

    private VueloAsiento generarVueloAsiento(List<String> asiento, VueloAsientoFiltro filtro, Usuario usuario) {
        /*
         * [0] el código de asiento (número de vuelo seguido por un guión y luego seguido por el número de asiento)
         * [1] el precio definido por la aerolínea para ese asiento
         * [2] la clase en la que se encuentra el asiento (turista, ejecutiva o primera clase)
         * [3] la ubicación del asiento en el avión (ventana, centro o pasillo)
         * [4] el estado del asiento (reservado o disponible, por el momento solo se reciben vueloAsientos disponibles)
         */

        Asiento asientoGenerado = getTipoAsientoPorCodigoLanchita(asiento.get(2));

        asientoGenerado.setCodigoAsiento(asiento.get(0));
        asientoGenerado.setPrecio(Double.parseDouble(asiento.get(1)) + usuario.getRecargo());
        asientoGenerado.setUbicacion(Ubicacion.getUbicacionPorCodigoLanchita(asiento.get(3)));
        asientoGenerado.setEstado(Estado.getEstadoPorCodigoLanchita(asiento.get(4)));

        return new VueloAsiento(
                this.codigo
                , this.nombre
                , new Vuelo(
                        filtro.getOrigen()
                        , filtro.getDestino()
                        , DateHelper.parseToDate(filtro.getFecha())
                )
                , asientoGenerado
        );
    }

    private Asiento getTipoAsientoPorCodigoLanchita(String codigoTipoAsiento) {

        switch (codigoTipoAsiento) {
            case "E":
                return new Ejecutivo();
            case "P":
                return new PrimeraClase();
            case "T":
                return new Turista();
            default:
                return null;
        }
    }

    private VueloAsiento getVueloAsiento(String codigoAsiento) {
        return this.asientos
                .stream()
                .filter(x -> x.getAsiento().getCodigoAsiento() == codigoAsiento)
                .findFirst()
                .get();
    }
}
