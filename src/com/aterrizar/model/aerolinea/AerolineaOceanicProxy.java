package com.aterrizar.model.aerolinea;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.aterrizar.enumerator.Destino;
import com.aterrizar.exception.AsientoLanchitaNoDisponibleException;
import com.aterrizar.exception.AsientoNoDisponibleException;
import com.aterrizar.exception.ParametroVacioException;
import com.aterrizar.model.Vuelo;
import com.aterrizar.model.asiento.Asiento;
import com.aterrizar.model.asiento.AsientoDTO;
import com.aterrizar.model.usuario.Usuario;
import com.aterrizar.model.vueloasiento.VueloAsiento;
import com.aterrizar.model.vueloasiento.VueloAsientoFiltro;
import com.aterrizar.util.date.DateHelper;

public class AerolineaOceanicProxy extends Aerolinea {
    private AerolineaOceanic aerolineaOceanic;

    public AerolineaOceanicProxy(AerolineaOceanic aerolineaOceanic) {
        super("OCE", "Oceanic");
        this.aerolineaOceanic = aerolineaOceanic;
    }

    @Override
    public Aerolinea filtrarAsientos(VueloAsientoFiltro filtro, Usuario usuario) throws ParametroVacioException {
    	List<AsientoDTO> asientosDisponibles = new ArrayList<AsientoDTO>();
    	validarParametros(filtro);
        usuario.agregarFiltroAlHistorial(filtro);
        
        
       if(filtro.getDestino() == null) {
    	   //Se obtienen asientos  con origen
            asientosDisponibles = this.aerolineaOceanic.asientosDisponiblesParaOrigen(
                    filtro.getOrigen().name()
                    , filtro.getFecha()
            );
        	
        }else{
        	//Se obtienen asientos con origen y destino
        	asientosDisponibles = this.aerolineaOceanic.
        							   asientosDisponiblesParaOrigenYDestino
        							   	(filtro.getOrigen().toString(), 
        							   	 filtro.getFecha(),
        							   	 filtro.getDestino().toString());
        }
        

        if(!asientosDisponibles.isEmpty()) {
           mapearAsientos(filtro, usuario, asientosDisponibles);
        }

        return this;
    }


	private void mapearAsientos(VueloAsientoFiltro filtro, Usuario usuario, List<AsientoDTO> asientosDisponibles) {
        this.asientos = asientosDisponibles
                .stream()
                .map(asiento -> generarVueloAsiento(asiento, filtro, usuario))
                .collect(Collectors.toList());
		
	}

	private VueloAsiento generarVueloAsiento(AsientoDTO asiento, VueloAsientoFiltro filtro, Usuario usuario) {
        return new VueloAsiento(
                this.codigo
                , this.nombre
                , new Vuelo(
                        filtro.getOrigen()
                        , filtro.getDestino()
                        , DateHelper.parseToDate(filtro.getFecha())
                )
                , asiento.getClaseAsiento()
        );
	}

	@Override
    public void comprar(String codigoAsiento, Usuario usuario) throws AsientoNoDisponibleException {
    /*    if ( this.aerolineaOceanic.comprar(codigoAsiento)) {
            usuario.agregarVueloComprado(getVueloAsiento(codigoAsiento));
        };*/
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
