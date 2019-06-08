package com.aterrizar.model.aerolinea;

import java.util.ArrayList;
import java.util.List;

import com.aterrizar.exception.AsientoNoDisponibleException;
import com.aterrizar.exception.ParametroVacioException;
import com.aterrizar.model.asiento.AsientoDTO;
import com.aterrizar.model.usuario.Usuario;
import com.aterrizar.model.vueloasiento.VueloAsientoFiltro;

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
        
        
       /* if(filtro.getDestino() == null) {
            asientosDisponibles = this.aerolineaOceanic.asientosDisponiblesParaOrigen(
                    filtro.getOrigen().name()
                    , filtro.getFecha()
            );*/
        	
        //}else{

        	asientosDisponibles = this.aerolineaOceanic.
        							   asientosDisponiblesParaOrigenYDestino
        							   	(filtro.getOrigen().toString(), 
        							   	 filtro.getFecha(),
        							   	 filtro.getDestino().toString());
        //}
        

        if(!asientosDisponibles.isEmpty()) {
           // mapearAsientos(filtro, usuario, asientosDisponibles);
        }

        return this;
    }


	@Override
    public void comprar(String codigoAsiento, Usuario usuario) throws AsientoNoDisponibleException {

    }
}
