const express = require('express');
const router = express.Router();
const controlador = require('../controllers/metastwitch.controller');

// ✅ Obtener todas las metas de Twitch
router.get('/', controlador.obtenerMetasTwitch);

// ✅ Obtener estados de metas para Twitch
router.get('/estados', controlador.obtenerEstadosMetasTwitch);

module.exports = router;
