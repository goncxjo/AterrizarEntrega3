package com.aterrizar.model.aterrizar;

import com.aterrizar.exception.AsientoNoDisponibleException;
import com.aterrizar.exception.ParametroVacioException;
import com.aterrizar.model.aerolinea.Aerolinea;
import com.aterrizar.model.aerolinea.AerolineaLanchitaProxy;
import com.aterrizar.model.usuario.Usuario;
import com.aterrizar.model.vueloasiento.VueloAsientoFiltro;

public class Comunicador extends Aerolinea {
    AerolineaLanchitaProxy aerolineaLanchitaProxy;

    public Comunicador(AerolineaLanchitaProxy aerolineaLanchitaProxy) {
        this.aerolineaLanchitaProxy = aerolineaLanchitaProxy;
    }

    @Override
    public Aerolinea filtrarAsientos(VueloAsientoFiltro filtro, Usuario usuario) throws ParametroVacioException {
        return agregarAsientosLanchita(filtro, usuario);
    }

    @Override
    public void comprar(String codigoAsiento, Integer numeroAsiento, Usuario usuario) throws AsientoNoDisponibleException {
        if(codigoAsiento.contains(this.aerolineaLanchitaProxy.getCodigo())) {
            this.aerolineaLanchitaProxy.comprar(codigoAsiento, null, usuario);
        } else {
            throw new AsientoNoDisponibleException("El asiento no existe");
        }
    }

    private Aerolinea agregarAsientosLanchita(VueloAsientoFiltro filtro, Usuario usuario) throws ParametroVacioException {
        this.asientos.addAll(
                aerolineaLanchitaProxy
                        .filtrarAsientos(filtro, usuario)
                        .buscarSuperOfertas(usuario)
                        .getAsientos()
        );

        return this;
    }

}
