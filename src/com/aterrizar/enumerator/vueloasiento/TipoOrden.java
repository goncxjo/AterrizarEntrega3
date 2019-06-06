package com.aterrizar.enumerator.vueloasiento;

import com.aterrizar.model.vueloasiento.VueloAsiento;

public enum TipoOrden {
    superOferta
    , precioAscendente {

        @Override
        public int sort(VueloAsiento asientoIzq, VueloAsiento asientoDer) {
            int resultadoComparacion = super.sort(asientoIzq, asientoDer);

            if(resultadoComparacion == 0) {
                Double a = asientoIzq.getAsiento().getPrecio();
                Double b = asientoDer.getAsiento().getPrecio();

                return a.compareTo(b);
            }

            return resultadoComparacion;
        }
    }
    , precioDescendente {
        @Override
        public int sort(VueloAsiento asientoIzq, VueloAsiento asientoDer) {
            int resultadoComparacion = super.sort(asientoIzq, asientoDer);

            if(resultadoComparacion == 0) {
                Double a = asientoIzq.getAsiento().getPrecio();
                Double b = asientoDer.getAsiento().getPrecio();

                return b.compareTo(a);
            }

            return resultadoComparacion;
        }
    }
    , tiempoVuelo
    , popularidad;

    public int sort(VueloAsiento asientoIzq, VueloAsiento asientoDer) {
        if(asientoIzq.esSuperOferta()) {
            return -1;
        } else if (asientoDer.esSuperOferta()) {
            return -1;
        } else {
            return 0;
        }
    }
}
