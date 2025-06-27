package org.example.Biblioteca;

import java.io.Serializable;
import java.util.Objects;

public class Libro implements Serializable {
    private long ISBN;
    private String  titolo, autore, genere;
    private int valutazione;
    private StatoLettura statoLettura;

    public Libro(long ISBN, String titolo, String autore, String genere, int valutazione, StatoLettura statoLettura) {
        if (!validazioneCampi(ISBN,titolo,autore,genere,valutazione)){
            throw new IllegalArgumentException();
        }
        this.ISBN = ISBN;
        this.titolo = titolo;
        this.autore = autore;
        this.genere = genere;
        this.valutazione = valutazione;
        this.statoLettura = statoLettura;
    }

    private boolean validazioneCampi(long isbn, String titolo, String autore, String genere, int valutazione) {
        return isbn >= 0 && titolo != null && !titolo.isEmpty() && autore != null && !autore.isEmpty() && genere != null && !genere.isEmpty() && valutazione >= 0 && valutazione <= 5;
    }

    public long getISBN() {
        return ISBN;
    }

    public void setISBN(long ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public int getValutazione() {
        return valutazione;
    }

    public void setValutazione(int valutazione) {
        this.valutazione = valutazione;
    }

    public StatoLettura getStatoLettura() {
        return statoLettura;
    }

    public void setStatoLettura(StatoLettura statoLettura) {
        this.statoLettura = statoLettura;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "ISBN='" + ISBN + '\'' +
                ", titolo='" + titolo + '\'' +
                ", autore='" + autore + '\'' +
                ", genere='" + genere + '\'' +
                ", valutazione=" + valutazione +
                ", statoLettura=" + statoLettura +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Libro libro)) return false;
        return getISBN() == libro.getISBN() && Objects.equals(getTitolo(), libro.getTitolo()) && Objects.equals(getAutore(), libro.getAutore()) && Objects.equals(getGenere(), libro.getGenere());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitolo(), getAutore(), getGenere(), getISBN());
    }
}
