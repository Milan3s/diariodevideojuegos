const express = require('express');
const router = express.Router();
const controller = require('../controllers/metastwitch.controller');

// GET metas
router.get('/', controller.obtenerMetasTwitch);

// POST metas
router.post('/', controller.insertarMetaTwitch); // <- ESTA ES LA CLAVE

// PUT metas
router.put('/:id_meta', controller.actualizarMetaTwitch);

// DELETE múltiples metas
router.post('/eliminar-multiples', controller.eliminarMetasSeleccionadas);

// GET estados
router.get('/estados', controller.obtenerEstadosMetasTwitch);

module.exports = router;
