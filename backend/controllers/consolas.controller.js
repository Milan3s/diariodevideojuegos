const db = require('../db');
const fs = require('fs');
const path = require('path');
const { imagenesBasePath, videosBasePath, subirArchivos } = require('./ruta.controller');
const { format } = require('date-fns');

module.exports = {
  subirArchivos,

  obtenerConsolas: (req, res) => {
    const sql = `
    SELECT 
      c.*, 
      e.nombre AS estado_nombre,
      (
        SELECT COUNT(*) 
        FROM juegos_consolas jc 
        WHERE jc.id_consola = c.id_consola
      ) AS cantidad_juegos
    FROM consolas c
    LEFT JOIN estados e ON c.id_estado = e.id_estado
    ORDER BY c.nombre
  `;


    db.query(sql, (err, results) => {
      if (err) return res.status(500).json({ error: 'Error obteniendo consolas' });

      const consolas = results.map(c => {
        const carpeta = c.abreviatura?.trim().toLowerCase().replace(/\s+/g, '_') || 'default';

        const imagenUrl = c.imagen
          ? `http://localhost:3000/imagenes_consolas/${carpeta}/${encodeURIComponent(c.imagen)}`
          : null;

        const videoUrl = c.video
          ? `http://localhost:3000/videos_consolas/${carpeta}/${encodeURIComponent(c.video)}`
          : null;

        return {
          ...c,
          fecha_lanzamiento: c.fecha_lanzamiento
            ? format(new Date(c.fecha_lanzamiento), 'yyyy-MM-dd')
            : null,
          RutaImagen: imagenUrl,
          RutaVideo: videoUrl
        };
      });

      res.json(consolas);
    });
  }
  ,

  agregarConsola: (req, res) => {
    try {
      const parseBool = (v) => v === 'true' || v === '1';
      const parseNumber = (v) => {
        const n = Number(v);
        return isNaN(n) ? null : n;
      };

      const {
        nombre, abreviatura, fabricante, generacion, region, tipo,
        procesador, memoria, almacenamiento, frecuencia_mhz,
        caracteristicas, fecha_lanzamiento, id_estado, hz
      } = req.body;

      const chip = parseBool(req.body.chip);
      const original = parseBool(req.body.original);
      const modificada = parseBool(req.body.modificada);
      const caja = parseBool(req.body.caja);
      const precintada = parseBool(req.body.precintada);

      const imagen = req.files?.imagen?.[0]?.originalname || null;
      const video = req.files?.video?.[0]?.originalname || null;

      const sql = `
      INSERT INTO consolas (
        nombre, abreviatura, fabricante, generacion, region, tipo,
        procesador, memoria, almacenamiento, frecuencia_mhz, chip, caracteristicas,
        fecha_lanzamiento, imagen, video, id_estado, original, modificada, caja, precintada, hz
      )
      VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    `;

      const values = [
        nombre || null,
        abreviatura || null,
        fabricante || null,
        generacion || null,
        region || null,
        tipo || null,
        procesador || null,
        memoria || null,
        almacenamiento || null,
        parseNumber(frecuencia_mhz),
        chip,
        caracteristicas || null,
        fecha_lanzamiento || null,
        imagen,
        video,
        parseNumber(id_estado),
        original,
        modificada,
        caja,
        precintada,
        hz || null
      ];

      // ✅ Aquí va el db.query con el manejo correcto del error
      db.query(sql, values, (err, result) => {
        if (err) {
          console.error('Error MySQL al insertar consola:', err);
          return res.status(500).json({
            error: 'Error al insertar consola',
            detalle: err.message
          });
        }

        res.json({ mensaje: 'Consola insertada correctamente', id_consola: result.insertId });
      });
    } catch (error) {
      console.error('Error inesperado en agregarConsola:', error);
      res.status(500).json({ error: 'Error inesperado al insertar consola', detalle: error.message });
    }
  },

  actualizarConsola: (req, res) => {
    const id = req.params.id;

    const parseBool = (v) => v === 'true' || v === '1';
    const parseNumber = (v) => {
      const n = Number(v);
      return isNaN(n) ? null : n;
    };

    // Extraer campos del cuerpo
    const {
      nombre, abreviatura, fabricante, generacion, region, tipo,
      procesador, memoria, almacenamiento, frecuencia_mhz,
      caracteristicas, fecha_lanzamiento, id_estado, hz
    } = req.body;

    // ✅ Extraer booleans correctamente
    const chip = parseBool(req.body.chip);
    const original = parseBool(req.body.original);
    const modificada = parseBool(req.body.modificada);
    const caja = parseBool(req.body.caja);
    const precintada = parseBool(req.body.precintada);

    // ✅ Archivos si hay
    const imagen = req.files?.imagen?.[0]?.originalname || null;
    const video = req.files?.video?.[0]?.originalname || null;

    const sql = `
    UPDATE consolas SET
      nombre = ?, abreviatura = ?, fabricante = ?, generacion = ?, region = ?, tipo = ?,
      procesador = ?, memoria = ?, almacenamiento = ?, frecuencia_mhz = ?, chip = ?, caracteristicas = ?,
      fecha_lanzamiento = ?, imagen = COALESCE(?, imagen), video = COALESCE(?, video), id_estado = ?, 
      original = ?, modificada = ?, caja = ?, precintada = ?, hz = ?
    WHERE id_consola = ?
  `;

    const values = [
      nombre, abreviatura, fabricante, generacion, region, tipo,
      procesador, memoria, almacenamiento, parseNumber(frecuencia_mhz), chip, caracteristicas,
      fecha_lanzamiento, imagen, video, parseNumber(id_estado),
      original, modificada, caja, precintada, hz,
      id
    ];

    db.query(sql, values, (err) => {
      if (err) return res.status(500).json({ error: 'Error actualizando consola' });
      res.json({ mensaje: 'Consola actualizada correctamente' });
    });
  }

  ,

  obtenerEstadosConsolas: (req, res) => {
    const sql = `
      SELECT id_estado, nombre 
      FROM estados 
      WHERE tipo = 'consola'
      ORDER BY nombre
    `;

    db.query(sql, (err, results) => {
      if (err) return res.status(500).json({ error: 'Error obteniendo estados de consola' });
      res.json(results);
    });
  },

  eliminarConsola: (req, res) => {
    const id = req.params.id;

    db.query('SELECT abreviatura, imagen, video FROM consolas WHERE id_consola = ?', [id], (err, rows) => {
      if (err || rows.length === 0) return res.status(404).json({ error: 'Consola no encontrada' });

      const { abreviatura, imagen, video } = rows[0];
      const carpeta = abreviatura?.trim().toLowerCase().replace(/\s+/g, '_') || 'default';

      if (imagen) {
        const rutaImagen = path.join(imagenesBasePath, carpeta, imagen);
        if (fs.existsSync(rutaImagen)) fs.unlinkSync(rutaImagen);
      }

      if (video) {
        const rutaVideo = path.join(videosBasePath, carpeta, video);
        if (fs.existsSync(rutaVideo)) fs.unlinkSync(rutaVideo);
      }

      db.query('DELETE FROM consolas WHERE id_consola = ?', [id], (err) => {
        if (err) return res.status(500).json({ error: 'Error eliminando consola' });
        res.json({ mensaje: 'Consola eliminada correctamente' });
      });
    });
  },

  eliminarVariasConsolas: (req, res) => {
    const ids = req.body.ids;

    if (!Array.isArray(ids) || ids.length === 0) {
      return res.status(400).json({ error: 'Lista de IDs no válida' });
    }

    const placeholders = ids.map(() => '?').join(', ');
    db.query(`SELECT abreviatura, imagen, video FROM consolas WHERE id_consola IN (${placeholders})`, ids, (err, consolas) => {
      if (err) return res.status(500).json({ error: 'Error obteniendo consolas para eliminar' });

      consolas.forEach(({ abreviatura, imagen, video }) => {
        const carpeta = abreviatura?.trim().toLowerCase().replace(/\s+/g, '_') || 'default';

        if (imagen) {
          const rutaImagen = path.join(imagenesBasePath, carpeta, imagen);
          if (fs.existsSync(rutaImagen)) fs.unlinkSync(rutaImagen);
        }

        if (video) {
          const rutaVideo = path.join(videosBasePath, carpeta, video);
          if (fs.existsSync(rutaVideo)) fs.unlinkSync(rutaVideo);
        }
      });

      db.query(`DELETE FROM consolas WHERE id_consola IN (${placeholders})`, ids, (err) => {
        if (err) return res.status(500).json({ error: 'Error eliminando consolas' });
        res.json({ mensaje: 'Consolas eliminadas correctamente' });
      });
    });
  }
};
