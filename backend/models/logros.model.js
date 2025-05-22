const { format } = require('date-fns');

module.exports = (sequelize, DataTypes) => {
  const Logro = sequelize.define('Logro', {
    id_logro: {
      type: DataTypes.INTEGER,
      autoIncrement: true,
      primaryKey: true
    },
    nombre: {
      type: DataTypes.STRING,
      allowNull: false
    },
    descripcion: {
      type: DataTypes.TEXT,
      allowNull: true
    },
    horas_estimadas: {
      type: DataTypes.INTEGER,
      allowNull: true
    },
    anio: {
      type: DataTypes.INTEGER,
      allowNull: true
    },
    fecha_inicio: {
      type: DataTypes.DATE,
      allowNull: true,
      get() {
        const rawDate = this.getDataValue('fecha_inicio');
        return rawDate ? format(rawDate, 'yyyy-MM-dd') : null;
      }
    },
    fecha_fin: {
      type: DataTypes.DATE,
      allowNull: true,
      get() {
        const rawDate = this.getDataValue('fecha_fin');
        return rawDate ? format(rawDate, 'yyyy-MM-dd') : null;
      }
    },
    intentos: {
      type: DataTypes.INTEGER,
      allowNull: true
    },
    creditos: {
      type: DataTypes.INTEGER,
      allowNull: true
    },
    puntuacion: {
      type: DataTypes.INTEGER,
      allowNull: true
    },
    fecha_registro: {
      type: DataTypes.DATE,
      allowNull: true,
      defaultValue: DataTypes.NOW,
      get() {
        const rawDate = this.getDataValue('fecha_registro');
        return rawDate ? format(rawDate, 'yyyy-MM-dd') : null;
      }
    },
    id_juego: {
      type: DataTypes.INTEGER,
      allowNull: true,
      references: {
        model: 'juegos',
        key: 'id_juegos'
      }
    },
    id_estado: {
      type: DataTypes.INTEGER,
      allowNull: true,
      references: {
        model: 'estados',
        key: 'id_estado'
      }
    },
    id_dificultad: {
      type: DataTypes.INTEGER,
      allowNull: true,
      references: {
        model: 'dificultades_logros',
        key: 'id_dificultad'
      }
    },
    id_consola: {
      type: DataTypes.INTEGER,
      allowNull: true,
      references: {
        model: 'consolas',
        key: 'id_consola'
      }
    }
  }, {
    tableName: 'logros',
    timestamps: false
  });

  return Logro;
};
