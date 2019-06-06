package com.aterrizar.model.aerolinea;

import com.aterrizar.exception.AsientoLanchitaNoDisponibleException;
import java.util.List;

public abstract class AerolineaLanchita {
    public abstract List<List<String>> asientosDisponibles(String origen, String fechaSalida, String destino, String fechaLlegada);
    public abstract void comprar(String codigoAsiento) throws AsientoLanchitaNoDisponibleException;
}
