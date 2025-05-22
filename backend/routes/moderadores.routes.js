const express = require('express');
const router = express.Router();
const moderadoresController = require('../controllers/moderadores.controller');

// Solo mostrar moderadores
router.get('/', moderadoresController.obtenerModeradores);

module.exports = router;
