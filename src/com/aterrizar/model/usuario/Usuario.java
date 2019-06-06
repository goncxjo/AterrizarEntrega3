package com.aterrizar.model.usuario;

import com.aterrizar.exception.TipoUsuarioNoDisponibleException;
import com.aterrizar.model.asiento.Asiento;
import com.aterrizar.model.vueloasiento.VueloAsiento;
import com.aterrizar.model.vueloasiento.VueloAsientoFiltro;

import java.util.ArrayList;
import java.util.List;

public abstract class Usuario {
    protected String nombre;
    protected String apellido;
    protected int DNI;
    protected List<VueloAsientoFiltro> historialBusquedas;
    protected List<VueloAsiento> historialCompras;

    public Usuario(String nombre, String apellido, int DNI) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.DNI = DNI;
        this.historialBusquedas = new ArrayList();
        this.historialCompras = new ArrayList();
    }

    public Usuario(Usuario usuario) {
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.DNI = usuario.getDNI();
        this.historialBusquedas = usuario.getHistorialBusquedas();
        this.historialCompras = usuario.getHistorialCompras();
    }

    public Usuario() {}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getDNI() {
        return DNI;
    }

    public void setDNI(int DNI) {
        this.DNI = DNI;
    }

    public List<VueloAsientoFiltro> getHistorialBusquedas() {
        return this.historialBusquedas;
    }

    public void agregarFiltroAlHistorial(VueloAsientoFiltro vuelo) { this.historialBusquedas.add(vuelo); }

    public List<VueloAsiento> getHistorialCompras() { return this.historialCompras; }

    public void agregarVueloComprado(VueloAsiento vueloAsiento) { this.historialCompras.add(vueloAsiento); }

    public float getRecargo() { return 0; }

    public boolean puedeVerSuperOferta(Asiento asiento) { return false; }

    public Usuario actualizarTipo(Usuario nuevoUsuario) throws TipoUsuarioNoDisponibleException {
        throw new TipoUsuarioNoDisponibleException("No existe el usuario solicitado");
    }
}



