package com.aterrizar.enumerator;

public enum Ubicacion {
    Centro("C"), Pasillo("P"), Ventanilla("V");

    private String codigoLanchita;

    Ubicacion(String codigoLanchita) {
        this.codigoLanchita = codigoLanchita;
    }

    public static Ubicacion getUbicacionPorCodigoLanchita(String inicial) {
        switch (inicial) {
            case "C":
                return Ubicacion.Centro;
            case "P":
                return Ubicacion.Pasillo;
            case "V":
                return Ubicacion.Ventanilla;
            default:
                return Ubicacion.valueOf(inicial);
        }
    }
}
