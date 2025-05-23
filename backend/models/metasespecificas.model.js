// models/metasEspecificas.model.js

const { format } = require('date-fns');

module.exports = (sequelize, DataTypes) => {
  const MetaEspecifica = sequelize.define('MetaEspecifica', {
    id_meta_especifica: {
      type: DataTypes.INTEGER,
      autoIncrement: true,
      primaryKey: true
    },
    descripcion: {
      type: DataTypes.STRING,
      allowNull: false
    },
    juegos_objetivo: {
      type: DataTypes.INTEGER,
      allowNull: false
    },
    juegos_completados: {
      type: DataTypes.INTEGER,
      allowNull: false,
      defaultValue: 0
    },
    fabricante: {
      type: DataTypes.STRING,
      allowNull: true
    },
    id_consola: {
      type: DataTypes.INTEGER,
      allowNull: true,
      references: {
        model: 'consolas',
        key: 'id_consola'
      }
    },
    fecha_inicio: {
      type: DataTypes.DATEONLY,
      allowNull: true,
      get() {
        const value = this.getDataValue('fecha_inicio');
        return value ? format(new Date(value), 'yyyy-MM-dd') : null;
      }
    },
    fecha_fin: {
      type: DataTypes.DATEONLY,
      allowNull: true,
      get() {
        const value = this.getDataValue('fecha_fin');
        return value ? format(new Date(value), 'yyyy-MM-dd') : null;
      }
    },
    fecha_registro: {
      type: DataTypes.DATE,
      allowNull: false,
      defaultValue: DataTypes.NOW
    },
    id_estado_metas: {
      type: DataTypes.INTEGER,
      allowNull: false,
      references: {
        model: 'estado_metas',
        key: 'id'
      }
    }
  }, {
    tableName: 'metas_especificas',
    timestamps: false
  });

  return MetaEspecifica;
};
