const db = require('../db');
const { format } = require('date-fns');

module.exports = {
  obtenerResumenDelDia: (req, res) => {
    const queries = [
      { key: 'totalConsolas',       sql: 'SELECT COUNT(id_consola) AS valor FROM consolas' },
      { key: 'totalJuegos',         sql: 'SELECT COUNT(id_juegos) AS valor FROM juegos' },
      { key: 'totalLogros',         sql: 'SELECT COUNT(id_logro) AS valor FROM logros' },
      { key: 'totalMetasEspecificas', sql: 'SELECT COUNT(id_meta_especifica) AS valor FROM metas_especificas' },
      { key: 'totalMetasJuegos',    sql: 'SELECT COUNT(id_meta_juegos) AS valor FROM metas_juegos' },
      { key: 'totalMetasTwitch',    sql: 'SELECT COUNT(id_meta) AS valor FROM metas_twitch' },
      { key: 'totalMejorasCanal',   sql: 'SELECT COUNT(id_mejora) AS valor FROM mejoras_canal' }
    ];

    const resultados = {};
    let pendientes = queries.length;
    let errorOcurrido = false;

    queries.forEach(({ key, sql }) => {
      db.query(sql, (err, result) => {
        if (errorOcurrido) return; // Ya hubo error

        if (err) {
          errorOcurrido = true;
          console.error(`❌ Error en ${key}:`, err);
          return res.status(500).json({ mensaje: `Error en ${key}` });
        }

        resultados[key] = result[0]?.valor || 0;

        pendientes--;
        if (pendientes === 0) {
          res.json({
            fecha: format(new Date(), 'yyyy-MM-dd'),
            ...resultados
          });
        }
      });
    });
  }
};
