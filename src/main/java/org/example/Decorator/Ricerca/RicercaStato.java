package org.example.Decorator.Ricerca;

import org.example.Biblioteca.Libro;
import org.example.Biblioteca.StatoLettura;

import java.util.ArrayList;
import java.util.List;

public class RicercaStato extends RicercaComponent{
    private StatoLettura statoLettura;
    public RicercaStato(RicercaDecorator ricercaDecorator, StatoLettura statoLettura) {
        super(ricercaDecorator);
        this.statoLettura = statoLettura;
    }
    @Override
    public List<Libro> cerca() {
        System.out.println("Cerco tutti i libri di StatoLettura "+statoLettura);
        List<Libro> libriGenere = new ArrayList<>();
        for (Libro l : super.cerca()){
            if (l.getStatoLettura().equals(this.statoLettura)){
                libriGenere.add(l);
            }
        }
        return libriGenere;
    }
}
