const express = require('express');
const cors = require('cors');
const app = express();

const juegosRoutes = require('./routes/juegos.routes');
const resumenRoutes = require('./routes/resumen.routes');
const logrosRoutes = require('./routes/logros.routes'); // ✅ Importar rutas de logros
const estadosRoutes = require('./routes/estados.routes'); // ✅ Nueva ruta de estados

app.use(cors());
app.use(express.json());

// ✅ Rutas montadas
app.use('/api', juegosRoutes);
app.use('/api/resumen', resumenRoutes);
app.use('/api/logros', logrosRoutes); // ✅ Nueva ruta para logros
app.use('/api/estados', estadosRoutes); // ✅ Ruta activa


app.listen(3000, () => {
  console.log('✅ Servidor corriendo en http://localhost:3000');
});
