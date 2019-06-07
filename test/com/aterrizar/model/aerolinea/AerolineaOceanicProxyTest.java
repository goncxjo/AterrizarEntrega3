package com.aterrizar.model.aerolinea;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aterrizar.enumerator.Ubicacion;
import com.aterrizar.model.asiento.AsientoDTO;
import com.aterrizar.model.asiento.Ejecutivo;
import com.aterrizar.model.asiento.PrimeraClase;
import com.aterrizar.model.asiento.Turista;
import com.aterrizar.util.date.DateHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

public class AerolineaOceanicProxyTest {
    @Mock private AerolineaOceanic mockOceanic;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        //Asientos disponibles con vuelos desde Buenos Aires
        when(mockOceanic.asientosDisponiblesParaOrigen("BUE", "31/12/1990"))
                .thenReturn(this.generarAsientosOrigen());

        //Asientos disponibles con vuelos desde Buenos Aires a Mexico
        when(mockOceanic.asientosDisponiblesParaOrigenYDestino("BUE", "31/12/1990","MEX"))
                .thenReturn(this.generarAsientosDeBUEaMEX());

        //Asientos disponibles con vuelos desde Buenos Aires a Los Angeles
        when(mockOceanic.asientosDisponiblesParaOrigenYDestino("BUE", "31/12/1990","SLA"))
                .thenReturn(this.generarAsientosDeBUEaSLA());


        //Se indican todos los asientos reservados
        when(mockOceanic.estaReservado("OCE 010", 1))
                .thenReturn(true);

        when(mockOceanic.estaReservado("OCE 004", 2))
                .thenReturn(true);

        when(mockOceanic.estaReservado("OCE 001", 7))
                .thenReturn(true);


        //Se indican asientos que se pueden comprar si no se compro nunca antes
        when(mockOceanic.comprarSiHayDisponibilidad(anyString(), eq("OCE 001"), eq(1)))
                .thenReturn(true);

        when(mockOceanic.comprarSiHayDisponibilidad(anyString(), eq("OCE 002"), eq(1)))
                .thenReturn(true);

        when(mockOceanic.comprarSiHayDisponibilidad(anyString(), eq("OCE 003"), eq(1)))
                .thenReturn(true);

        when(mockOceanic.comprarSiHayDisponibilidad(anyString(), eq("OCE 004"), eq(1)))
                .thenReturn(true);

        //Se indican asientos que se pueden reservar si no se reservo nunca antes
        when(mockOceanic.reservar(anyString(), eq("OCE 001"), eq(1)))
                .thenReturn(true);

        when(mockOceanic.reservar(anyString(), eq("OCE 002"), eq(1)))
                .thenReturn(true);

        when(mockOceanic.reservar(anyString(), eq("OCE 003"), eq(1)))
                .thenReturn(true);

        when(mockOceanic.reservar(anyString(), eq("OCE 004"), eq(1)))
                .thenReturn(true);
    }


    @Test
    public void asientosDisponiblesParaOrigen_ObtenerTodosAsientosDesdeBUE() {
        List<AsientoDTO> asientosOrigen = mockOceanic.asientosDisponiblesParaOrigen("BUE", "31/12/1990");
        //Todos los asientos disponibles para Buenos Aires
        System.out.println("Todos los asientos disponibles para Buenos Aires:");
        for (AsientoDTO asiento: asientosOrigen) {
            System.out.println(asiento.getAsiento());
        }
    }

    @Test
    public void asientosDisponiblesParaOrigenYDestino_ObtenerAsientosDesdeBUEaMEX() {
        List<AsientoDTO> asientos = mockOceanic.asientosDisponiblesParaOrigenYDestino("BUE", "31/12/1990","MEX");
        //Todos los asientos disponibles desde Buenos Aires a Mexico
        System.out.println("Todos los asientos disponibles desde Buenos Aires a Mexico:");
        for (AsientoDTO asiento: asientos) {
            System.out.println(asiento.getAsiento());
        }
    }

    @Test
    public void asientosDisponiblesParaOrigenYDestino_ObtenerAsientosDesdeBUEaSLA() {
        List<AsientoDTO> asientos = mockOceanic.asientosDisponiblesParaOrigenYDestino("BUE", "31/12/1990","SLA");
        //Todos los asientos disponibles desde Buenos Aires a Mexico
        System.out.println("Todos los asientos disponibles desde Buenos Aires a Los Angeles:");
        for (AsientoDTO asiento: asientos) {
            System.out.println(asiento.getAsiento());
        }
    }

    @Test
    public void estaReservado_asientoNoEstaReservado() {
        //El asiento OCE 001 1 esta disponible
        assertFalse("El asiento OCE 001 01 esta reservado",mockOceanic.estaReservado("OCE 001", 1));
    }

    @Test
    public void estaReservado_asientoEstaReservado() {
        //El asiento OCE 010 1 esta reservado
        assertTrue("El asiento OCE 010 01 no esta reservado",mockOceanic.estaReservado("OCE 010", 1));
    }

    @Test
    public void comprarSiHayDisponibilidad_SeCompraAsientoDisponible() {
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
