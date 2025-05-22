const express = require('express');
const router = express.Router();
const juegosController = require('../controllers/juegos.controller');
const { subirArchivos } = require('../controllers/ruta.controller');

// ==========================
// Rutas para obtener datos
// ==========================

// Obtener todos los juegos
router.get('/', juegosController.obtenerJuegos);

// Obtener todos los estados
router.get('/estados', juegosController.obtenerEstados);

// Obtener todas las consolas
router.get('/consolas', juegosController.obtenerConsolas);

// ==========================
// Rutas de creación y edición
// ==========================

// Crear un nuevo juego con archivos
router.post(
  '/',
  subirArchivos.fields([
    { name: 'imagen', maxCount: 1 },
    { name: 'video', maxCount: 1 },
    { name: 'consola_abreviatura', maxCount: 1 } // Necesario para que multer lo lea aunque no se guarde
  ]),
  juegosController.agregarJuego
);

// Actualizar un juego existente con archivos
router.put(
  '/:id',
  subirArchivos.fields([
    { name: 'imagen', maxCount: 1 },
    { name: 'video', maxCount: 1 }
  ]),
  juegosController.actualizarJuego
);

// ==========================
// Rutas de eliminación
// ==========================

// Eliminar un solo juego por ID
router.delete('/:id', juegosController.eliminarJuego);

// Eliminar múltiples juegos
router.post('/eliminar-multiples', juegosController.eliminarVariosJuegos);


// ==========================
// Exportar router
// ==========================
module.exports = router;
