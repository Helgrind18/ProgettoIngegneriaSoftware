package org.example.Decorator;

import org.example.Biblioteca.Libro;
import org.example.LibreriaTemplate.LibreriaTemplate;

import java.util.ArrayList;
import java.util.List;

public class RicercaGenere extends RicercaComponent {

    private String genere;

    public RicercaGenere(RicercaDecorator ricercaDecorator, String genere) {
        super(ricercaDecorator);
        this.genere = genere;
    }

    @Override
    public List<Libro> cerca() {
        System.out.println("Cerco tutti i libri di GENERE "+genere);
        List<Libro> libriGenere = new ArrayList<>();
        for (Libro l : super.cerca()){
            if (l.getGenere().equalsIgnoreCase(this.genere)){
                libriGenere.add(l);
            }
        }
        return libriGenere;
    }
}
