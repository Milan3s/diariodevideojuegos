const { format } = require('date-fns');

module.exports = (sequelize, DataTypes) => {
  const Consola = sequelize.define('Consola', {
    id_consola: {
      type: DataTypes.INTEGER,
      autoIncrement: true,
      primaryKey: true
    },
    nombre: {
      type: DataTypes.STRING,
      allowNull: true
    },
    abreviatura: {
      type: DataTypes.STRING,
      allowNull: true
    },
    anio: {
      type: DataTypes.INTEGER,
      allowNull: true
    },
    fabricante: {
      type: DataTypes.STRING,
      allowNull: true
    },
    generacion: {
      type: DataTypes.STRING,
      allowNull: true
    },
    region: {
      type: DataTypes.STRING,
      allowNull: true
    },
    tipo: {
      type: DataTypes.STRING,
      allowNull: true
    },
    procesador: {
      type: DataTypes.STRING,
      allowNull: true
    },
    memoria: {
      type: DataTypes.STRING,
      allowNull: true
    },
    almacenamiento: {
      type: DataTypes.STRING,
      allowNull: true
    },
    frecuencia_mhz: {
      type: DataTypes.INTEGER,
      allowNull: true
    },
    chip: {
      type: DataTypes.BOOLEAN,
      allowNull: true
    },
    caracteristicas: {
      type: DataTypes.TEXT,
      allowNull: true
    },
    fecha_lanzamiento: {
      type: DataTypes.DATE,
      allowNull: true,
      get() {
        const raw = this.getDataValue('fecha_lanzamiento');
        return raw ? format(raw, 'dd-MM-yyyy') : null;
      }
    },
    imagen: {
      type: DataTypes.STRING,
      allowNull: true
    },
    imagen_sistema_votos: {
      type: DataTypes.STRING,
      allowNull: true
    },
    id_estado: {
      type: DataTypes.INTEGER,
      allowNull: true,
      references: {
        model: 'estados',
        key: 'id_estado'
      }
    },
    original: {
      type: DataTypes.BOOLEAN,
      allowNull: true,
      defaultValue: false
    },
    modificada: {
      type: DataTypes.BOOLEAN,
      allowNull: true,
      defaultValue: false
    },
    caja: {
      type: DataTypes.BOOLEAN,
      allowNull: true,
      defaultValue: false
    },
    precintada: {
      type: DataTypes.BOOLEAN,
      allowNull: true,
      defaultValue: false
    },
    hz: {
      type: DataTypes.STRING,
      allowNull: true
    }
  }, {
    tableName: 'consolas',
    timestamps: false
  });

  return Consola;
};
