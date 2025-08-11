package com.alurachallenge.Literatura.Repository;

import com.alurachallenge.Literatura.Model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTitulo(String titulo);
    List<Libro> findByIdioma(String idioma);

    @Query("SELECT l FROM Libro l JOIN l.autor a WHERE a.nombre LIKE %:nombre%")
    List<Libro> buscarLibrosPorAutor(String nombre);

    @Query("SELECT l.idioma, COUNT(l) AS total FROM Libro l GROUP BY l.idioma ORDER BY total DESC")
    List<Object[]> contarLibrosPorIdioma();

    @Query("SELECT l FROM Libro l ORDER BY l.numeroDeDescargas DESC LIMIT 10")
    List<Libro> findTop10ByNumeroDeDescargas();

}