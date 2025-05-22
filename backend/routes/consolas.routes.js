const express = require('express');
const router = express.Router();
const consolasController = require('../controllers/consolas.controller');
const { subirArchivos } = require('../controllers/ruta.controller');

// GET
router.get('/', consolasController.obtenerConsolas);
router.get('/estados', consolasController.obtenerEstadosConsolas);

// POST
router.post(
  '/',
  subirArchivos.fields([
    { name: 'imagen', maxCount: 1 },
    { name: 'video', maxCount: 1 },
    { name: 'consola_abreviatura', maxCount: 1 }
  ]),
  consolasController.agregarConsola
);

router.post('/eliminar-multiples', consolasController.eliminarVariasConsolas);


// PUT
router.put(
  '/:id',
  subirArchivos.fields([
    { name: 'imagen', maxCount: 1 },
    { name: 'video', maxCount: 1 },
    { name: 'consola_abreviatura', maxCount: 1 }
  ]),
  consolasController.actualizarConsola
);

// DELETE
router.delete('/:id', consolasController.eliminarConsola);


module.exports = router;
