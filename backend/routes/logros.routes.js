const express = require('express');
const router = express.Router();

const logrosController = require('../controllers/logros.controller');

// Obtener todos los logros
router.get('/', logrosController.obtenerLogros);

// Obtener estados filtrados o todos
router.get('/estados', logrosController.obtenerEstados);

// Obtener juegos para el selector
router.get('/juegos', logrosController.obtenerJuegos);

// Obtener dificultades para el selector
router.get('/dificultades', logrosController.obtenerDificultades);

// Obtener consolas para el selector
router.get('/consolas', logrosController.obtenerConsolas);

// Agregar un nuevo logro
router.post('/', logrosController.agregarLogro);

// Eliminar múltiples logros
router.delete('/', logrosController.eliminarMultiplesLogros);

// Actualizar un logro existente
router.put('/:id', logrosController.actualizarLogro);

// Eliminar un logro por ID
router.delete('/:id', logrosController.eliminarLogro);

// ✅ Eliminar múltiples logros correctamente (vía POST)
router.post('/eliminar-multiples', logrosController.eliminarMultiplesLogros);

module.exports = router;
