package com.alurachallenge.Literatura;

import com.alurachallenge.Literatura.dto.Datos;
import com.alurachallenge.Literatura.dto.DatosAutor;
import com.alurachallenge.Literatura.dto.DatosLibro;
import com.alurachallenge.Literatura.Model.Autor;
import com.alurachallenge.Literatura.Model.Libro;
import com.alurachallenge.Literatura.Repository.AutorRepository;
import com.alurachallenge.Literatura.Repository.LibroRepository;
import com.alurachallenge.Literatura.service.ConsumoAPI;
import com.alurachallenge.Literatura.service.ConvierteDatos;
import org.springframework.stereotype.Component;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Clase principal que gestiona el menú y la lógica de la aplicación de consola.
 * Interactúa con la API de Gutendex y la base de datos a través de los repositorios.
 */

@Component
public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    /**
     * Muestra el menú de opciones y maneja la interacción con el usuario.
     */

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ----------------------------------------
                    Bienvenido a la aplicación de Literatura
                    ----------------------------------------
                    Elige una opción a través de su número:
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un año determinado
                    5 - Listar libros por idioma
                    6 - Buscar libros por autor
                    7 - Contar libros por idioma
                    8 - Top 10 libros más descargados
                    9 - Buscar libros de autor en la API
                    10 - Contar libros por autor
                    0 - Salir
                    ----------------------------------------
                    """;
            System.out.println(menu);
            System.out.print("Ingresa por favor tu opción: ");

            try {
                // Lee la opción del usuario
                opcion = teclado.nextInt();
                teclado.nextLine(); // Esta línea es crucial para limpiar el buffer.

                switch (opcion) {
                    case 1: buscarLibroPorTitulo(); break;
                    case 2: listarLibrosRegistrados(); break;
                    case 3: listarAutoresRegistrados(); break;
                    case 4: listarAutoresVivosPorAnio(); break;
                    case 5: listarLibrosPorIdioma(); break;
                    case 6: buscarLibrosPorAutor(); break;
                    case 7: contarLibrosPorIdioma(); break;
                    case 8: listarTop10Libros(); break;
                    case 9: buscarLibrosDeAutorEnApi(); break;
                    case 10: contarLibrosPorAutor(); break;
                    case 0: System.out.println("Cerrando la aplicación..."); break;
                    default: System.out.println("Opción inválida. Por favor, elige una opción del 0 al 7.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Por favor, ingresa un número.");
                teclado.nextLine(); // Limpiar el buffer para evitar un bucle infinito
            }
        }
    }

    /**
     * Busca un libro en la API por título y lo registra en la base de datos si no existe.
     */

    private void buscarLibroPorTitulo() {
        System.out.println("Escribe el nombre del libro que deseas buscar:");
        var nombreLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        var datos = conversor.obtenerDatos(json, Datos.class);

        // Filtra y busca el primer libro que coincida con el título
        Optional<DatosLibro> libroBuscado = datos.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            // Verifica si el libro ya existe en el repositorio
            DatosLibro datosLibro = libroBuscado.get();
            Optional<Libro> libroExistente = libroRepository.findByTitulo(datosLibro.titulo());
            if (libroExistente.isPresent()) {
                System.out.println("\n----------------------------------------");
                System.out.println("⚠️ Este libro ya está registrado en la base de datos.");
                System.out.println("----------------------------------------");
            } else {
                // Crea un nuevo libro y maneja la creación o asociación del autor
                Libro nuevoLibro = new Libro(datosLibro);
                DatosAutor datosAutor = datosLibro.autores().get(0);
                Optional<Autor> autorExistente = autorRepository.findByNombre(datosAutor.nombre());
                if (autorExistente.isPresent()) {
                    nuevoLibro.setAutor(autorExistente.get());
                } else {
                    Autor nuevoAutor = new Autor(datosAutor);
                    autorRepository.save(nuevoAutor);
                    nuevoLibro.setAutor(nuevoAutor);
                }
                libroRepository.save(nuevoLibro);
                System.out.println("\n----------------------------------------");
                System.out.println("✅ Libro registrado exitosamente: ");
                System.out.println(nuevoLibro);
                System.out.println("----------------------------------------");
            }
        } else {
            System.out.println("\n----------------------------------------");
            System.out.println("❌ Libro no encontrado en la API.");
            System.out.println("----------------------------------------");
        }
    }

    /**
     * Lista todos los libros registrados en la base de datos.
     */

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("\nNo hay libros registrados en la base de datos.");
        } else {
            System.out.println("\n----------------------------------------");
            System.out.println("  Libros Registrados");
            System.out.println("----------------------------------------");
            libros.forEach(System.out::println);
            System.out.println("----------------------------------------");
        }
    }

    /**
     * Lista todos los autores registrados en la base de datos.
     */

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if(autores.isEmpty()){
            System.out.println("\nNo hay autores registrados en la base de datos.");
        } else {
            System.out.println("\n----------------------------------------");
            System.out.println("  Autores Registrados");
            System.out.println("----------------------------------------");
            autores.forEach(System.out::println);
            System.out.println("----------------------------------------");
        }
    }

    /**
     * Pide un año al usuario y lista los autores que estaban vivos en ese año.
     */

    private void listarAutoresVivosPorAnio() {
        try {
            System.out.println("Escribe el año para verificar autores vivos:");
            var anio = teclado.nextInt();
            teclado.nextLine();
            List<Autor> autoresVivos = autorRepository.buscarAutoresVivosPorAnio(anio);
            if (autoresVivos.isEmpty()) {
                System.out.println("No hay autores vivos para el año " + anio + ", lo sentimos.");
            } else {
                autoresVivos.forEach(System.out::println);
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada no válida. Por favor, ingresa un número de año.");
            teclado.nextLine();
        }
    }

    /**
     * Pide un idioma al usuario y lista los libros registrados en ese idioma.
     */

    private void listarLibrosPorIdioma() {
        System.out.println("Escribe el idioma para buscar los libros:");
        System.out.println("es -> Español");
        System.out.println("en -> Inglés");
        System.out.println("fr -> Francés");
        System.out.println("pt -> Portugués");
        System.out.print("Opción: ");
        var idioma = teclado.nextLine().toLowerCase();
        if (idioma.equals("es") || idioma.equals("en") || idioma.equals("fr") || idioma.equals("pt")) {
            List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);
            if (librosPorIdioma.isEmpty()) {
                System.out.println("No hay libros registrados en ese idioma.");
            } else {
                librosPorIdioma.forEach(System.out::println);
            }
        } else {
            System.out.println("Opción de idioma no válida. Por favor, elige entre las opciones disponibles.");
        }
    }

    /**
     * Pide un nombre de autor y busca los libros de ese autor en la base de datos.
     */

    private void buscarLibrosPorAutor() {
        System.out.println("\n----- Búsqueda de autores -----");
        System.out.println("Usa ese formato para tu búsqueda: «Apellido, Nombre». Ejemplo: Austen, Jane");
        System.out.print("Escribe por favor el nombre del autor buscar: ");
        var nombreAutor = teclado.nextLine();
        List<Libro> libros = libroRepository.buscarLibrosPorAutor(nombreAutor);
        if (libros.isEmpty()) {
            System.out.println("\n----------------------------------------");
            System.out.println("⚠️ No se encontraron libros para ese autor.");
            System.out.println("----------------------------------------");
        } else {
            System.out.println("\n----------------------------------------");
            System.out.println("  Libros del autor: " + nombreAutor);
            System.out.println("----------------------------------------");
            libros.forEach(System.out::println);
            System.out.println("----------------------------------------");
        }
    }

    /**
     * Cuenta y muestra el número de libros por cada idioma registrado.
     */

    private void contarLibrosPorIdioma() {
        List<Object[]> conteo = libroRepository.contarLibrosPorIdioma();
        if (conteo.isEmpty()) {
            System.out.println("\n----------------------------------------");
            System.out.println("⚠️ No hay libros registrados en la base de datos.");
            System.out.println("----------------------------------------");
        } else {
            System.out.println("\n----------------------------------------");
            System.out.println("  Conteo de Libros por Idioma");
            System.out.println("----------------------------------------");
            conteo.forEach(resultado -> System.out.println("Idioma: " + resultado[0] + " - Total: " + resultado[1]));
            System.out.println("----------------------------------------");
        }
    }

    /**
     * Muestra los 10 libros más descargados actualmente de tu base de datos
     */

    private void listarTop10Libros() {
        System.out.println("\n----------------------------------------");
        System.out.println("  Top 10 Libros más Descargados");
        System.out.println("----------------------------------------");

        // Obtiene la lista y la almacena en una variable
        List<Libro> top10Libros = libroRepository.findTop10ByNumeroDeDescargas();

        // Usa un bucle for para enumerar cada libro
        for (int i = 0; i < top10Libros.size(); i++) {
            Libro libro = top10Libros.get(i);
            System.out.println((i + 1) + ". Título: " + libro.getTitulo() + " | Descargas: " + libro.getNumeroDeDescargas());
        }

        System.out.println("----------------------------------------");
    }

    /**
     * Busca libros por nombre de autor.
     */


    private void buscarLibrosDeAutorEnApi() {
        System.out.println("Escribe el nombre del autor para buscar todos sus libros en la API:");
        var nombreAutor = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreAutor.replace(" ", "+"));
        var datos = conversor.obtenerDatos(json, Datos.class);

        List<DatosLibro> librosEncontrados = datos.resultados();

        if (librosEncontrados.isEmpty()) {
            System.out.println("\n----------------------------------------");
            System.out.println("❌ No se encontraron libros para el autor: " + nombreAutor);
            System.out.println("----------------------------------------");
        } else {
            System.out.println("\n----------------------------------------");
            System.out.println("✅ Se encontraron " + librosEncontrados.size() + " libros de " + nombreAutor + ".");
            System.out.println("Registrando libros...");
            System.out.println("----------------------------------------");

            for (DatosLibro datosLibro : librosEncontrados) {
                Optional<Libro> libroExistente = libroRepository.findByTitulo(datosLibro.titulo());
                if (libroExistente.isPresent()) {
                    System.out.println("⚠️ El libro '" + datosLibro.titulo() + "' ya está registrado.");
                } else {
                    Libro nuevoLibro = new Libro(datosLibro);
                    DatosAutor datosAutor = datosLibro.autores().get(0);
                    Optional<Autor> autorExistente = autorRepository.findByNombre(datosAutor.nombre());
                    if (autorExistente.isPresent()) {
                        nuevoLibro.setAutor(autorExistente.get());
                    } else {
                        Autor nuevoAutor = new Autor(datosAutor);
                        autorRepository.save(nuevoAutor);
                        nuevoLibro.setAutor(nuevoAutor);
                    }
                    libroRepository.save(nuevoLibro);
                    System.out.println("  -> Registrado: '" + nuevoLibro.getTitulo() + "'");
                }
            }
            System.out.println("----------------------------------------");
            System.out.println("Proceso de registro finalizado.");
            System.out.println("----------------------------------------");
        }
    }

    /**
     * Cuenta los libros por autor.
     */

    private void contarLibrosPorAutor() {
        System.out.println("\n----------------------------------------");
        System.out.println("  Conteo de Libros por Autor");
        System.out.println("----------------------------------------");
        List<Object[]> conteo = autorRepository.contarLibrosPorAutor();
        if (conteo.isEmpty()) {
            System.out.println("⚠️ No hay libros registrados en la base de datos.");
        } else {
            conteo.forEach(resultado -> System.out.println("Autor: " + resultado[0] + " - Total de libros: " + resultado[1]));
        }
        System.out.println("----------------------------------------");
    }






}


// Preparar el proyecto para el portafolio: Ahora es un buen momento para documentar tu código, asegurarte de que todo esté bien comentado y subir el proyecto a un repositorio de GitHub para que puedas compartirlo.