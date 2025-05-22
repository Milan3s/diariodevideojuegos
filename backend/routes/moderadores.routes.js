const express = require('express');
const router = express.Router();
const controladorModeradores = require('../controllers/moderadores.controller');

router.get('/', controladorModeradores.obtenerModeradores);
router.post('/', controladorModeradores.agregarModerador);
router.put('/alta/:id', controladorModeradores.agregarAltaModerador);

module.exports = router;
