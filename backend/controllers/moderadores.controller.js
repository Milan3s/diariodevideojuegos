const db = require('../db');
const { format } = require('date-fns');

module.exports = {
  obtenerModeradores: (req, res) => {
    const sql = `
      SELECT 
        m.id_moderador,
        m.nombre,
        m.email,
        m.fecha_alta,
        m.fecha_baja,
        m.id_estado,
        e.nombre AS estado_nombre
      FROM moderadores m
      LEFT JOIN estados e ON m.id_estado = e.id_estado AND e.tipo = 'moderador'
      ORDER BY m.nombre ASC
    `;

    db.query(sql, (err, resultados) => {
      if (err) {
        console.error('❌ Error al obtener moderadores:', err);
        return res.status(500).json({ mensaje: 'Error al obtener moderadores' });
      }

      const moderadores = resultados.map(m => ({
        ...m,
        fecha_alta: m.fecha_alta ? format(new Date(m.fecha_alta), 'yyyy-MM-dd') : null,
        fecha_baja: m.fecha_baja ? format(new Date(m.fecha_baja), 'yyyy-MM-dd') : null
      }));

      res.json(moderadores);
    });
  }
};
