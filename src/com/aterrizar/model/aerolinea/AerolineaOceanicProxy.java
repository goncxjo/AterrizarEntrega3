package com.aterrizar.model.aerolinea;

import com.aterrizar.enumerator.Destino;
import com.aterrizar.enumerator.asiento.Estado;
import com.aterrizar.exception.AsientoLanchitaNoDisponibleException;
import com.aterrizar.exception.AsientoNoDisponibleException;
import com.aterrizar.exception.AsientoOceanicNoDisponibleException;
import com.aterrizar.exception.ParametroVacioException;
import com.aterrizar.model.Vuelo;
import com.aterrizar.model.asiento.*;
import com.aterrizar.model.usuario.Usuario;
import com.aterrizar.model.vueloasiento.VueloAsiento;
import com.aterrizar.model.vueloasiento.VueloAsientoFiltro;
import com.aterrizar.util.date.DateHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AerolineaOceanicProxy extends Aerolinea {
    private AerolineaOceanic aerolineaOceanic;

    public AerolineaOceanicProxy(AerolineaOceanic aerolineaOceanic) {
        super("OCE", "Oceanic");
        this.aerolineaOceanic = aerolineaOceanic;
    }

    @Override
    public Aerolinea filtrarAsientos(VueloAsientoFiltro filtro, Usuario usuario) throws ParametroVacioException {
        validarParametros(filtro);
        usuario.agregarFiltroAlHistorial(filtro);

        List<AsientoDTO> asientosDisponibles;
        if(filtro.getDestino() == null) {
           //Se obtienen asientos  con origen
            asientosDisponibles = this.aerolineaOceanic.asientosDisponiblesParaOrigen(
                    filtro.getOrigen().name()
                    , filtro.getFecha()
            );
        } else {
            //Se obtienen asientos con origen y destino
            asientosDisponibles = this.aerolineaOceanic.asientosDisponiblesParaOrigenYDestino(
                    filtro.getOrigen().toString()
                    , filtro.getFecha()
                    , filtro.getDestino().toString()
            );
        }

        if(!asientosDisponibles.isEmpty()) {
           mapearAsientos(filtro, asientosDisponibles, usuario);
        }

        return this;
    }


	private void mapearAsientos(VueloAsientoFiltro filtro, List<AsientoDTO> asientosDisponibles, Usuario usuario) {
        this.asientos = asientosDisponibles
                .stream()
                .map(asiento -> generarVueloAsiento(asiento, filtro, usuario))
                .collect(Collectors.toList());
		
	}

	private VueloAsiento generarVueloAsiento(AsientoDTO asiento, VueloAsientoFiltro filtro, Usuario usuario) {
        /*
         * AsientoDTO:
         * ---------------------------------------------------------------------------------
         * Código de vuelo (String)
         * Número de asiento (Integer)
         * Fecha de salida (formato “dd/MM/AAAA”)
         * Hora de salida (formato “hh:mm”)
         * El precio definido por la aerolínea para ese asiento
         * La clase en la que se encuentra el asiento (turista, ejecutiva o primera clase)
         * La ubicación del asiento en el avión (ventana, centro o pasillo)
         * ---------------------------------------------------------------------------------
         * */
        return new VueloAsiento(
                this.codigo
                , this.nombre
                , new Vuelo(
                        filtro.getOrigen()
                        , filtro.getDestino()
                        , DateHelper.parseToDate(filtro.getFecha())
                )
                , generarAsiento(asiento, usuario)
        );
	}

    private Asiento generarAsiento(AsientoDTO asiento, Usuario usuario) {
        Asiento asientoGenerado = asiento.getClaseAsiento();

        asientoGenerado.setEstado(Estado.Disponible);
        asientoGenerado.setUbicacion(asiento.getUbicacion());
        asientoGenerado.setPrecio(asiento.getPrecio() + usuario.getRecargo());
        asientoGenerado.setCodigoAsiento(asiento.getCodigoVuelo() + "-" + asiento.getNumeroAsiento());

        return asientoGenerado;
    }

	@Override
    public void comprar(String codigoAsiento, Usuario usuario) throws AsientoNoDisponibleException {
        String dni = Integer.toString(usuario.getDNI());
        String codigoVuelo = codigoAsiento.split("-")[0];
        Integer numeroAsiento = Integer.parseInt(codigoAsiento.split("-")[1]);

        try {
            this.aerolineaOceanic.comprarSiHayDisponibilidad(dni, codigoVuelo, numeroAsiento);
            usuario.agregarVueloComprado(getVueloAsiento(codigoAsiento));
        } catch (AsientoOceanicNoDisponibleException e) {
            throw new AsientoNoDisponibleException(this.nombre + ": " + e.getMessage());
        }
    }
	
	
	private VueloAsiento getVueloAsiento(String codigoAsiento) throws AsientoOceanicNoDisponibleException {
        Optional<VueloAsiento> vueloAsiento = this.asientos
                .stream()
                .filter(x -> x.getAsiento().getCodigoAsiento().equals(codigoAsiento))
                .findFirst();

        if(vueloAsiento.isPresent()) {
            return vueloAsiento.get();
        } else {
            throw new AsientoOceanicNoDisponibleException("El asiento no existe");
        }
	}

	public boolean estaReservado(String codigoDeVuelo, Integer numeroDeAsiento) {
		return this.aerolineaOceanic.estaReservado(codigoDeVuelo, numeroDeAsiento);
	}
	
	@Override
    protected void validarParametros(VueloAsientoFiltro filtro) throws ParametroVacioException {
        Enum<Destino> origen = filtro.getOrigen();
        String fecha = filtro.getFecha();

        if(origen == null || origen.equals("")) {
            throw new ParametroVacioException("El origen no puede estar vacío");
        }

        if(fecha == null || fecha.equals("")) {
            throw new ParametroVacioException("La fecha no puede estar vacía");
        }
    }
}
