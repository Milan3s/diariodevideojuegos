const net = require('net');
const mysql = require('mysql2');

const db = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '',
  database: 'mi_diario'
});

// Verificar que MySQL esté en línea antes de conectar
const socket = new net.Socket();
socket.setTimeout(2000);

socket.on('connect', () => {
  console.log('✅ MySQL está activo en el puerto 3306. Intentando conectar...');

  db.connect(err => {
    if (err) {
      console.error('❌ Error al conectar a MySQL:', err.message);
      process.exit(1);
    }
    console.log('✅ Conexión a MySQL establecida');
  });

  socket.destroy();
});

socket.on('timeout', () => {
  console.error('❌ ERROR: No se pudo verificar MySQL (tiempo agotado).');
  socket.destroy();
  process.exit(1);
});

socket.on('error', () => {
  console.error('❌ ERROR: MySQL no está iniciado. Por favor, abre XAMPP y enciende MySQL.');
  process.exit(1);
});

socket.connect(3306, '127.0.0.1');

module.exports = db;
