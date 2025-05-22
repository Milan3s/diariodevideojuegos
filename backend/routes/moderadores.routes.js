const express = require('express');
const router = express.Router();
const controladorModeradores = require('../controllers/moderadores.controller');

// ✅ Ruta GET para obtener moderadores
router.get('/', controladorModeradores.obtenerModeradores);

// ✅ Ruta POST para insertar un nuevo moderador
router.post('/', controladorModeradores.agregarModerador); // <-- ESTA ES LA QUE FALTABA O ESTABA MAL

module.exports = router;
