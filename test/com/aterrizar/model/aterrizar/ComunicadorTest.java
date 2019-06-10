package com.aterrizar.model.aterrizar;

import com.aterrizar.enumerator.Destino;
import com.aterrizar.enumerator.Ubicacion;
import com.aterrizar.exception.AsientoNoDisponibleException;
import com.aterrizar.exception.ParametroVacioException;
import com.aterrizar.model.aerolinea.AerolineaLanchita;
import com.aterrizar.model.aerolinea.AerolineaLanchitaProxy;
import com.aterrizar.model.asiento.*;
import com.aterrizar.model.aterrizar.Comunicador;
import com.aterrizar.model.usuario.Usuario;
import com.aterrizar.model.usuario.NoRegistrado;
import com.aterrizar.model.vueloasiento.VueloAsiento;
import com.aterrizar.model.vueloasiento.VueloAsientoFiltro;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import com.aterrizar.model.vueloasiento.VueloAsientoFiltroBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ComunicadorTest {
	private Comunicador comunicador;
	@Mock AerolineaLanchita mockLanchita;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		when(mockLanchita.asientosDisponibles(anyString(), anyString(), anyString(), anyString()))
				.thenReturn(Arrays.asList(
						Arrays.asList("LCH 344-42","1000.00","E","C","D")
						, Arrays.asList("LCH 344-46","400.00","T","V","D")
				));

		AerolineaLanchitaProxy aerolineaLanchitaProxy = new AerolineaLanchitaProxy(mockLanchita);
		comunicador = new Comunicador(aerolineaLanchitaProxy);
	}

	@Test
	public void filtrarAsientos_UnUsuarioBuscaAsientosYEncuentra() throws ParametroVacioException {
		Usuario usuario = new NoRegistrado("Ricardo \"EL COMANDANTE\"", "Fort", 37422007);

		VueloAsientoFiltro filtro = new VueloAsientoFiltroBuilder()
				.agregarOrigen(Destino.BUE)
				.agregarDestino(Destino.MIA)
				.agregarFecha("20190510")
				.agregarTipoAsiento(new Turista())
				.agregarUbicacion(Ubicacion.Ventanilla)
				.build();

		List<VueloAsiento> vueloAsientos = comunicador
				.filtrarAsientos(filtro, usuario)
				.getVueloAsientos();

		assertFalse(vueloAsientos.isEmpty());
	}

	@Test
	public void comprarAsiento_UnUsuarioCompraUnAsiento() throws AsientoNoDisponibleException, ParametroVacioException {
		when(mockLanchita.asientosDisponibles(anyString(), anyString(), anyString(), anyString()))
				.thenReturn(Arrays.asList(
						Arrays.asList("LCH 344-46","400.00","T","V","D")
				));

		doAnswer(invocationOnMock -> {
			when(mockLanchita.asientosDisponibles(anyString(), anyString(), anyString(), anyString()))
					.thenAnswer(i -> Arrays.asList());

			AerolineaLanchitaProxy aerolineaLanchitaProxy = new AerolineaLanchitaProxy(mockLanchita);
			this.comunicador = new Comunicador(aerolineaLanchitaProxy);
			return null;
		}).when(mockLanchita).comprar(anyString());

		Usuario usuario = new NoRegistrado("Ricardo \"EL COMANDANTE\"", "Fort", 37422007);
		VueloAsientoFiltro filtro = new VueloAsientoFiltroBuilder()
				.agregarOrigen(Destino.BUE)
				.agregarDestino(Destino.MIA)
				.agregarFecha("20190510")
				.agregarTipoAsiento(new Turista())
				.agregarUbicacion(Ubicacion.Ventanilla)
				.build();

		VueloAsiento vueloAsiento = comunicador
				.filtrarAsientos(filtro, usuario)
				.getVueloAsientos()
				.get(0);

		this.comunicador.comprar(vueloAsiento.getAsiento().getCodigoAsiento(), usuario);

		List<VueloAsiento> asientosLuegoDeComprar = comunicador
				.filtrarAsientos(filtro, usuario)
				.getVueloAsientos();

		assertFalse("El usuario no ha podido comprar el asiento.", asientosLuegoDeComprar.contains(vueloAsiento));
	}
	
}
