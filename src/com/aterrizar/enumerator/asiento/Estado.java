package com.aterrizar.enumerator.asiento;

public enum Estado {
    Disponible("D"), Reservado("R");

    private String codigoLanchita;

    Estado(String codigoLanchita) {
        this.codigoLanchita = codigoLanchita;
    }

    public static Estado getEstadoPorCodigoLanchita(String inicial) {
        switch (inicial) {
            case "D":
                return Disponible;
            case "R":
                return Reservado;
            default:
                return Estado.valueOf(inicial);
        }
    }
}
