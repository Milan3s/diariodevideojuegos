const os = require('os');
const fs = require('fs');
const path = require('path');
const multer = require('multer');

// Rutas base para consolas y juegos
const userHomeDir = os.homedir();
const imagenesJuegosBasePath = path.join(userHomeDir, 'Documents', 'recursos', 'imagenes_juegos');
const videosJuegosBasePath = path.join(userHomeDir, 'Documents', 'recursos', 'videos_juegos');
const imagenesConsolasBasePath = path.join(userHomeDir, 'Documents', 'recursos', 'imagenes_consolas');
const videosConsolasBasePath = path.join(userHomeDir, 'Documents', 'recursos', 'videos_consolas');

// Asegura que la carpeta exista
const ensureDir = (dir) => {
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir, { recursive: true });
  }
};

const subirArchivos = multer({
  storage: multer.diskStorage({
    destination: (req, file, cb) => {
      let consola = '';

      if (req.body && req.body.consola_abreviatura) {
        consola = String(req.body.consola_abreviatura).trim().toLowerCase();
      } else {
        consola = 'default';
      }

      consola = consola.replace(/\s+/g, '_').replace(/[^\w\-]/g, '') || 'default';

      console.log(`📦 Usando carpeta de consola: ${consola}`);

      const isImage = file.fieldname === 'imagen';
      const isVideo = file.fieldname === 'video';

      let basePath;
      if (req.originalUrl.includes('/juegos')) {
        basePath = isImage
          ? path.join(imagenesJuegosBasePath, consola, 'boxart')
          : path.join(videosJuegosBasePath, consola, 'snap');
      } else {
        basePath = isImage
          ? path.join(imagenesConsolasBasePath, consola)
          : path.join(videosConsolasBasePath, consola);
      }

      ensureDir(basePath);
      cb(null, basePath);
    },

    filename: (req, file, cb) => {
      cb(null, file.originalname);
    }
  })
});

module.exports = {
  imagenesBasePath: imagenesConsolasBasePath,
  videosBasePath: videosConsolasBasePath,
  subirArchivos
};
