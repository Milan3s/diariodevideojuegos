const express = require('express');
const router = express.Router();
const db = require('../db');

router.get('/', (req, res) => {
  const tipo = req.query.tipo;

  let sql = 'SELECT id_estado, tipo, nombre FROM estados';
  const values = [];

  if (tipo) {
    sql += ' WHERE tipo = ?';
    values.push(tipo);
  }

  sql += ' ORDER BY nombre';

  db.query(sql, values, (err, resultados) => {
    if (err) {
      console.error('❌ Error al obtener estados:', err);
      return res.status(500).json({ mensaje: 'Error al obtener estados' });
    }

    res.json(resultados);
  });
});

module.exports = router;
