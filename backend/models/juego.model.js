const { format } = require('date-fns');

module.exports = (sequelize, DataTypes) => {
  const Juego = sequelize.define('Juego', {
    id_juegos: {
      type: DataTypes.INTEGER,
      autoIncrement: true,
      primaryKey: true
    },
    nombre: {
      type: DataTypes.STRING,
      allowNull: false
    },
    genero: {
      type: DataTypes.STRING,
      allowNull: true
    },
    editor: {
      type: DataTypes.STRING,
      allowNull: true
    },
    desarrollador: {
      type: DataTypes.STRING,
      allowNull: true
    },
    modo_juego: {
      type: DataTypes.STRING,
      allowNull: true
    },
    fecha_lanzamiento: {
      type: DataTypes.DATE,
      allowNull: true,
      get() {
        const rawDate = this.getDataValue('fecha_lanzamiento');
        return rawDate ? format(rawDate, 'dd-MM-yyyy') : null;
      }
    },
    imagen: {
      type: DataTypes.STRING,
      allowNull: true
    },
    es_recomendado: {
      type: DataTypes.BOOLEAN, // Mejor como booleano
      allowNull: false,
      defaultValue: false
    },
    id_estado: {
      type: DataTypes.INTEGER,
      allowNull: true,
      references: {
        model: 'estados',
        key: 'id_estado'
      }
    },
    region: {
      type: DataTypes.STRING,
      allowNull: true
    },
    caja_original: {
      type: DataTypes.BOOLEAN,
      allowNull: false,
      defaultValue: false
    },
    precintado: {
      type: DataTypes.BOOLEAN,
      allowNull: false,
      defaultValue: false
    },
    fecha_creacion_registro: {
      type: DataTypes.DATE,
      allowNull: true,
      defaultValue: DataTypes.NOW
    }
  }, {
    tableName: 'juegos',
    timestamps: false
  });

  return Juego;
};
