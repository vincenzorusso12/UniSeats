package com.uniseats.prenotazioni;

import java.util.List;

public class AulaBean {

    private String codice;
    private String dipartimento;
    private int nPosti;
    private String edificio;

    public AulaBean() {}

    public AulaBean(String codice, String dipartimento, int nPosti, String edificio) {
        this.codice = codice;
        this.dipartimento = dipartimento;
        this.nPosti = nPosti;
        this.edificio = edificio;
    }

    public String getCodice() {
        return codice;
    }

    public String getDipartimento() {
        return dipartimento;
    }

    public int getnPosti() {
        return nPosti;
    }

    public String getEdificio() {
        return edificio;
    }

    public List<PostoBean> getPosti(){
        /*TODO: WRITE getPosti() method*/
        return null;
    }

}
