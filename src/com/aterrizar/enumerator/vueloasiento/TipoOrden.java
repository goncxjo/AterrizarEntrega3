package com.aterrizar.enumerator.vueloasiento;

import com.aterrizar.model.vueloasiento.VueloAsiento;

public enum TipoOrden {
    superOferta
    , precioAscendente {

        @Override
        public int sort(VueloAsiento asientoIzquierda, VueloAsiento asientoDerecha) {
            int resultadoComparacion = super.sort(asientoIzquierda, asientoDerecha);

            if(resultadoComparacion == 0) {
                Double precioDeAsientoA = asientoIzquierda.getAsiento().getPrecio();
                Double precioDeAsientoB = asientoDerecha.getAsiento().getPrecio();

                return precioDeAsientoA.compareTo(precioDeAsientoB);
            }

            return resultadoComparacion;
        }
    }
    , precioDescendente {
        @Override
        public int sort(VueloAsiento asientoIzquierda, VueloAsiento asientoDerecha) {
            int resultadoComparacion = super.sort(asientoIzquierda, asientoDerecha);

            if(resultadoComparacion == 0) {
                Double precioDeAsientoA = asientoIzquierda.getAsiento().getPrecio();
                Double precioDeAsientoB = asientoDerecha.getAsiento().getPrecio();

                return precioDeAsientoB.compareTo(precioDeAsientoA);
            }

            return resultadoComparacion;
        }
    }
    , tiempoVuelo {
    	@Override
    	public int sort(VueloAsiento asientoIzquierda, VueloAsiento asientoDerecha) {
    		int resultadoComparacion = super.sort(asientoIzquierda, asientoDerecha);
    		
    		 if(resultadoComparacion == 0) {
    			 Double tiempoVueloAsientoA = asientoIzquierda.getVuelo().getTiempoVuelo();
                 Double tiempoVueloAsientoB = asientoDerecha.getVuelo().getTiempoVuelo();

                 return tiempoVueloAsientoA.compareTo(tiempoVueloAsientoB);
             }
    		 
    		 return resultadoComparacion;
    	}
    }
    , popularidad {
    	
    };

    public int sort(VueloAsiento asientoIzq, VueloAsiento asientoDer) {
        if(asientoIzq.getAsiento().esSuperOferta()) {
            return -1;
        } else if (asientoDer.getAsiento().esSuperOferta()) {
            return -1;
        } else {
            return 0;
        }
    }
}
