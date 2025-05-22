const express = require('express');
const cors = require('cors');
const app = express();

const juegosRoutes = require('./routes/juegos.routes');
const resumenRoutes = require('./routes/resumen.routes');
const logrosRoutes = require('./routes/logros.routes'); 
const estadosRoutes = require('./routes/estados.routes'); 
const moderadoresRoutes = require('./routes/moderadores.routes'); // ✅ NUEVO

app.use(cors());
app.use(express.json());

// ✅ Rutas montadas
app.use('/api', juegosRoutes);
app.use('/api/resumen', resumenRoutes);
app.use('/api/logros', logrosRoutes); 
app.use('/api/estados', estadosRoutes);
app.use('/api/moderadores', moderadoresRoutes); // ✅ NUEVO

app.listen(3000, () => {
  console.log('✅ Servidor corriendo en http://localhost:3000');
});
