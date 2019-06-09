package com.aterrizar.model.asiento;

public class AsientoSobreReservado {
    private String codigoAsiento;
    private String DNI;

    public AsientoSobreReservado(String codigoAsiento, String DNI) {
        this.codigoAsiento = codigoAsiento;
        this.DNI = DNI;
    }

    public String getCodigoAsiento() {
        return codigoAsiento;
    }

    public void setCodigoAsiento(String codigoAsiento) {
        this.codigoAsiento = codigoAsiento;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String dNI) {
        DNI = dNI;
    }
}
