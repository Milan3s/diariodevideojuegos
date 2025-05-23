const db = require('../db');
const { format } = require('date-fns');

module.exports = {
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
        fecha_inicio: meta.fecha_inicio ? format(new Date(meta.fecha_inicio), 'dd-MM-yyyy') : null,
        fecha_fin: meta.fecha_fin ? format(new Date(meta.fecha_fin), 'dd-MM-yyyy') : null,
        fecha_registro: meta.fecha_registro ? format(new Date(meta.fecha_registro), 'dd-MM-yyyy HH:mm:ss') : null
      }));

      res.json(metas);
    });
  },

  obtenerEstadosMetasTwitch: (req, res) => {
    const tipo = req.query.tipo || 'twitch'; // por defecto 'twitch' si no se pasa tipo

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
