const express = require('express');
const router = express.Router();
const controladorModeradores = require('../controllers/moderadores.controller');

// Obtener todos los moderadores
router.get('/', controladorModeradores.obtenerModeradores);

// Agregar un nuevo moderador
router.post('/', controladorModeradores.agregarModerador);

// Dar de alta un moderador (actualiza fecha_alta y limpia fecha_baja)
router.put('/alta/:id', controladorModeradores.darDeAltaModerador);

// Actualizar datos de un moderador
router.put('/:id', controladorModeradores.actualizarModerador);

// Eliminar uno o varios moderadores
router.post('/eliminar', controladorModeradores.eliminarModeradores);

module.exports = router;
