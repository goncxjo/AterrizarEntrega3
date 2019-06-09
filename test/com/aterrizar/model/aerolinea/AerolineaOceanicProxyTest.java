package com.aterrizar.model.aerolinea;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aterrizar.enumerator.Destino;
import com.aterrizar.enumerator.Ubicacion;
import com.aterrizar.exception.AsientoLanchitaNoDisponibleException;
import com.aterrizar.exception.AsientoNoDisponibleException;
import com.aterrizar.exception.ParametroVacioException;
import com.aterrizar.model.asiento.AsientoDTO;
import com.aterrizar.model.asiento.Ejecutivo;
import com.aterrizar.model.asiento.PrimeraClase;
import com.aterrizar.model.asiento.Turista;
import com.aterrizar.model.usuario.NoRegistrado;
import com.aterrizar.model.usuario.Usuario;
import com.aterrizar.model.vueloasiento.VueloAsiento;
import com.aterrizar.model.vueloasiento.VueloAsientoFiltro;
import com.aterrizar.model.vueloasiento.VueloAsientoFiltroBuilder;
import com.aterrizar.util.date.DateHelper;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

public class AerolineaOceanicProxyTest {
    private AerolineaOceanicProxy aerolineaOceanicProxy;

    @Mock private AerolineaOceanic mockOceanic;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
       }


    @Test
    public void asientosDisponiblesParaOrigen_ObtenerTodosAsientosDesdeBUE() throws ParametroVacioException {

        //Asientos disponibles con vuelos desde Buenos Aires
        when(mockOceanic.asientosDisponiblesParaOrigen("BUE", "31/12/1990"))
                .thenReturn(this.generarAsientosOrigen());
        
        //Cargo datos de aerolinea oceanic al proxy
        aerolineaOceanicProxy = new AerolineaOceanicProxy(mockOceanic);
        
        VueloAsientoFiltro filtro = new VueloAsientoFiltroBuilder()
                .agregarOrigen(Destino.BUE)
                .agregarFecha("31/12/1990")
                .build();

        Usuario usuario = new NoRegistrado("Ricardo \"EL COMANDANTE\"", "Fort)", 37422007);

        List<VueloAsiento> vueloAsientos = aerolineaOceanicProxy
                .filtrarAsientos(filtro, usuario)
                .getAsientos();

        assertTrue("No contiene 4 vuelos", vueloAsientos.size() == 4);
    }

    @Test
    public void asientosDisponiblesParaOrigenYDestino_ObtenerAsientosDesdeBUEaMEX() throws ParametroVacioException {
        //Asientos disponibles con vuelos desde Buenos Aires a Mexico
        when(mockOceanic.asientosDisponiblesParaOrigenYDestino("BUE", "31/12/1990","MEX"))
                .thenReturn(this.generarAsientosDeBUEaMEX());
        
        aerolineaOceanicProxy = new AerolineaOceanicProxy(mockOceanic);

        VueloAsientoFiltro filtro = new VueloAsientoFiltroBuilder()
                .agregarOrigen(Destino.BUE)
                .agregarDestino(Destino.MEX)
                .agregarFecha("31/12/1990")
                .build();

        Usuario usuario = new NoRegistrado("Ricardo \"EL COMANDANTE\"", "Fort)", 37422007);

        List<VueloAsiento> vueloAsientos = aerolineaOceanicProxy
                .filtrarAsientos(filtro, usuario)
                .getAsientos();

        assertTrue("No contiene 2 vuelos", vueloAsientos.size() == 2);
    }

    @Test
    public void asientosDisponiblesParaOrigenYDestino_ObtenerAsientosDesdeBUEaSLA() throws ParametroVacioException {

        //Asientos disponibles con vuelos desde Buenos Aires a Los Angeles
        when(mockOceanic.asientosDisponiblesParaOrigenYDestino("BUE", "31/12/1990","SLA"))
                .thenReturn(this.generarAsientosDeBUEaSLA());
        
        aerolineaOceanicProxy = new AerolineaOceanicProxy(mockOceanic);

        VueloAsientoFiltro filtro = new VueloAsientoFiltroBuilder()
                .agregarOrigen(Destino.BUE)
                .agregarDestino(Destino.SLA)
                .agregarFecha("31/12/1990")
                .build();

        Usuario usuario = new NoRegistrado("Ricardo \"EL COMANDANTE\"", "Fort)", 37422007);

        List<VueloAsiento> vueloAsientos = aerolineaOceanicProxy
                .filtrarAsientos(filtro, usuario)
                .getAsientos();

        assertTrue("No se encontraron vuelos",!vueloAsientos.isEmpty());
    }

    @Test
    public void asientosDisponiblesParaOrigenYDestino_NoObtenerAsientosDesdeBUEaTOK() throws ParametroVacioException {

        //Asientos disponibles con vuelos desde Buenos Aires a Los Angeles
        when(mockOceanic.asientosDisponiblesParaOrigenYDestino("BUE", "31/12/1990","SLA"))
                .thenReturn(this.generarAsientosDeBUEaSLA());
        
        aerolineaOceanicProxy = new AerolineaOceanicProxy(mockOceanic);

        VueloAsientoFiltro filtro = new VueloAsientoFiltroBuilder()
                .agregarOrigen(Destino.BUE)
                .agregarDestino(Destino.TOK)
                .agregarFecha("31/12/1990")
                .build();

        Usuario usuario = new NoRegistrado("Ricardo \"EL COMANDANTE\"", "Fort)", 37422007);

        List<VueloAsiento> vueloAsientos = aerolineaOceanicProxy
                .filtrarAsientos(filtro, usuario)
                .getAsientos();

        assertTrue("Se encontraron vuelos",vueloAsientos.isEmpty());
    }
 
    @Test(expected = ParametroVacioException.class)
    public void asientosDisponiblesParaOrigenYDestino_NoSeAceptaOrigenNulo() throws ParametroVacioException {

        //Asientos disponibles con vuelos desde Buenos Aires a Los Angeles
        when(mockOceanic.asientosDisponiblesParaOrigenYDestino("BUE", "31/12/1990","SLA"))
                .thenReturn(this.generarAsientosDeBUEaSLA());
        
       
        aerolineaOceanicProxy = new AerolineaOceanicProxy(mockOceanic);

        VueloAsientoFiltro filtro = new VueloAsientoFiltroBuilder()
                .agregarFecha("31/12/1990")
                .build();

        Usuario usuario = new NoRegistrado("Ricardo \"EL COMANDANTE\"", "Fort)", 37422007);

        aerolineaOceanicProxy
                .filtrarAsientos(filtro, usuario)
                .getAsientos();

    }

    @Test(expected = ParametroVacioException.class)
    public void asientosDisponiblesParaOrigenYDestino_NoSeAceptaFechaNulo() throws ParametroVacioException {

        //Asientos disponibles con vuelos desde Buenos Aires a Los Angeles
        when(mockOceanic.asientosDisponiblesParaOrigenYDestino("BUE", "31/12/1990","SLA"))
                .thenReturn(this.generarAsientosDeBUEaSLA());
        
       
        aerolineaOceanicProxy = new AerolineaOceanicProxy(mockOceanic);

        VueloAsientoFiltro filtro = new VueloAsientoFiltroBuilder()
                .agregarOrigen(Destino.BUE)
                .agregarDestino(Destino.TOK)
                .build();

        Usuario usuario = new NoRegistrado("Ricardo \"EL COMANDANTE\"", "Fort)", 37422007);

        aerolineaOceanicProxy
                .filtrarAsientos(filtro, usuario)
                .getAsientos();

    }
    
    @Test
    public void estaReservado_asientoNoEstaReservado() {
        //Se carga asiento no reservado
    	when(mockOceanic.estaReservado("OCE 001", 1))
        .thenReturn(false);
    	
    	aerolineaOceanicProxy = new AerolineaOceanicProxy(mockOceanic);
    	
        //El asiento OCE 001 1 esta disponible
        assertFalse("El asiento OCE 001 01 esta reservado",aerolineaOceanicProxy.estaReservado("OCE 001", 1));
    }

    @Test
    public void estaReservado_asientoEstaReservado() {
    	//Se carga asiento reservado
        when(mockOceanic.estaReservado("OCE 010", 1))
        .thenReturn(true);
        
    	aerolineaOceanicProxy = new AerolineaOceanicProxy(mockOceanic);
        
        //El asiento OCE 010 1 esta reservado
        assertTrue("El asiento OCE 010 01 no esta reservado",aerolineaOceanicProxy.estaReservado("OCE 010", 1));
    }

    @Test
    public void comprarSiHayDisponibilidad_SeCompraAsientoDisponible() {

        when(mockOceanic.comprarSiHayDisponibilidad(anyString(), eq("OCE 001"), eq(1)))
        .thenReturn(true);
        
        //El asiento OCE 001 1 se pudo comprar
        assertTrue("El asiento OCE 001 1 no se pudo comprar",
                mockOceanic.comprarSiHayDisponibilidad("40854236", "OCE 001", 1));
    }

    @Test
    public void comprarSiHayDisponibilidad_NoSeCompraAsientoNoDisponible() {
        //El asiento OCE 007 1 no se pudo comprar
        assertFalse("El asiento OCE 007 1  se pudo comprar",
                mockOceanic.comprarSiHayDisponibilidad("40854236", "OCE 007", 1));
    }

    @Test
    public void reservar_SeReservaAsientoDisponible() {
        when(mockOceanic.reservar(anyString(), eq("OCE 001"), eq(1)))
        .thenReturn(true);
        
        //El asiento OCE 001 1 se pudo reservar
        assertTrue("El asiento OCE 001 1 no se pudo reservar",
                mockOceanic.reservar("40854236", "OCE 001", 1));
    }

    @Test
    public void reserva_NoSeReservaAsientoNoDisponible() {
        //El asiento OCE 007 1 no se pudo reservar
        assertFalse("El asiento OCE 007 1  se pudo reservar",
                mockOceanic.reservar("40854236", "OCE 007", 1));
    }


    //Generar asientos  Origen de Buenos Aires a Los Angeles
    public List<AsientoDTO> generarAsientosDeBUEaSLA(){
        List<AsientoDTO> asientos = new ArrayList();
        Date fechaSalida = DateHelper.parseFromISO8601("31/12/1990");

        asientos.add(new AsientoDTO("OCE 001", 1, fechaSalida, null, 100, new Ejecutivo(), Ubicacion.Centro));
        asientos.add(new AsientoDTO("OCE 002", 1, fechaSalida, null, 110, new Turista(), Ubicacion.Pasillo));
        return asientos;
    }

    //Generar asientos  Origen de Buenos Aires a Mexico
    public List<AsientoDTO> generarAsientosDeBUEaMEX(){
        List<AsientoDTO> asientos = new ArrayList();
        Date fechaSalida = DateHelper.parseFromISO8601("31/12/1990");

        asientos.add(new AsientoDTO("OCE 003", 1, fechaSalida, null, 340, new PrimeraClase(), Ubicacion.Pasillo));
        asientos.add(new AsientoDTO("OCE 004", 1, fechaSalida, null, 200, new Ejecutivo(), Ubicacion.Ventanilla));
        return asientos;
    }

    //Generar asientos  Origen desde Buenos Aires con fecha "31/12/1990"
    public List<AsientoDTO> generarAsientosOrigen(){
        List<AsientoDTO> asientos = new ArrayList();
        asientos.addAll(this.generarAsientosDeBUEaMEX());
        asientos.addAll(this.generarAsientosDeBUEaSLA());
        return asientos;
    }
}
