package org.example.Decorator.Ricerca;

import org.example.Biblioteca.Libro;

import java.util.List;

public abstract class RicercaComponent implements RicercaDecorator {

    /*
     * Per implementazione del pattern decorator devo avere un riferimento all'interfaccia. Delegando ad esso la chiamata di cerca()
     * */

    private RicercaDecorator ricercaDecorator;

    public RicercaComponent(RicercaDecorator ricercaDecorator) {
        this.ricercaDecorator = ricercaDecorator;
    }

    @Override
    public List<Libro> cerca() {
        return ricercaDecorator.cerca();
    }

    public RicercaDecorator getRicercaDecorator() {
        return ricercaDecorator;
    }

}
