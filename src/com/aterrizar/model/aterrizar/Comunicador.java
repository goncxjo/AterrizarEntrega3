package com.aterrizar.model.aterrizar;

import com.aterrizar.exception.AsientoNoDisponibleException;
import com.aterrizar.exception.ParametroVacioException;
import com.aterrizar.model.aerolinea.Aerolinea;
import com.aterrizar.model.aerolinea.AerolineaLanchitaProxy;
import com.aterrizar.model.asiento.Asiento;
import com.aterrizar.model.usuario.Usuario;
import com.aterrizar.model.vueloasiento.VueloAsientoFiltro;

import java.util.List;

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
    public void comprar(String codigoAsiento, int dni) throws AsientoNoDisponibleException {
        if(codigoAsiento.contains(this.aerolineaLanchitaProxy.getCodigo())) {
            this.aerolineaLanchitaProxy.comprar(codigoAsiento, dni);
        } else {
            throw new AsientoNoDisponibleException("El asiento no existe");
        }
    }


    private Aerolinea agregarAsientosLanchita(VueloAsientoFiltro filtro, Usuario usuario) throws ParametroVacioException {
        this.vueloAsientos.addAll(
                aerolineaLanchitaProxy
                        .filtrarAsientos(filtro, usuario)
                        .buscarSuperOfertas(usuario)
                        .getVueloAsientos()
        );

        return this;
    }


    @Override
    protected List getAsientosDisponiblesPorAerolinea(VueloAsientoFiltro filtro) {
        return null;
    }

    @Override
    protected Asiento generarAsiento(Object asiento, Usuario usuario) {
        return null;
    }
}
