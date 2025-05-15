package config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DatabaseInsertar {

    public static void insertarDatos() {
        try (Connection conn = Conexion.obtenerConexion()) {
            if (conn != null) {
                Statement stmt = conn.createStatement();

                String sqlConfiguracionAuxiliares =
                        "INSERT OR IGNORE INTO configuracion_auxiliares (nombre_visual, nombre_tabla, columna_id, columna_nombre) VALUES "
                        + "('Estados | Juego', 'estados', 'id_estado', 'nombre'), "
                        + "('Estados | Logro', 'estados', 'id_estado', 'nombre'), "
                        + "('Estados | Moderador', 'estados', 'id_estado', 'nombre'), "
                        + "('Estados | Consola', 'estados', 'id_estado', 'nombre'), "
                        + "('Dificultades', 'dificultades_logros', 'id_dificultad', 'nombre');";

                String sqlEstados =
                        "INSERT INTO estados (tipo, nombre) VALUES "
                        + "('juego', 'Pendiente'), ('juego', 'Jugando'), ('juego', 'Completado'), ('juego', 'Abandonado'), "
                        + "('logro', 'Pendiente'), ('logro', 'Completado'), "
                        + "('moderador', 'Activo'), ('moderador', 'Inactivo'), "
                        + "('consola', 'Original'), ('consola', 'Con chip');";

                String sqlDificultades =
                        "INSERT INTO dificultades_logros (nombre, tipo) VALUES "
                        + "('Fácil', 'logro'), ('Media', 'logro'), ('Difícil', 'logro'), ('Extrema', 'logro');";

                String sqlConsolas =
                    "INSERT INTO consolas (nombre, abreviatura, anio, fabricante, generacion, region, tipo, procesador, memoria, almacenamiento, frecuencia_mhz, chip, caracteristicas, fecha_lanzamiento, imagen, id_estado) VALUES "
                    // Consolas originales con datos corregidos
                    + "('Atari 2600', 'A2600', 1977, 'Atari', 'Segunda', 'PAL', 'Sobremesa', 'MOS Technology 6507', '128 B', 'Cartucho', 1.19, 1, 'Primera consola doméstica de éxito', '1977-09-11', 'atari2600.jpg', 1), "
                    + "('NES', 'NES', 1983, 'Nintendo', 'Tercera', 'PAL', 'Sobremesa', 'Ricoh 2A03', '2 KB', 'Cartucho', 1.79, 1, 'Revitalizó la industria del videojuego', '1983-07-15', 'nes.jpg', 1), "
                    + "('SNES', 'SNES', 1990, 'Nintendo', 'Cuarta', 'PAL', 'Sobremesa', 'Ricoh 5A22', '128 KB', 'Cartucho', 3.58, 2, 'Avances gráficos y de sonido', '1990-11-21', 'snes.jpg', 1), "
                    + "('Sega Genesis', 'GEN', 1988, 'Sega', 'Cuarta', 'PAL', 'Sobremesa', 'Motorola 68000', '64 KB', 'Cartucho', 7.67, 1, 'Gran rivalidad con SNES', '1988-10-29', 'genesis.jpg', 1), "
                    + "('Nintendo 64', 'N64', 1996, 'Nintendo', 'Quinta', 'PAL', 'Sobremesa', 'NEC VR4300', '4 MB', 'Cartucho', 93.75, 3, 'Primer control analógico', '1996-06-23', 'n64.jpg', 1), "
                    + "('PlayStation', 'PSX', 1994, 'Sony', 'Quinta', 'PAL', 'Sobremesa', 'MIPS R3000A', '2 MB', 'CD-ROM', 33.75, 1, 'CD como soporte principal', '1994-12-03', 'psx.jpg', 1), "
                    + "('PlayStation 2', 'PS2', 2000, 'Sony', 'Sexta', 'PAL', 'Sobremesa', 'Emotion Engine', '32 MB', 'DVD-ROM', 294.9, 2, 'La consola más vendida de la historia', '2000-03-04', 'ps2.jpg', 1), "

                    // Otras consolas con datos precisos
                    + "('Atari 5200', 'A5200', 1982, 'Atari', 'Segunda', 'NTSC', 'Sobremesa', 'MOS 6502C', '16 KB', 'Cartucho', 1.79, 1, 'Mejoras sobre Atari 2600', '1982-11-01', 'atari5200.jpg', 1), "
                    + "('Atari 7800', 'A7800', 1986, 'Atari', 'Tercera', 'NTSC', 'Sobremesa', '6502C', '4 KB', 'Cartucho', 1.79, 1, 'Retrocompatible con 2600', '1986-05-01', 'atari7800.jpg', 1), "
                    + "('Atari Jaguar', 'JAG', 1993, 'Atari', 'Quinta', 'PAL', 'Sobremesa', 'Motorola 68000', '2 MB', 'Cartucho', 26.6, 2, 'Consola de 64 bits', '1993-11-23', 'jaguar.jpg', 1), "
                    + "('GameCube', 'GCN', 2001, 'Nintendo', 'Sexta', 'PAL', 'Sobremesa', 'IBM PowerPC', '24 MB', 'MiniDVD', 486.0, 3, 'Discos propietarios pequeños', '2001-09-14', 'gamecube.jpg', 1), "
                    + "('Wii', 'WII', 2006, 'Nintendo', 'Séptima', 'PAL', 'Sobremesa', 'IBM PowerPC', '88 MB', 'DVD', 729.0, 4, 'Control por movimiento', '2006-12-08', 'wii.jpg', 1), "
                    + "('Sega Master System', 'SMS', 1985, 'Sega', 'Tercera', 'PAL', 'Sobremesa', 'Zilog Z80', '8 KB', 'Cartucho', 3.58, 1, 'Competidor del NES', '1985-10-20', 'sms.jpg', 1), "
                    + "('Sega Saturn', 'SAT', 1994, 'Sega', 'Quinta', 'PAL', 'Sobremesa', 'Dual Hitachi SH-2', '2 MB', 'CD-ROM', 28.6, 2, 'Doble procesador', '1994-11-22', 'saturn.jpg', 1), "
                    + "('Sega Dreamcast', 'DC', 1998, 'Sega', 'Sexta', 'PAL', 'Sobremesa', 'Hitachi SH-4', '16 MB', 'GD-ROM', 200.0, 3, 'Primera con modem integrado', '1998-11-27', 'dreamcast.jpg', 1), "

                    // Consolas portátiles
                    + "('Game Boy', 'GB', 1989, 'Nintendo', 'Tercera', 'PAL', 'Portátil', 'Custom 8080', '8 KB', 'Cartucho', 4.19, 1, 'Primera portátil exitosa', '1989-04-21', 'gameboy.jpg', 1), "
                    + "('Game Boy Advance', 'GBA', 2001, 'Nintendo', 'Sexta', 'PAL', 'Portátil', 'ARM7TDMI', '32 KB', 'Cartucho', 16.78, 2, 'Gráficos similares a SNES', '2001-03-21', 'gba.jpg', 1), "
                    + "('Nintendo DS', 'NDS', 2004, 'Nintendo', 'Séptima', 'PAL', 'Portátil', 'ARM946E-S', '4 MB', 'Tarjeta DS', 67.0, 2, 'Pantalla táctil', '2004-11-21', 'nds.jpg', 1), "
                    + "('PlayStation Portable', 'PSP', 2004, 'Sony', 'Séptima', 'PAL', 'Portátil', 'MIPS R4000', '32 MB', 'UMD', 333.0, 3, 'Potente portátil multimedia', '2004-12-12', 'psp.jpg', 1);";

                String sqlJuegos =
                        "INSERT INTO juegos (nombre, descripcion, desarrollador, editor, genero, modo_juego, fecha_lanzamiento, id_estado, es_recomendado, imagen, video) VALUES "
                        + "('Super Mario Bros.', 'Plataformas clásico de NES.', 'Nintendo', 'Nintendo', 'Plataformas', 'Un jugador', '1985-09-13', 1, 1, 'smb.jpg', 'https://youtu.be/e5C2g1U1Gn4'), "
                        + "('The Legend of Zelda', 'Aventura con exploración libre.', 'Nintendo', 'Nintendo', 'Aventura', 'Un jugador', '1986-02-21', 1, 1, 'zelda_nes.jpg', 'https://youtu.be/cPCLFtxpadE'), "
                        + "('Sonic the Hedgehog', 'Mascota veloz de Sega.', 'Sega', 'Sega', 'Plataformas', 'Un jugador', '1991-06-23', 1, 1, 'sonic.jpg', 'https://youtu.be/aUZ1W1v5MBY'), "
                        + "('Donkey Kong Country', 'Innovador en gráficos prerenderizados.', 'Rare', 'Nintendo', 'Plataformas', 'Un jugador', '1994-11-21', 1, 1, 'dkc.jpg', 'https://youtu.be/JVaU-BhG3FA'), "
                        + "('GoldenEye 007', 'Shooter basado en película Bond.', 'Rare', 'Nintendo', 'FPS', 'Multijugador / Un jugador', '1997-08-25', 1, 1, 'goldeneye.jpg', 'https://youtu.be/LVQbYnzSRFE'), "
                        + "('Final Fantasy VII', 'RPG con Cloud Strife.', 'Square', 'Square', 'JRPG', 'Un jugador', '1997-01-31', 1, 1, 'ff7.jpg', 'https://youtu.be/q5Yj5Z1hM80'), "
                        + "('Shadow of the Colossus', 'Aventura contra gigantes.', 'Team Ico', 'Sony', 'Aventura', 'Un jugador', '2005-10-18', 1, 1, 'sotc.jpg', 'https://youtu.be/yJ6sRO3z-YU');";

                String sqlRelaciones =
                        "INSERT INTO juegos_consolas (id_juego, id_consola) VALUES "
                        + "(1, 2), (2, 2), (3, 4), (4, 3), (5, 5), (6, 6), (7, 7);";

                String sqlVotosConsolas =
                        "INSERT INTO votos_consolas (id_consola, diseno, comodidad, catalogo, durabilidad, precio, conectividad, tienda, comentarios, nota) VALUES "
                        + "(2, 5, 5, 8, 6, 6, 2, 'Retro Store', 'La NES cambió todo.', 8), "
                        + "(3, 6, 6, 9, 7, 5, 3, 'Retro Store', 'Gráficos avanzados para su época.', 9), "
                        + "(4, 7, 6, 8, 6, 5, 3, 'Retro Store', 'Sonic corriendo a tope.', 8), "
                        + "(5, 7, 6, 8, 7, 4, 3, 'Retro Store', 'Multijugador local legendario.', 8), "
                        + "(6, 8, 7, 9, 8, 6, 5, 'Retro Store', 'RPGs inolvidables.', 9), "
                        + "(7, 9, 8, 10, 8, 6, 6, 'Retro Store', 'Catálogo imbatible.', 10);";

                String sqlVotosJuegos =
                        "INSERT INTO votos_juegos (id_juego, jugabilidad, dificultad, diseno_niveles, graficos, sonido, controles, adiccion, rejugabilidad, valor_nostalgico, innovacion, diseno_visual, multijugador_local, precio, tienda, comentarios, nota) VALUES "
                        + "(1, 9, 5, 8, 7, 6, 8, 10, 7, 10, 8, 7, 0, 25, 'Retro Store', 'El origen de los plataformas.', 9), "
                        + "(2, 9, 6, 9, 7, 6, 9, 9, 8, 10, 9, 8, 0, 30, 'Retro Store', 'Pionero en aventuras.', 9), "
                        + "(3, 9, 4, 7, 8, 7, 9, 8, 7, 8, 7, 8, 0, 20, 'Retro Store', 'Rápido y adictivo.', 8), "
                        + "(4, 9, 5, 9, 9, 8, 9, 8, 8, 9, 9, 9, 0, 25, 'Retro Store', 'Increíbles gráficos en SNES.', 9), "
                        + "(5, 8, 7, 8, 7, 7, 8, 8, 7, 8, 8, 8, 1, 30, 'Retro Store', 'Multijugador mítico.', 8), "
                        + "(6, 10, 6, 9, 8, 9, 9, 10, 9, 10, 10, 9, 0, 35, 'Retro Store', 'Obra maestra de los RPG.', 10), "
                        + "(7, 9, 6, 9, 10, 10, 9, 8, 8, 9, 9, 10, 0, 30, 'Retro Store', 'Pura poesía visual.', 10);";

                stmt.execute(sqlConfiguracionAuxiliares);
                stmt.execute(sqlEstados);
                stmt.execute(sqlDificultades);
                stmt.execute(sqlConsolas);
                stmt.execute(sqlJuegos);
                stmt.execute(sqlRelaciones);
                stmt.execute(sqlVotosConsolas);
                stmt.execute(sqlVotosJuegos);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Datos reales insertados correctamente.", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al insertar los datos: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}