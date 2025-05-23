const db = require('../db');
const { format } = require('date-fns');

module.exports = {
  // 🔍 Obtener todas las metas
  obtenerMetasTwitch: (req, res) => {
    const sql = `
      SELECT 
        m.id_meta,
        m.descripcion,
        m.meta,
        m.actual,
        m.fecha_inicio,
        m.fecha_fin,
        m.fecha_registro,
        m.id_estado_metas,
        e.nombre_estado AS estado_nombre
      FROM metas_twitch m
      LEFT JOIN estado_metas e 
        ON m.id_estado_metas = e.id 
        AND e.tipo_meta = 'twitch'
      ORDER BY m.fecha_inicio ASC
    `;

    db.query(sql, (err, resultados) => {
      if (err) {
        console.error('❌ Error al obtener metas de Twitch:', err);
        return res.status(500).json({ mensaje: 'Error al obtener metas de Twitch' });
      }

      const metas = resultados.map(meta => ({
        ...meta,
        fecha_inicio: meta.fecha_inicio ? format(new Date(meta.fecha_inicio), 'yyyy-MM-dd') : null,
        fecha_fin: meta.fecha_fin ? format(new Date(meta.fecha_fin), 'yyyy-MM-dd') : null,
        fecha_registro: meta.fecha_registro ? format(new Date(meta.fecha_registro), 'yyyy-MM-dd HH:mm:ss') : null
      }));

      res.json(metas);
    });
  },

  // 🛠️ Actualizar una meta específica
  actualizarMetaTwitch: (req, res) => {
    const { id_meta } = req.params;
    const { descripcion, meta, actual, fecha_inicio, fecha_fin, id_estado_metas } = req.body;

    const sql = `
      UPDATE metas_twitch
      SET descripcion = ?, meta = ?, actual = ?, fecha_inicio = ?, fecha_fin = ?, id_estado_metas = ?
      WHERE id_meta = ?
    `;

    const valores = [descripcion, meta, actual, fecha_inicio, fecha_fin, id_estado_metas, id_meta];

    db.query(sql, valores, (err, resultado) => {
      if (err) {
        console.error('❌ Error al actualizar meta Twitch:', err);
        return res.status(500).json({ mensaje: 'Error al actualizar meta Twitch' });
      }

      if (resultado.affectedRows === 0) {
        return res.status(404).json({ mensaje: 'Meta no encontrada' });
      }

      res.json({ mensaje: '✅ Meta actualizada correctamente' });
    });
  },

  // ➕ Insertar una nueva meta
  insertarMetaTwitch: (req, res) => {
    const { descripcion, meta, actual, fecha_inicio, fecha_fin, id_estado_metas } = req.body;

    const sql = `
      INSERT INTO metas_twitch (descripcion, meta, actual, fecha_inicio, fecha_fin, id_estado_metas)
      VALUES (?, ?, ?, ?, ?, ?)
    `;

    const valores = [descripcion, meta, actual, fecha_inicio, fecha_fin, id_estado_metas];

    db.query(sql, valores, (err, resultado) => {
      if (err) {
        console.error('❌ Error al insertar nueva meta Twitch:', err);
        return res.status(500).json({ mensaje: 'Error al insertar meta Twitch' });
      }

      res.status(201).json({ mensaje: '✅ Meta insertada correctamente', id_meta: resultado.insertId });
    });
  },

  // ❌ Eliminar metas seleccionadas
  eliminarMetasSeleccionadas: (req, res) => {
    const { ids } = req.body;

    if (!Array.isArray(ids) || ids.length === 0) {
      return res.status(400).json({ mensaje: 'Debes proporcionar una lista de IDs' });
    }

    const sql = `DELETE FROM metas_twitch WHERE id_meta IN (?)`;

    db.query(sql, [ids], (err, resultado) => {
      if (err) {
        console.error('❌ Error al eliminar metas seleccionadas:', err);
        return res.status(500).json({ mensaje: 'Error al eliminar metas seleccionadas' });
      }

      res.json({ mensaje: `✅ Se eliminaron ${resultado.affectedRows} metas` });
    });
  },

  // 🔽 Obtener los estados para el selector
  obtenerEstadosMetasTwitch: (req, res) => {
    const tipo = req.query.tipo || 'twitch';

    const sql = `
      SELECT id, nombre_estado 
      FROM estado_metas 
      WHERE tipo_meta = ?
      ORDER BY id ASC
    `;

    db.query(sql, [tipo], (err, resultados) => {
      if (err) {
        console.error('❌ Error al obtener estados de metas Twitch:', err);
        return res.status(500).json({ mensaje: 'Error al obtener estados de metas Twitch' });
      }

      res.json(resultados);
    });
  }
};
