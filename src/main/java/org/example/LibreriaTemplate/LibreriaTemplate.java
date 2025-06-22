package org.example.LibreriaTemplate;

import org.example.Biblioteca.Libro;
import org.example.Biblioteca.StatoLettura;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public abstract class LibreriaTemplate {
    protected File fileLibreria;
    private List<Libro> biblitoeca = new ArrayList<>();

    public LibreriaTemplate(File fileLibreria) {
        this.fileLibreria = fileLibreria;
    }

    public List<Libro> getBiblitoeca() {
        return biblitoeca;
    }

    //Questo metodo implementa la logica del template, ovvero permette di definire la logica, mentre le implementazioni implementeranno
    //i vari metodi
    public void esegui() {
        leggiFile();
    }

    //Metodo per leggere linea per linea dal file e popolare la struttura dati
    abstract void leggiFile();

    //Metodo per ottenere il libro dalla linea appena letta
    Libro ottieniLibro(String linea, String delimitatore) {
        StringTokenizer st = new StringTokenizer(linea, delimitatore);
        long isbn = Long.parseLong(st.nextToken());
        String titolo = st.nextToken();
        String autore = st.nextToken();
        String genere = st.nextToken();
        int valutazione = Integer.parseInt(st.nextToken());
        StatoLettura statoLettura = StatoLettura.valueOf(st.nextToken());
        Libro l = new Libro(isbn, titolo, autore, genere, valutazione, statoLettura);
        System.out.println("Libro ricostruito = " + l);
        return l;
    }

    public boolean aggiungiLibro(Libro l) {
        if (biblitoeca.contains(l)) {
            System.out.println("La struttra contiene già il libro" + l + ", non lo aggiugo");
            return false;
        }
        System.out.println("La biblioteca NON contiene il libro " + l + ", lo aggiungo");
        biblitoeca.add(l);
        return true;
    }


    //Consente di scrivere sul file
    public abstract void scriviSuFile(Libro nuovo);
}
