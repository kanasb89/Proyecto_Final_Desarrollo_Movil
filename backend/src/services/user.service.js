const bcrypt = require('bcrypt');
const pool = require('../config/db');

const SALT_ROUNDS = 10;

async function createUser({ nombreCompleto, correo, password }) {
  const passwordHash = await bcrypt.hash(password, SALT_ROUNDS);
  const { rows } = await pool.query(
    `INSERT INTO usuarios (nombre_completo, correo, password_hash)
     VALUES ($1, $2, $3)
     RETURNING id, nombre_completo AS "nombreCompleto", correo`,
    [nombreCompleto, correo, passwordHash]
  );
  return rows[0];
}

async function findByEmail(correo) {
  const { rows } = await pool.query(
    `SELECT id, nombre_completo AS "nombreCompleto", correo, password_hash AS "passwordHash"
     FROM usuarios WHERE correo = $1`,
    [correo]
  );
  return rows[0] || null;
}

async function findById(id) {
  const { rows } = await pool.query(
    `SELECT id, nombre_completo AS "nombreCompleto", correo
     FROM usuarios WHERE id = $1`,
    [id]
  );
  return rows[0] || null;
}

const verifyPassword = (plain, hash) => bcrypt.compare(plain, hash);

module.exports = { createUser, findByEmail, findById, verifyPassword };
