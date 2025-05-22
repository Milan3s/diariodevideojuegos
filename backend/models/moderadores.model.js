const { format } = require('date-fns');

module.exports = (sequelize, DataTypes) => {
  const Moderador = sequelize.define('Moderador', {
    id_moderador: {
      type: DataTypes.INTEGER,
      autoIncrement: true,
      primaryKey: true
    },
    nombre: {
      type: DataTypes.STRING,
      allowNull: false
    },
    email: {
      type: DataTypes.STRING,
      allowNull: false,
      validate: {
        isEmail: true
      }
    },
    fecha_alta: {
      type: DataTypes.DATE,
      allowNull: true,
      get() {
        const rawDate = this.getDataValue('fecha_alta');
        return rawDate ? format(rawDate, 'dd-MM-yyyy') : null;
      }
    },
    fecha_baja: {
      type: DataTypes.DATE,
      allowNull: true,
      get() {
        const rawDate = this.getDataValue('fecha_baja');
        return rawDate ? format(rawDate, 'dd-MM-yyyy') : null;
      }
    },
    id_estado: {
      type: DataTypes.INTEGER,
      allowNull: true,
      references: {
        model: 'estados',
        key: 'id_estado'
      }
    }
  }, {
    tableName: 'moderadores',
    timestamps: false
  });

  return Moderador;
};
