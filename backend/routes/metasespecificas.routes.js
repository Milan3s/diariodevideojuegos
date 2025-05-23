// routes/metasEspecificas.routes.js

const express = require('express');
const router = express.Router();
const metasController = require('../controllers/metasEspecificas.controller');

// ==========================
// Rutas para obtener datos
// ==========================

// Obtener todas las metas específicas
router.get('/', metasController.obtenerMetas);

// ==========================
// Exportar router
// ==========================
module.exports = router;