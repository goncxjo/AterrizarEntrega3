package com.aterrizar.enumerator;

public enum Destino {
    BUE("Buenos Aires")
    , LA("Los Angeles")
    , BAR("Barcelona")
    , MAD("Madrid")
    , TOK("Tokio")
    , BRA("Brasilia")
    , MIA("Miami")
    , SLA("Los Angeles")
    , MEX("M�xico");

    String nombre;

    Destino(String nombre) {
        this.nombre = nombre;
    }
}
