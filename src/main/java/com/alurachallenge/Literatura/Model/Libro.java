package com.alurachallenge.Literatura.Model;

import com.alurachallenge.Literatura.dto.DatosLibro;
import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String idioma;
    private Double numeroDeDescargas;
    @ManyToOne
    private Autor autor;

    public Libro() {}

    public Libro(DatosLibro datosLibro){
        this.titulo = datosLibro.titulo();
        this.idioma = datosLibro.idiomas().get(0);
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();
    }

    // Getters y Setters...
    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "---------------------------------" +
                "\nTitulo: " + titulo +
                "\nAutor: " + autor.getNombre() +
                "\nIdioma: " + idioma +
                "\nNúmero de descargas: " + numeroDeDescargas +
                "\n---------------------------------";
    }

    public String getTitulo() {
        return titulo;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }
}