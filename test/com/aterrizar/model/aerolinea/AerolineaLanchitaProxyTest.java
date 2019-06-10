package com.aterrizar.model.aerolinea;

import com.aterrizar.enumerator.Destino;
import com.aterrizar.enumerator.Ubicacion;
import com.aterrizar.exception.AsientoLanchitaNoDisponibleException;
import com.aterrizar.exception.AsientoNoDisponibleException;
import com.aterrizar.exception.ParametroVacioException;
import com.aterrizar.model.asiento.Asiento;
import com.aterrizar.model.asiento.Turista;
import com.aterrizar.model.usuario.Estandar;
import com.aterrizar.model.usuario.NoRegistrado;
import com.aterrizar.model.usuario.Usuario;
import com.aterrizar.model.vueloasiento.VueloAsiento;
import com.aterrizar.model.vueloasiento.VueloAsientoFiltro;
import com.aterrizar.model.vueloasiento.VueloAsientoFiltroBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AerolineaLanchitaProxyTest {

    private AerolineaLanchitaProxy aerolineaLanchitaProxy;

    @Mock AerolineaLanchita mockLanchita;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void buscarVuelos_UsuarioEstandar_BuenosAiresBarcelona_TieneVuelosDisponibles() throws ParametroVacioException {
        when(mockLanchita.asientosDisponibles(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Arrays.asList(
                        Arrays.asList("LCH 344-42","1000.00","E","C","D")
                        , Arrays.asList("LCH 344-46","400.00","T","V","D")
                ));

        aerolineaLanchitaProxy = new AerolineaLanchitaProxy(mockLanchita);

        VueloAsientoFiltro filtro = new VueloAsientoFiltroBuilder()
                .agregarOrigen(Destino.BUE)
                .agregarDestino(Destino.BAR)
                .agregarFecha("20190510")
                .build();

        Usuario usuario = new NoRegistrado("Ricardo \"EL COMANDANTE\"", "Fort)", 37422007);

        List<VueloAsiento> vueloAsientos = aerolineaLanchitaProxy
                .filtrarAsientos(filtro, usuario)
                .getVueloAsientos();

        assertFalse(vueloAsientos.isEmpty());
    }

    @Test
    public void buscarVuelos_UsuarioEstandar_BuenosAiesTokio_NoTieneVuelosDisponibles() throws ParametroVacioException {
        when(mockLanchita.asientosDisponibles(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Arrays.asList());

        aerolineaLanchitaProxy = new AerolineaLanchitaProxy(mockLanchita);

        VueloAsientoFiltro filtro = new VueloAsientoFiltroBuilder()
                .agregarOrigen(Destino.BUE)
                .agregarDestino(Destino.TOK)
                .agregarFecha("20190510")
                .agregarTipoAsiento(new Turista())
                .agregarUbicacion(Ubicacion.Ventanilla)
                .build();

        Usuario usuario = new NoRegistrado("Ricardo \"EL COMANDANTE\"", "Fort)", 37422007);

        List<VueloAsiento> vueloAsientos = aerolineaLanchitaProxy
                .filtrarAsientos(filtro, usuario)
                .getVueloAsientos();

        assertTrue(vueloAsientos.isEmpty());
    }

    @Test
    public void buscarVuelos_UsuarioNoRegistrado_AsientoQueVale100_TieneRecargo() throws ParametroVacioException {
        when(mockLanchita.asientosDisponibles(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Arrays.asList(
                        Arrays.asList("LCH 622-12","115.00","T","V","D")
                ));

        aerolineaLanchitaProxy = new AerolineaLanchitaProxy(mockLanchita);

        VueloAsientoFiltro filtro = new VueloAsientoFiltroBuilder()
                .agregarOrigen(Destino.BUE)
                .agregarDestino(Destino.MIA)
                .agregarFecha("20190510")
                .agregarTipoAsiento(new Turista())
                .agregarUbicacion(Ubicacion.Ventanilla)
                .build();

        Usuario usuario = new NoRegistrado("Ricardo \"EL COMANDANTE\"", "Fort)", 37422007);

        List<VueloAsiento> vueloAsientos = aerolineaLanchitaProxy
                .filtrarAsientos(filtro, usuario)
                .getVueloAsientos();
        Double precioTotal = (double) Math.round(vueloAsientos.get(0).getAsiento().getPrecio());

        // precio asiento = 100
        // impuestos asiento = 15
        // recargo = 20
        // TOTAL = 135
        System.out.println(precioTotal);
        assertEquals("El asiento no tiene recargo", 135.00, precioTotal, 0.0);
    }

    @Test
    public void comprar_UsuarioEstandar_ReservaUnAsientoDisponible() throws AsientoNoDisponibleException, ParametroVacioException {
        String codigoAsiento = "LCH 622-12";

        when(mockLanchita.asientosDisponibles(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Arrays.asList(
                        Arrays.asList(codigoAsiento,"1000.00","T","V","D")
                ));

        doAnswer(invocationOnMock -> {
            when(mockLanchita.asientosDisponibles(anyString(), anyString(), anyString(), anyString()))
                    .thenAnswer(i -> Arrays.asList());
            this.aerolineaLanchitaProxy = new AerolineaLanchitaProxy(mockLanchita);
            return null;
        }).when(mockLanchita).comprar(codigoAsiento);

        aerolineaLanchitaProxy = new AerolineaLanchitaProxy(mockLanchita);

        VueloAsientoFiltro filtro = new VueloAsientoFiltroBuilder()
                .agregarOrigen(Destino.BUE)
                .agregarDestino(Destino.MIA)
                .agregarFecha("20190510")
                .agregarTipoAsiento(new Turista())
                .agregarUbicacion(Ubicacion.Ventanilla)
                .build();

        Usuario usuario = new Estandar("Ricardo \"EL COMANDANTE\"", "Fort)", 37422007);

        List<VueloAsiento> vueloAsientosAntesDeComprar = aerolineaLanchitaProxy
                .filtrarAsientos(filtro, usuario)
                .getVueloAsientos();
        
        this.aerolineaLanchitaProxy.comprar(codigoAsiento, usuario);

        List<VueloAsiento> vueloAsientosDespuesDeComprar = aerolineaLanchitaProxy
                .filtrarAsientos(filtro, usuario)
                .getVueloAsientos();

        assertTrue("El asiento está disponible", !vueloAsientosAntesDeComprar.isEmpty() && vueloAsientosDespuesDeComprar.isEmpty());
    }

    @Test
    public void comprar_UsuarioEstandar_ReservaUnAsientoDisponibleYSeEliminaDelVuelo() throws AsientoNoDisponibleException, ParametroVacioException {
        String codigoAsiento = "LCH 622-12";

        when(mockLanchita.asientosDisponibles(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Arrays.asList(
                        Arrays.asList(codigoAsiento,"1000.00","T","V","D")
                ));

        doAnswer(invocationOnMock -> {
            when(mockLanchita.asientosDisponibles(anyString(), anyString(), anyString(), anyString()))
                    .thenAnswer(i -> Arrays.asList());
            this.aerolineaLanchitaProxy = new AerolineaLanchitaProxy(mockLanchita);
            return null;
        }).when(mockLanchita).comprar(codigoAsiento);

        aerolineaLanchitaProxy = new AerolineaLanchitaProxy(mockLanchita);

        VueloAsientoFiltro filtro = new VueloAsientoFiltroBuilder()
                .agregarOrigen(Destino.BUE)
                .agregarDestino(Destino.MIA)
                .agregarFecha("20190510")
                .agregarTipoAsiento(new Turista())
                .agregarUbicacion(Ubicacion.Ventanilla)
                .build();

        Usuario usuario = new Estandar("Ricardo \"EL COMANDANTE\"", "Fort)", 37422007);

        List<VueloAsiento> vueloAsientosAntesDeComprar = aerolineaLanchitaProxy
                .filtrarAsientos(filtro, usuario)
                .getVueloAsientos();

        this.aerolineaLanchitaProxy.comprar(codigoAsiento, usuario);

        List<VueloAsiento> vueloAsientosDespuesDeComprar = aerolineaLanchitaProxy
                .filtrarAsientos(filtro, usuario)
                .getVueloAsientos();

        assertTrue("El asiento aún existe", !vueloAsientosAntesDeComprar.isEmpty() && vueloAsientosDespuesDeComprar.isEmpty());
    }

    @Test(expected = AsientoNoDisponibleException.class)
    public void comprar_UsuarioEstandar_NoPuedeComprarUnAsientoReservado() throws AsientoNoDisponibleException {
        doThrow(new AsientoLanchitaNoDisponibleException("El asiento ya se encuentra reservado"))
                .when(mockLanchita)
                .comprar(anyString());

        aerolineaLanchitaProxy = new AerolineaLanchitaProxy(mockLanchita);
        Usuario usuario = new Estandar("Ricardo \"EL COMANDANTE\"", "Fort)", 37422007);

        this.aerolineaLanchitaProxy.comprar("LCH 622-12", usuario);
    }
}
