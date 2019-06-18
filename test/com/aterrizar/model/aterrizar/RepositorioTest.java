package com.aterrizar.model.aterrizar;

import com.aterrizar.exception.AsientoLanchitaYaReservadoException;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.aterrizar.enumerator.Destino;
import com.aterrizar.enumerator.Ubicacion;
import com.aterrizar.enumerator.asiento.Estado;
import com.aterrizar.exception.AsientoNoDisponibleException;
import com.aterrizar.model.Vuelo;
import com.aterrizar.model.aerolinea.AerolineaLanchita;
import com.aterrizar.model.aerolinea.AerolineaLanchitaProxy;
import com.aterrizar.model.asiento.*;
import com.aterrizar.model.usuario.*;
import com.aterrizar.model.vueloasiento.VueloAsiento;
import com.aterrizar.util.date.DateHelper;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.Before;

public class RepositorioTest {
	private Repositorio repositorio;
	@Mock AerolineaLanchita mockLanchita;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		repositorio = new Repositorio(new Comunicador(new AerolineaLanchitaProxy(mockLanchita)));
	}

	@Test
	public void reservar_AsientoDisponible_ReservaUnAsientoDisponible() throws AsientoNoDisponibleException, AsientoLanchitaYaReservadoException {
	    String codigoAsiento = "LCH 005-40";

		Usuario usuario = new Estandar("Ricardo \"EL COMANDANTE\"", "Fort)", 37422007);

		VueloAsiento vueloAsiento = new VueloAsiento(
				"Lanchita"
				, "LCH"
				, new Vuelo(Destino.EZE, Destino.MIA, DateHelper.parseToDate("13/05/2019"), 10.0, 5.0)
				, new Ejecutivo(codigoAsiento, 50000, Ubicacion.Centro, Estado.Disponible)
				);

		when(mockLanchita.estaReservado(anyString())).thenReturn(false);
		boolean estaReservadoAntesDeReservar = repositorio.estaReservado(codigoAsiento);

		doNothing().when(mockLanchita).reservar(anyObject(), anyString());
		repositorio.reservar(vueloAsiento, usuario);

		when(mockLanchita.estaReservado(anyString())).thenReturn(true);
        boolean estaReservadoDespuesDeReservar = repositorio.estaReservado(codigoAsiento);

		assertTrue("No se pudo reservar el asiento", !estaReservadoAntesDeReservar && estaReservadoDespuesDeReservar);
    }
}
