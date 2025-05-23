const db = require('../db');

module.exports = {
  obtenerMetas: (req, res) => {
    const sql = `
      SELECT m.id_meta_especifica, m.descripcion, m.juegos_objetivo, m.juegos_completados,
             m.fabricante, m.id_consola, m.fecha_inicio, m.fecha_fin, m.id_estado_metas,
             c.nombre AS consola_nombre, e.nombre_estado AS estado_nombre
      FROM metas_especificas m
      LEFT JOIN consolas c ON m.id_consola = c.id_consola
      LEFT JOIN estado_metas e ON m.id_estado_metas = e.id
      WHERE e.tipo_meta = 'especificas'
      ORDER BY m.fecha_registro DESC
    `;

    db.query(sql, (err, resultados) => {
      if (err) return res.status(500).json({ error: 'Error al obtener metas' });
      res.json(resultados);
    });
  },

  agregarMeta: (req, res) => {
    const {
      descripcion, juegos_objetivo, juegos_completados,
      fabricante, id_consola, fecha_inicio, fecha_fin, id_estado_metas
    } = req.body;

    const sql = `
      INSERT INTO metas_especificas
      (descripcion, juegos_objetivo, juegos_completados, fabricante,
       id_consola, fecha_inicio, fecha_fin, fecha_registro, id_estado_metas)
      VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), ?)
    `;

    const values = [
      descripcion, juegos_objetivo, juegos_completados,
      fabricante, id_consola, fecha_inicio, fecha_fin, id_estado_metas
    ];

    db.query(sql, values, (err, result) => {
      if (err) return res.status(500).json({ error: 'Error al insertar meta' });
      res.json({ mensaje: 'Meta insertada correctamente', id_meta_especifica: result.insertId });
    });
  },

  actualizarMeta: (req, res) => {
    const id = req.params.id;
    const {
      descripcion, juegos_objetivo, juegos_completados,
      fabricante, id_consola, fecha_inicio, fecha_fin, id_estado_metas
    } = req.body;

    const sql = `
      UPDATE metas_especificas SET
        descripcion = ?, juegos_objetivo = ?, juegos_completados = ?, fabricante = ?,
        id_consola = ?, fecha_inicio = ?, fecha_fin = ?, id_estado_metas = ?
      WHERE id_meta_especifica = ?
    `;

    const values = [
      descripcion, juegos_objetivo, juegos_completados,
      fabricante, id_consola, fecha_inicio, fecha_fin, id_estado_metas, id
    ];

    db.query(sql, values, (err) => {
      if (err) return res.status(500).json({ error: 'Error al actualizar meta' });
      res.json({ mensaje: 'Meta actualizada correctamente' });
    });
  },

  eliminarMultiples: (req, res) => {
    const { ids } = req.body;
    if (!Array.isArray(ids) || ids.length === 0) {
      return res.status(400).json({ error: 'Se requiere un array de IDs para eliminar.' });
    }

    const placeholders = ids.map(() => '?').join(',');
    const sql = `DELETE FROM metas_especificas WHERE id_meta_especifica IN (${placeholders})`;

    db.query(sql, ids, (err, result) => {
      if (err) return res.status(500).json({ error: 'Error eliminando metas' });
      res.json({ mensaje: `${result.affectedRows} metas eliminadas correctamente.` });
    });
  },

  obtenerEstados: (req, res) => {
    const sql = `SELECT id, nombre_estado FROM estado_metas WHERE tipo_meta = 'especificas' ORDER BY nombre_estado`;
    db.query(sql, (err, results) => {
      if (err) return res.status(500).json({ error: 'Error al obtener estados' });
      res.json(results);
    });
  },

  obtenerConsolas: (req, res) => {
    const sql = 'SELECT id_consola, nombre, abreviatura FROM consolas ORDER BY nombre';
    db.query(sql, (err, results) => {
      if (err) return res.status(500).json({ error: 'Error al obtener consolas' });
      res.json(results);
    });
  }
};
