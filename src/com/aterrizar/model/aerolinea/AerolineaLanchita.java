package com.aterrizar.model.aerolinea;

import com.aterrizar.exception.AsientoLanchitaNoDisponibleException;
import com.aterrizar.exception.AsientoLanchitaYaReservadoException;
import com.aterrizar.model.usuario.Usuario;
import com.aterrizar.model.vueloasiento.VueloAsiento;

import java.util.List;

public interface AerolineaLanchita {
    List<List<String>> asientosDisponibles(String origen, String fechaSalida, String destino, String fechaLlegada);
    void comprar(VueloAsiento vueloAsiento) throws AsientoLanchitaNoDisponibleException;
    void reservar(VueloAsiento vueloAsiento, String dni) throws AsientoLanchitaYaReservadoException;
    boolean estaReservado(String codigoAsiento);
}
