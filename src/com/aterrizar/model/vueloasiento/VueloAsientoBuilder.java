package com.aterrizar.model.vueloasiento;

import com.aterrizar.enumerator.Destino;
import com.aterrizar.enumerator.Ubicacion;
import com.aterrizar.model.asiento.Asiento;

public class VueloAsientoBuilder {
	VueloAsiento vueloAsiento;

    public VueloAsientoBuilder() {
        this.vueloAsiento = new VueloAsiento();
    }

    public VueloAsientoBuilder agregarTipoAsiento(String nombreAerolinea) {
    	vueloAsiento.setNombreAerolinea(nombreAerolinea);
        return this;
    }

    public VueloAsientoFiltroBuilder agregarOrigen(Destino origen) {
    	vueloAsiento.setOrigen(origen);
        return this;
    }

    public VueloAsientoFiltroBuilder agregarDestino(Destino  destino) {
    	vueloAsiento.setDestino(destino);
        return this;
    }

    public VueloAsientoFiltroBuilder agregarFecha(String fecha) {
    	vueloAsiento.setFecha(fecha);
        return this;
    }

    public VueloAsientoFiltroBuilder agregarUbicacion(Ubicacion ubicacion) {
    	vueloAsiento.setUbicacion(ubicacion);
        return this;
    }
    
    public VueloAsientoFiltro build() {
        return vueloAsiento;
    }

}
