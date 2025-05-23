const { format } = require('date-fns');

module.exports = (sequelize, DataTypes) => {
  const MetaTwitch = sequelize.define('MetaTwitch', {
    id_meta: {
      type: DataTypes.INTEGER,
      autoIncrement: true,
      primaryKey: true
    },
    descripcion: {
      type: DataTypes.STRING,
      allowNull: false
    },
    meta: {
      type: DataTypes.INTEGER,
      allowNull: false
    },
    actual: {
      type: DataTypes.INTEGER,
      allowNull: false,
      defaultValue: 0
    },
    mes: {
      type: DataTypes.INTEGER,
      get() {
        return this.getDataValue('mes');
      }
    },
    anio: {
      type: DataTypes.INTEGER,
      get() {
        return this.getDataValue('anio');
      }
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
    fecha_registro: {
      type: DataTypes.DATE,
      allowNull: false,
      defaultValue: DataTypes.NOW,
      get() {
        const rawDate = this.getDataValue('fecha_registro');
        return rawDate ? format(rawDate, 'yyyy-MM-dd HH:mm:ss') : null;
      }
    },
    id_estado_metas: {
      type: DataTypes.INTEGER,
      allowNull: true,
      references: {
        model: 'estado_metas',
        key: 'id'
      }
    }
  }, {
    tableName: 'metas_twitch',
    timestamps: false
  });

  return MetaTwitch;
};
