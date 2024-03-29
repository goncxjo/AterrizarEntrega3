package com.aterrizar.enumerator;

public enum Destino {
    /*BUE("Buenos Aires")
    , EZE("Ezeiza")
    , LA("Los Angeles")
    , BAR("Barcelona")
    , MAD("Madrid")
    , TOK("Tokio")
    , BRA("Brasilia")
    , MIA("Miami");*/

    BUE("Buenos Aires", "Argentina")
    , EZE("Ezeiza", "Argentina")
    , LA("Los Angeles", "EE.UU.")
    , BAR("Barcelona", "España")
    , MAD("Madrid", "España")
    , TOK("Tokio", "Japón")
    , BSB("Brasilia", "Brasil")
    , MIA("Miami", "EE.UU.")
    , MEX("Ciudad de México", "México");

    String nombre;
    String pais;

    Destino(String nombre, String pais) {
        this.nombre = nombre;
        this.pais = pais;
    }
}
