const express = require('express');
const cors = require('cors');
const multer = require('multer');
const fs = require('fs');
const path = require('path');
const os = require('os');

const app = express();
const PORT = 3000;

// ================================
// 📁 Directorios base de recursos
// ================================
const userHomeDir = os.homedir();
const imagenesJuegosBasePath     = path.join(userHomeDir, 'Documents', 'recursos', 'imagenes_juegos');
const videosJuegosBasePath       = path.join(userHomeDir, 'Documents', 'recursos', 'videos_juegos');
const imagenesConsolasBasePath   = path.join(userHomeDir, 'Documents', 'recursos', 'imagenes_consolas');
const videosConsolasBasePath     = path.join(userHomeDir, 'Documents', 'recursos', 'videos_consolas');

const ensureDir = (dir) => {
  if (!fs.existsSync(dir)) fs.mkdirSync(dir, { recursive: true });
};

[
  imagenesJuegosBasePath,
  videosJuegosBasePath,
  imagenesConsolasBasePath,
  videosConsolasBasePath
].forEach(ensureDir);

// =====================================
// 🌐 Servir recursos estáticos por ruta
// =====================================
app.use('/imagenes_juegos', express.static(imagenesJuegosBasePath));
app.use('/videos_juegos', express.static(videosJuegosBasePath));
app.use('/imagenes_consolas', express.static(imagenesConsolasBasePath));
app.use('/videos_consolas', express.static(videosConsolasBasePath));

// ===================
// 🌍 Middlewares base
// ===================
app.use(cors());
app.use(express.json());

// ===================
// 📦 Multer dinámico
// ===================
const subirArchivos = multer({
  storage: multer.diskStorage({
    destination: (req, file, cb) => {
      let carpeta = 'default';
      try {
        let raw = req.body?.consola_abreviatura || req.body?.abreviatura || 'default';
        carpeta = raw.trim().toLowerCase().replace(/\s+/g, '_').replace(/[^\w\-]/g, '') || 'default';
      } catch (err) {
        console.warn('⚠️ No se pudo obtener consola_abreviatura:', err);
      }

      const isImage = file.fieldname === 'imagen';
      const isVideo = file.fieldname === 'video';

      let finalDir;
      if (req.originalUrl.includes('/juegos')) {
        finalDir = isImage
          ? path.join(imagenesJuegosBasePath, carpeta, 'boxart')
          : path.join(videosJuegosBasePath, carpeta, 'snap');
      } else {
        finalDir = isImage
          ? path.join(imagenesConsolasBasePath, carpeta)
          : path.join(videosConsolasBasePath, carpeta);
      }

      ensureDir(finalDir);
      console.log(`📂 Subiendo archivo a: ${finalDir}`);
      console.log(`📎 Nombre de archivo: ${file.originalname}`);
      cb(null, finalDir);
    },
    filename: (req, file, cb) => {
      cb(null, file.originalname);
    }
  })
});

app.locals.subirArchivos = subirArchivos;

// ===================
// 🔗 Rutas de la API
// ===================
const juegosRoutes   = require('./routes/juegos.routes');
const consolasRoutes = require('./routes/consolas.routes');
const resumenRoutes  = require('./routes/resumen.routes'); 
const logrosRoutes = require('./routes/logros.routes'); 
const estadosRoutes = require('./routes/estados.routes');
const moderadoresRoutes = require('./routes/moderadores.routes'); 
const metasTwitchRoutes = require('./routes/metastwitch.routes');


app.use('/api/juegos', juegosRoutes);
app.use('/api/consolas', consolasRoutes);
app.use('/api/resumen', resumenRoutes); 
app.use('/api/logros', logrosRoutes); 
app.use('/api/estados', estadosRoutes);
app.use('/api/moderadores', moderadoresRoutes); 
app.use('/api/metas-twitch', metasTwitchRoutes);


// ===================
// 🚀 Iniciar servidor
// ===================
app.listen(PORT, () => {
  console.log(`🚀 Servidor corriendo en http://localhost:${PORT}`);
  console.log(`🕹️ Imágenes juegos:     ${imagenesJuegosBasePath}`);
  console.log(`🎮 Imágenes consolas:   ${imagenesConsolasBasePath}`);
  console.log(`📹 Videos juegos:       ${videosJuegosBasePath}`);
  console.log(`📼 Videos consolas:     ${videosConsolasBasePath}`);
});
