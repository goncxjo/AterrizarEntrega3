package com.aterrizar.model.aterrizar;

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

import org.junit.Before;

public class RepositorioTest {
	private Repositorio repositorio;
	@Mock AerolineaLanchita mockLanchita;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		AerolineaLanchitaProxy aerolineaLanchitaProxy = new AerolineaLanchitaProxy(mockLanchita);
		Comunicador comunicador = new Comunicador(aerolineaLanchitaProxy);
		repositorio = new Repositorio(comunicador);
	}
	
	@Test
	public void reservar_AsientoDisponible_ReservaUnAsientoDisponible() throws AsientoNoDisponibleException {
		Usuario usuario = new Estandar("Ricardo \"EL COMANDANTE\"", "Fort)", 37422007);
		VueloAsiento vueloAsiento = new VueloAsiento(
				"Lanchita"
				, "LCH"
				, new Vuelo(Destino.BUE, Destino.MIA, DateHelper.parseToDate("13/05/2019"))
				, new Ejecutivo("LCH 005-40", 50000, Ubicacion.Centro, Estado.Disponible)
				);
		
		repositorio.reservar(vueloAsiento, usuario);
		
		assertTrue("No se pudo reservar el asiento", repositorio.getListaEspera(vueloAsiento.getAsiento().getCodigoAsiento()).isEmpty());
		}
}
