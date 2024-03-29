package com.aterrizar.model.aterrizar;

import com.aterrizar.exception.AsientoLanchitaYaReservadoException;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.aterrizar.enumerator.Destino;
import com.aterrizar.enumerator.Ubicacion;
import com.aterrizar.enumerator.asiento.Estado;
import com.aterrizar.exception.AsientoNoDisponibleException;
import com.aterrizar.exception.AsientoYaReservadoException;
import com.aterrizar.model.Vuelo;
import com.aterrizar.model.aerolinea.AerolineaLanchita;
import com.aterrizar.model.aerolinea.AerolineaLanchitaProxy;
import com.aterrizar.model.asiento.*;
import com.aterrizar.model.usuario.*;
import com.aterrizar.model.vueloasiento.Reserva;
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
	public void reservar_ReservaUnAsientoDisponible() throws AsientoNoDisponibleException, AsientoLanchitaYaReservadoException {
		Usuario usuario = new Estandar("Ricardo \"EL COMANDANTE\"", "Fort", 37422007);

		VueloAsiento vueloAsiento = new VueloAsiento(
				"Lanchita"
				, "LCH"
				, new Vuelo(Destino.EZE, Destino.MIA, DateHelper.parseToDate("13/05/2019"), 10.0, 5.0)
				, new Ejecutivo("LCH 005-40", 50000, Ubicacion.Centro, Estado.Disponible)
				);

		when(mockLanchita.estaReservado(anyString())).thenReturn(false);
		boolean estaReservadoAntesDeReservar = repositorio.estaReservado(vueloAsiento.getAsiento().getCodigoAsiento());

		doNothing().when(mockLanchita).reservar(anyObject(), anyString());
		repositorio.reservar(vueloAsiento, usuario);

		when(mockLanchita.estaReservado(anyString())).thenReturn(true);
        boolean estaReservadoDespuesDeReservar = repositorio.estaReservado(vueloAsiento.getAsiento().getCodigoAsiento());
        
		assertTrue("No se pudo reservar el asiento", !estaReservadoAntesDeReservar && estaReservadoDespuesDeReservar);
	}
	
	
	@Test
	public void reservar_ReservaUnAsientoNoDisponible() throws AsientoNoDisponibleException, AsientoLanchitaYaReservadoException {
		Usuario usuario = new Estandar("Ricardo \"EL COMANDANTE\"", "Fort", 37422007);

		VueloAsiento vueloAsiento = new VueloAsiento(
				"Lanchita"
				, "LCH"
				, new Vuelo(Destino.EZE, Destino.MIA, DateHelper.parseToDate("13/05/2019"), 10.0, 5.0)
				, new Ejecutivo("LCH 005-40", 50000, Ubicacion.Centro, Estado.Reservado)
				);
		
		Mockito.doThrow(AsientoYaReservadoException.class).when(mockLanchita).reservar(anyString(),anyString());
		repositorio.reservar(vueloAsiento, usuario);
		
		assertTrue("No se pudo sobre reservar el asiento", !repositorio.getListaEspera(vueloAsiento.getAsiento().getCodigoAsiento()).isEmpty());
	}
	
	
	@Test
	public void transferir_CaeLaReservaDeUnAsientoYEsTransferidaAlPrimeroDeLaListaDeEspera() throws AsientoLanchitaYaReservadoException, AsientoNoDisponibleException {
		Usuario usuario = new Estandar("Ricardo \"EL COMANDANTE\"", "Fort", 37422007);
		Usuario usuario2 = new Estandar("Jessica", "Jones", 30303456);
		
		VueloAsiento vueloAsiento = new VueloAsiento(
				"Lanchita"
				, "LCH"
				, new Vuelo(Destino.EZE, Destino.MIA, DateHelper.parseToDate("13/05/2019"), 10.0, 5.0)
				, new Ejecutivo("LCH 005-40", 50000, Ubicacion.Centro, Estado.Disponible)
				);
		
		doNothing().when(mockLanchita).reservar(anyObject(), anyString());
		repositorio.reservar(vueloAsiento, usuario);
		Reserva asientoReservado = new Reserva(vueloAsiento.getAsiento().getCodigoAsiento(), usuario);
		
		Mockito.doThrow(AsientoYaReservadoException.class).when(mockLanchita).reservar(anyString(),anyString());
		repositorio.reservar(vueloAsiento, usuario2);
		boolean listaDeEsperaAntesDeTransferir = repositorio.getListaEspera(vueloAsiento.getAsiento().getCodigoAsiento()).isEmpty();
		
		repositorio.transferir(asientoReservado);
		boolean listaDeEsperaDespuesDeTransferir = repositorio.getListaEspera(vueloAsiento.getAsiento().getCodigoAsiento()).isEmpty();	
		
		assertTrue("No se pudo transferir la reserva", !listaDeEsperaAntesDeTransferir && listaDeEsperaDespuesDeTransferir);
	}
	
	
	@Test
	public void transferir_CaeLaReservaDeUnAsientoYLaListaDeEsperaEstaVacia() throws AsientoLanchitaYaReservadoException, AsientoNoDisponibleException {
		Usuario usuario = new Estandar("Ricardo \"EL COMANDANTE\"", "Fort", 37422007);
		
		VueloAsiento vueloAsiento = new VueloAsiento(
				"Lanchita"
				, "LCH"
				, new Vuelo(Destino.EZE, Destino.MIA, DateHelper.parseToDate("13/05/2019"), 10.0, 5.0)
				, new Ejecutivo("LCH 005-40", 50000, Ubicacion.Centro, Estado.Disponible)
				);
		
		doNothing().when(mockLanchita).reservar(anyObject(), anyString());
		repositorio.reservar(vueloAsiento, usuario);
		Reserva asientoReservado = new Reserva(vueloAsiento.getAsiento().getCodigoAsiento(), usuario);
		when(mockLanchita.estaReservado(anyString())).thenReturn(true);
		boolean estaReservadoAntesDeTransferir = repositorio.estaReservado(vueloAsiento.getAsiento().getCodigoAsiento());
		
		repositorio.transferir(asientoReservado);
		when(mockLanchita.estaReservado(anyString())).thenReturn(false);
		boolean estaReservadoDespuesDeTransferir = repositorio.estaReservado(vueloAsiento.getAsiento().getCodigoAsiento());
		
		assertTrue("La reserva fue transferida correctamente", estaReservadoAntesDeTransferir && !estaReservadoDespuesDeTransferir);
	}
}