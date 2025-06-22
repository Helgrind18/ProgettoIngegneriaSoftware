package org.example.LibreriaTemplate;

import org.example.Biblioteca.Libro;

import java.io.*;

public class LibreriaCSV extends LibreriaTemplate {
    public LibreriaCSV(File fileLibreria) {
        super(fileLibreria);
    }

    @Override
    void leggiFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileLibreria));
            while (true){
                String linea = br.readLine();
                if (linea == null){
                    System.out.println("EOF!");
                    br.close();
                    return;
                }
                System.out.println("Linea letta = "+linea);
                Libro l = super.ottieniLibro(linea,",");
                if (super.aggiungiLibro(l)){
                    System.out.println("Libro "+l +" aggiunto correttametne");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void scriviSuFile(Libro nuovo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileLibreria,true),true)) {
            pw.println(nuovo.getISBN() + "," +
                    nuovo.getTitolo() + "," +
                    nuovo.getAutore() + "," +
                    nuovo.getGenere() + "," +
                    nuovo.getValutazione() + "," +
                    nuovo.getStatoLettura());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void sovrascriviFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileLibreria, false))) {
            for (Libro l : getBiblitoeca()) {
                pw.println(
                        l.getISBN() + "," +
                                l.getTitolo() + "," +
                                l.getAutore() + "," +
                                l.getGenere() + "," +
                                l.getValutazione() + "," +
                                l.getStatoLettura()
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
