package com.example.practicaevaluadacorte2.dao;

public class DatosException extends  Exception{

    private final String sqlState;


    public DatosException(String mensaje, String sqlState, Throwable causa) {
        super(mensaje, causa);
        this.sqlState = sqlState;
    }

    public String getSqlState() {
        return sqlState;
    }
}
