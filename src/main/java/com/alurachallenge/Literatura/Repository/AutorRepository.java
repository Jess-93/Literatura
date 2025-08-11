package com.alurachallenge.Literatura.Repository;

import com.alurachallenge.Literatura.Model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombre(String nombre);

    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :anio AND a.fechaDeFallecimiento > :anio")
    List<Autor> buscarAutoresVivosPorAnio(Integer anio);

    @Query("SELECT a.nombre, COUNT(l.id) FROM Autor a JOIN a.libros l GROUP BY a.nombre")
    List<Object[]> contarLibrosPorAutor();
}