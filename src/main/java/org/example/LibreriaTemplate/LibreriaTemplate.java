package org.example.LibreriaTemplate;

import org.example.Biblioteca.Libro;
import org.example.Biblioteca.StatoLettura;

import java.io.File;
import java.util.*;

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

    //Consente di modificare il libro. Nota bene: non posso modificare un file, non posso inserire in mezzo un libro modificato
    abstract void sovrascriviFile();

    public boolean modificaLibro(Libro vecchio, Libro nuovo) {
        System.out.println("Devo modificare il libro " + vecchio + " con il seguente libro " + nuovo);
        if (nuovo == null) {
            return false;
        }
        if (!biblitoeca.contains(vecchio)) {
            System.out.println(vecchio + " inesistente!");
            return false;
        }
        ListIterator<Libro> libroListIterator = biblitoeca.listIterator();
        while (libroListIterator.hasNext()){
            Libro l = libroListIterator.next();
            if (l.getISBN() == vecchio.getISBN()){
                System.out.println("Ho trovato il libro!");
                libroListIterator.set(nuovo);
                sovrascriviFile();
                return true;
            }
        }
        System.out.println("Libro non trovato");
        return false;
    }


}
