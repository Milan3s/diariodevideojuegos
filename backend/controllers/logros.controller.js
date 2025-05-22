const db = require('../db');
const { format } = require('date-fns');

module.exports = {
  obtenerLogros: (req, res) => {
    const sql = `
      SELECT 
        l.id_logro,
        l.nombre,
        l.descripcion,
        l.horas_estimadas,
        l.anio,
        l.fecha_inicio,
        l.fecha_fin,
        l.intentos,
        l.creditos,
        l.puntuacion,
        l.progreso,
        l.fecha_registro,
        l.id_juego,
        j.nombre AS juego_nombre,
        l.id_estado,
        el.nombre AS estado_logro_nombre,
        l.id_dificultad,
        d.nombre AS dificultad_nombre,
        l.id_consola
      FROM logros l
      LEFT JOIN juegos j ON l.id_juego = j.id_juegos
      LEFT JOIN estados el ON l.id_estado = el.id_estado AND el.tipo = 'logro'
      LEFT JOIN dificultades_logros d ON l.id_dificultad = d.id_dificultad
      LEFT JOIN consolas c ON l.id_consola = c.id_consola
      ORDER BY l.fecha_registro DESC
    `;

    db.query(sql, (err, resultados) => {
      if (err) {
        console.error('❌ Error al obtener logros:', err);
        return res.status(500).json({ mensaje: 'Error al obtener logros' });
      }

      const logros = resultados.map(l => ({
        ...l,
        fecha_inicio: l.fecha_inicio ? format(new Date(l.fecha_inicio), 'yyyy-MM-dd') : null,
        fecha_fin: l.fecha_fin ? format(new Date(l.fecha_fin), 'yyyy-MM-dd') : null,
        fecha_registro: l.fecha_registro ? format(new Date(l.fecha_registro), 'yyyy-MM-dd') : null
      }));

      res.json(logros);
    });
  },

  agregarLogro: (req, res) => {
    const logro = req.body;
    const sql = `
      INSERT INTO logros (
        nombre, descripcion, horas_estimadas, anio, fecha_inicio, fecha_fin,
        intentos, creditos, puntuacion, progreso, fecha_registro,
        id_juego, id_estado, id_dificultad, id_consola
      )
      VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?, ?, ?)
    `;

    const valores = [
      logro.nombre,
      logro.descripcion,
      logro.horas_estimadas,
      logro.anio,
      logro.fecha_inicio,
      logro.fecha_fin,
      logro.intentos,
      logro.creditos,
      logro.puntuacion,
      logro.progreso,
      logro.id_juego,
      logro.id_estado,
      logro.id_dificultad,
      logro.id_consola
    ];

    db.query(sql, valores, (err, resultado) => {
      if (err) {
        console.error('❌ Error al agregar logro:', err);
        return res.status(500).json({ mensaje: 'Error al agregar logro' });
      }

      res.json({ mensaje: 'Logro agregado correctamente', id_logro: resultado.insertId });
    });
  },

  actualizarLogro: (req, res) => {
    const id = req.params.id;
    const logro = req.body;
    const sql = `
      UPDATE logros
      SET nombre = ?, descripcion = ?, horas_estimadas = ?, anio = ?,
          fecha_inicio = ?, fecha_fin = ?, intentos = ?, creditos = ?,
          puntuacion = ?, progreso = ?, id_juego = ?, id_estado = ?,
          id_dificultad = ?, id_consola = ?
      WHERE id_logro = ?
    `;

    const valores = [
      logro.nombre,
      logro.descripcion,
      logro.horas_estimadas,
      logro.anio,
      logro.fecha_inicio,
      logro.fecha_fin,
      logro.intentos,
      logro.creditos,
      logro.puntuacion,
      logro.progreso,
      logro.id_juego,
      logro.id_estado,
      logro.id_dificultad,
      logro.id_consola,
      id
    ];

    db.query(sql, valores, (err) => {
      if (err) {
        console.error('❌ Error al actualizar logro:', err);
        return res.status(500).json({ mensaje: 'Error al actualizar logro' });
      }

      res.json({ mensaje: 'Logro actualizado correctamente' });
    });
  },

  eliminarLogro: (req, res) => {
    const id = req.params.id;
    const sql = 'DELETE FROM logros WHERE id_logro = ?';

    db.query(sql, [id], (err) => {
      if (err) {
        console.error('❌ Error al eliminar logro:', err);
        return res.status(500).json({ mensaje: 'Error al eliminar logro' });
      }

      res.json({ mensaje: 'Logro eliminado correctamente' });
    });
  },

  eliminarMultiplesLogros: (req, res) => {
    const ids = req.body.ids;
    if (!Array.isArray(ids) || ids.length === 0) {
      return res.status(400).json({ mensaje: 'No se proporcionaron logros a eliminar' });
    }

    const sql = `DELETE FROM logros WHERE id_logro IN (?)`;
    db.query(sql, [ids], (err) => {
      if (err) {
        console.error('❌ Error al eliminar múltiples logros:', err);
        return res.status(500).json({ mensaje: 'Error al eliminar múltiples logros' });
      }

      res.json({ mensaje: 'Logros eliminados correctamente' });
    });
  },

  obtenerEstados: (req, res) => {
    const tipo = req.query.tipo;

    let sql = 'SELECT id_estado, nombre, tipo FROM estados';
    const params = [];

    if (tipo) {
      sql += ' WHERE tipo = ?';
      params.push(tipo);
    }

    sql += ' ORDER BY nombre';

    db.query(sql, params, (err, resultados) => {
      if (err) {
        console.error('❌ Error al obtener estados:', err);
        return res.status(500).json({ mensaje: 'Error al obtener estados' });
      }

      res.json(resultados);
    });
  },

  obtenerJuegos: (req, res) => {
    db.query('SELECT id_juegos, nombre FROM juegos ORDER BY nombre', (err, resultados) => {
      if (err) {
        console.error('❌ Error al obtener juegos:', err);
        return res.status(500).json({ mensaje: 'Error al obtener juegos' });
      }
      res.json(resultados);
    });
  },

  obtenerDificultades: (req, res) => {
    db.query('SELECT id_dificultad, nombre FROM dificultades_logros ORDER BY nombre', (err, resultados) => {
      if (err) {
        console.error('❌ Error al obtener dificultades:', err);
        return res.status(500).json({ mensaje: 'Error al obtener dificultades' });
      }
      res.json(resultados);
    });
  },

  obtenerConsolas: (req, res) => {
    db.query('SELECT id_consola, nombre, abreviatura FROM consolas ORDER BY nombre', (err, resultados) => {
      if (err) {
        console.error('❌ Error al obtener consolas:', err);
        return res.status(500).json({ mensaje: 'Error al obtener consolas' });
      }
      res.json(resultados);
    });
  }
};
