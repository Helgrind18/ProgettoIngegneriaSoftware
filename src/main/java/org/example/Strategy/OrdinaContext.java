package org.example.Strategy;

import org.example.Biblioteca.Libro;

import java.util.List;
import java.util.ListResourceBundle;

public class OrdinaContext {
    private List<Libro> biblioteca;
    private OrdinaStrategy ordinaStrategy;

    public OrdinaContext(List<Libro> biblioteca) {
        this.biblioteca = biblioteca;
        ordinaStrategy = new OrdinaValutazione(); //Di default imposto l'ordinamento basato sulla valutazione
    }

    public void setStrategy(OrdinaStrategy ordinaStrategy){
        this.ordinaStrategy = ordinaStrategy;
    }


    public List<Libro> ordina(){
        return ordinaStrategy.ordina(biblioteca);
    }
}
