const express = require('express');
const router = express.Router();
const controladorModeradores = require('../controllers/moderadores.controller');

// ✅ Obtener todos los moderadores
router.get('/', controladorModeradores.obtenerModeradores);

// ✅ Agregar un nuevo moderador
router.post('/', controladorModeradores.agregarModerador);

// ✅ Dar de alta un moderador (pone fecha_alta y borra fecha_baja)
router.put('/alta/:id', controladorModeradores.darDeAltaModerador);

// ✅ Dar de baja un moderador (pone fecha_baja con la fecha actual)
router.put('/baja/:id', controladorModeradores.darDeBajaModerador);

// ✅ Readmitir un moderador (borra fecha_baja)
router.put('/readmitir/:id', controladorModeradores.readmitirModerador);

// ✅ Actualizar datos de un moderador (nombre, email, estado)
router.put('/:id', controladorModeradores.actualizarModerador);

// ✅ Eliminar uno o varios moderadores
router.post('/eliminar', controladorModeradores.eliminarModeradores);

module.exports = router;
