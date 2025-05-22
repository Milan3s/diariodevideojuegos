const db = require('../db');
const { format } = require('date-fns');
const fs = require('fs');
const path = require('path');
const { imagenesBasePath, videosBasePath, subirArchivos } = require('./ruta.controller');

module.exports = {
  subirArchivos,

  agregarJuego: (req, res) => {
    console.log('BODY:', req.body);
    console.log('FILES:', req.files);

    const {
      nombre, descripcion, desarrollador, editor, genero, modo_juego,
      fecha_lanzamiento, id_estado, es_recomendado, region,
      caja_original, precintado, id_consola
    } = req.body;

    const imagen = req.files?.imagen?.[0]?.originalname || null;
    const video = req.files?.video?.[0]?.originalname || null;

    db.query('SELECT 1 FROM estados WHERE id_estado = ?', [id_estado], (err, estadoRes) => {
      if (err || estadoRes.length === 0) {
        return res.status(400).json({ error: 'ID de estado inválido o error interno' });
      }

      const sql = `
        INSERT INTO juegos 
          (nombre, descripcion, desarrollador, editor, genero, modo_juego,
           fecha_lanzamiento, fecha_creacion_registro, id_estado, es_recomendado,
           imagen, video, region, caja_original, precintado)
        VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?, ?, ?, ?, ?, ?)
      `;

      const values = [
        nombre, descripcion, desarrollador, editor, genero, modo_juego,
        fecha_lanzamiento, id_estado, es_recomendado,
        imagen, video, region, caja_original, precintado
      ];

      db.query(sql, values, (err, result) => {
        if (err) return res.status(500).json({ error: 'Error al insertar juego' });

        const idJuego = result.insertId;

        if (!id_consola) return res.json({ mensaje: 'Juego insertado sin consola', id_juego: idJuego });

        db.query('SELECT 1 FROM consolas WHERE id_consola = ?', [id_consola], (err, consolaRes) => {
          if (err || consolaRes.length === 0) {
            return res.status(400).json({ error: 'ID de consola inválido o error interno' });
          }

          db.query('INSERT INTO juegos_consolas (id_juego, id_consola) VALUES (?, ?)', [idJuego, id_consola], (err) => {
            if (err) return res.status(500).json({ error: 'Error asociando consola' });
            res.json({ mensaje: 'Juego insertado correctamente', id_juego: idJuego });
          });
        });
      });
    });
  },

  actualizarJuego: (req, res) => {
    const id = req.params.id;
    const {
      nombre, descripcion, desarrollador, editor, genero, modo_juego,
      fecha_lanzamiento, id_estado, es_recomendado, region,
      caja_original, precintado, id_consola
    } = req.body;

    const imagen = req.files?.imagen?.[0]?.originalname || null;
    const video = req.files?.video?.[0]?.originalname || null;

    db.query('SELECT 1 FROM juegos WHERE id_juegos = ?', [id], (err, result) => {
      if (err || result.length === 0) {
        return res.status(404).json({ error: 'Juego no encontrado' });
      }

      const sql = `
        UPDATE juegos SET
          nombre = ?, descripcion = ?, desarrollador = ?, editor = ?, genero = ?,
          modo_juego = ?, fecha_lanzamiento = ?, id_estado = ?, es_recomendado = ?,
          imagen = COALESCE(?, imagen), video = COALESCE(?, video),
          region = ?, caja_original = ?, precintado = ?
        WHERE id_juegos = ?
      `;

      const values = [
        nombre, descripcion, desarrollador, editor, genero,
        modo_juego, fecha_lanzamiento, id_estado, es_recomendado,
        imagen, video, region, caja_original, precintado, id
      ];

      db.query(sql, values, (err) => {
        if (err) return res.status(500).json({ error: 'Error actualizando juego' });

        if (id_consola) {
          const deleteSql = 'DELETE FROM juegos_consolas WHERE id_juego = ?';
          db.query(deleteSql, [id], (err) => {
            if (err) return res.status(500).json({ error: 'Error eliminando consolas anteriores' });

            const insertSql = 'INSERT INTO juegos_consolas (id_juego, id_consola) VALUES (?, ?)';
            db.query(insertSql, [id, id_consola], (err) => {
              if (err) return res.status(500).json({ error: 'Error asociando consola' });
              res.json({ mensaje: 'Juego actualizado correctamente' });
            });
          });
        } else {
          res.json({ mensaje: 'Juego actualizado correctamente (sin consola)' });
        }
      });
    });
  },

  obtenerJuegos: (req, res) => {
    const sql = `
    SELECT 
      j.id_juegos, j.nombre, j.descripcion, j.desarrollador, j.editor,
      j.genero, j.modo_juego, j.fecha_lanzamiento, j.fecha_creacion_registro,
      j.id_estado, j.es_recomendado, j.imagen, j.video, j.region,
      j.caja_original, j.precintado,
      e.nombre AS estado_nombre,
      GROUP_CONCAT(c.nombre ORDER BY c.nombre SEPARATOR ', ') AS consola_nombre,
      GROUP_CONCAT(c.abreviatura ORDER BY c.nombre SEPARATOR ', ') AS consola_abreviatura,
      GROUP_CONCAT(c.id_consola ORDER BY c.nombre SEPARATOR ',') AS consola_ids
    FROM juegos j
    LEFT JOIN estados e ON j.id_estado = e.id_estado
    LEFT JOIN juegos_consolas jc ON j.id_juegos = jc.id_juego
    LEFT JOIN consolas c ON jc.id_consola = c.id_consola
    GROUP BY j.id_juegos
  `;

    db.query(sql, (err, results) => {
      if (err) return res.status(500).json({ error: 'Error al obtener juegos' });

      const juegos = results.map(j => {
        const consolaAbreviaturas = j.consola_abreviatura?.split(', ') || [];
        const consolaFolder = consolaAbreviaturas[0] || 'default';

        const imagenUrl = j.imagen
          ? `http://localhost:3000/imagenes_juegos/${consolaFolder}/boxart/${encodeURIComponent(j.imagen)}`
          : null;

        const videoUrl = j.video
          ? `http://localhost:3000/videos_juegos/${consolaFolder}/snap/${encodeURIComponent(j.video)}`
          : null;

        const consolaIds = j.consola_ids?.split(',').map(Number) || [];
        const id_consola = consolaIds.length > 0 ? consolaIds[0] : null;

        return {
          ...j,
          id_consola,
          fecha_lanzamiento: j.fecha_lanzamiento
            ? format(new Date(j.fecha_lanzamiento), 'yyyy-MM-dd')
            : null,
          RutaImagen: imagenUrl,
          RutaVideo: videoUrl,
        };
      });

      res.json(juegos);
    });
  },


  obtenerEstados: (req, res) => {
    db.query('SELECT id_estado, CONCAT(tipo, " → ", nombre) AS nombre FROM estados ORDER BY nombre', (err, results) => {
      if (err) return res.status(500).json({ error: 'Error al obtener estados' });
      res.json(results);
    });
  },

  obtenerConsolas: (req, res) => {
    db.query('SELECT id_consola, nombre, abreviatura FROM consolas ORDER BY nombre', (err, results) => {
      if (err) return res.status(500).json({ error: 'Error al obtener consolas' });
      res.json(results);
    });
  },

  eliminarJuego: (req, res) => {
    const id = req.params.id;

    const infoSql = `
      SELECT j.imagen, j.video,
             GROUP_CONCAT(c.abreviatura ORDER BY c.nombre SEPARATOR ', ') AS consola_abreviatura
      FROM juegos j
      LEFT JOIN juegos_consolas jc ON j.id_juegos = jc.id_juego
      LEFT JOIN consolas c ON jc.id_consola = c.id_consola
      WHERE j.id_juegos = ?
      GROUP BY j.id_juegos
    `;

    db.query(infoSql, [id], (err, results) => {
      if (err || results.length === 0) return res.status(404).json({ error: 'Juego no encontrado' });

      const juego = results[0];
      const consolaFolder = juego.consola_abreviatura?.split(', ')[0] || 'default';

      if (juego.imagen) {
        const imgPath = path.join(imagenesBasePath, consolaFolder, 'boxart', juego.imagen);
        if (fs.existsSync(imgPath)) fs.unlinkSync(imgPath);
      }

      if (juego.video) {
        const vidPath = path.join(videosBasePath, consolaFolder, 'snap', juego.video);
        if (fs.existsSync(vidPath)) fs.unlinkSync(vidPath);
      }

      db.query('DELETE FROM juegos_consolas WHERE id_juego = ?', [id], (err) => {
        if (err) return res.status(500).json({ error: 'Error eliminando consolas' });

        db.query('DELETE FROM juegos WHERE id_juegos = ?', [id], (err) => {
          if (err) return res.status(500).json({ error: 'Error eliminando juego' });
          res.json({ mensaje: 'Juego eliminado correctamente' });
        });
      });
    });
  },

  eliminarVariosJuegos: (req, res) => {
    const { ids } = req.body;

    if (!Array.isArray(ids) || ids.length === 0) {
      return res.status(400).json({ error: 'Se requiere un array de IDs para eliminar.' });
    }

    const placeholders = ids.map(() => '?').join(',');
    const deleteConsolasSql = `DELETE FROM juegos_consolas WHERE id_juego IN (${placeholders})`;
    const deleteJuegosSql = `DELETE FROM juegos WHERE id_juegos IN (${placeholders})`;

    db.query(deleteConsolasSql, ids, (err) => {
      if (err) return res.status(500).json({ error: 'Error eliminando relaciones con consolas' });

      db.query(deleteJuegosSql, ids, (err, result) => {
        if (err) return res.status(500).json({ error: 'Error eliminando juegos' });

        res.json({ mensaje: `${result.affectedRows} juegos eliminados correctamente.` });
      });
    });
  }


};
