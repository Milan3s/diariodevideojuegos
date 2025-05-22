const db = require('../db');
const { format } = require('date-fns');

module.exports = {
  // ✅ Obtener todos los moderadores
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
        fecha_alta: m.fecha_alta ? format(new Date(m.fecha_alta), 'dd-MM-yyyy') : null,
        fecha_baja: m.fecha_baja ? format(new Date(m.fecha_baja), 'dd-MM-yyyy') : null
      }));

      res.json(moderadores);
    });
  },

  // ✅ Crear un nuevo moderador
  agregarModerador: (req, res) => {
    const { nombre, email, id_estado } = req.body;

    if (!nombre || !email || !id_estado) {
      return res.status(400).json({ mensaje: 'Faltan campos obligatorios' });
    }

    const sql = `
      INSERT INTO moderadores (nombre, email, id_estado, fecha_alta)
      VALUES (?, ?, ?, NOW())
    `;

    db.query(sql, [nombre, email, id_estado], (err, resultado) => {
      if (err) {
        console.error('❌ Error al insertar moderador:', err);
        return res.status(500).json({ mensaje: 'Error al insertar moderador' });
      }

      res.status(201).json({
        mensaje: 'Moderador insertado correctamente',
        id_moderador: resultado.insertId
      });
    });
  },

  // ✅ Dar de alta un moderador existente
  darDeAltaModerador: (req, res) => {
    const id = req.params.id;

    const sql = `
      UPDATE moderadores 
      SET fecha_alta = CURDATE(), fecha_baja = NULL 
      WHERE id_moderador = ?
    `;

    db.query(sql, [id], (err) => {
      if (err) {
        console.error('❌ Error al dar de alta moderador:', err);
        return res.status(500).json({ mensaje: 'Error al dar de alta moderador' });
      }

      res.json({ mensaje: 'Moderador dado de alta correctamente' });
    });
  },

  // ✅ Actualizar moderador
  actualizarModerador: (req, res) => {
    const id = req.params.id;
    const { nombre, email, id_estado } = req.body;

    if (!nombre || !email || !id_estado) {
      return res.status(400).json({ mensaje: 'Faltan campos obligatorios para la actualización' });
    }

    const sql = `
      UPDATE moderadores
      SET nombre = ?, email = ?, id_estado = ?
      WHERE id_moderador = ?
    `;

    db.query(sql, [nombre, email, id_estado, id], (err) => {
      if (err) {
        console.error('❌ Error al actualizar moderador:', err);
        return res.status(500).json({ mensaje: 'Error al actualizar moderador' });
      }

      res.json({ mensaje: 'Moderador actualizado correctamente' });
    });
  },

  // ✅ Eliminar uno o varios moderadores por ID
  eliminarModeradores: (req, res) => {
    const { ids } = req.body;

    if (!Array.isArray(ids) || ids.length === 0) {
      return res.status(400).json({ mensaje: 'No se proporcionaron IDs para eliminar' });
    }

    const sql = `DELETE FROM moderadores WHERE id_moderador IN (?)`;

    db.query(sql, [ids], (err, resultado) => {
      if (err) {
        console.error('❌ Error al eliminar moderadores:', err);
        return res.status(500).json({ mensaje: 'Error al eliminar moderadores' });
      }

      res.json({
        mensaje: 'Moderadores eliminados correctamente',
        eliminados: resultado.affectedRows
      });
    });
  }
};
