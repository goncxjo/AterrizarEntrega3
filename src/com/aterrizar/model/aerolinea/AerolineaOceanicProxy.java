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
    protected void validarParametros(VueloAsientoFiltro filtro) throws ParametroVacioException {
        Destino origen = filtro.getOrigen();
        String fecha = filtro.getFecha();

        if(origen == null) {
            throw new ParametroVacioException("El origen no puede estar vacío");
        }

        if(fecha == null || fecha.equals("")) {
            throw new ParametroVacioException("La fecha no puede estar vacía");
        }
    }

    @Override
    protected List getAsientosDisponiblesPorAerolinea(VueloAsientoFiltro filtro) {
        List asientosDisponibles = new ArrayList();

        if(filtro.getDestino() == null) {
           //Se obtienen asientos  con origen
            asientosDisponibles.addAll(this.aerolineaOceanic.asientosDisponiblesParaOrigen(
                    getCodigoCiudadFormatoOceanic(filtro.getOrigen())
                    , filtro.getFecha()
            ));
        } else {
            //Se obtienen asientos con origen y destino
            asientosDisponibles.addAll(this.aerolineaOceanic.asientosDisponiblesParaOrigenYDestino(
                    getCodigoCiudadFormatoOceanic(filtro.getOrigen())
                    , filtro.getFecha()
                    , getCodigoCiudadFormatoOceanic(filtro.getDestino())
            ));
        }

        return asientosDisponibles;
    }

    private String getCodigoCiudadFormatoOceanic(Destino destino) {
        String codigo = destino.name();

        if(destino.equals(Destino.LA)) {
            codigo = "SLA";
        } else if (codigo.length() < 2) {
            codigo.concat("_");
        }

        return codigo;
    }

    @Override
    protected Asiento generarAsiento(Object asiento, Usuario usuario) {
	//Se formatea el objeto asiento para usar sus metodos y propiedades
        AsientoDTO asientoGenerado = (AsientoDTO) asiento;

        Asiento nuevoAsiento = asientoGenerado.getClaseAsiento();
        nuevoAsiento.setEstado(Estado.Disponible);
        nuevoAsiento.setUbicacion(asientoGenerado.getUbicacion());
        nuevoAsiento.setPrecio(asientoGenerado.getPrecio() + usuario.getRecargo());
        nuevoAsiento.setCodigoAsiento(asientoGenerado.getCodigoVuelo() + "-" + asientoGenerado.getNumeroAsiento());

        return nuevoAsiento;
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
	

	public boolean estaReservado(String codigoDeVuelo, Integer numeroDeAsiento) {
		return this.aerolineaOceanic.estaReservado(codigoDeVuelo, numeroDeAsiento);
	}
}
