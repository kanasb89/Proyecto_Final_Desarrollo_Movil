const jwt = require('jsonwebtoken');
const userService = require('../services/user.service');

async function register(req, res, next) {
  try {
    const { nombreCompleto, correo, password } = req.body;

    if (await userService.findByEmail(correo)) {
      return res.status(409).json({ mensaje: 'El correo ya está registrado' });
    }
    const usuario = await userService.createUser({ nombreCompleto, correo, password });
    res.status(201).json({ mensaje: 'Usuario registrado correctamente', usuario });
  } catch (err) {
    if (err.code === '23505') { // violación de UNIQUE (condición de carrera)
      return res.status(409).json({ mensaje: 'El correo ya está registrado' });
    }
    next(err);
  }
}

async function login(req, res, next) {
  try {
    const { correo, password } = req.body;
    const usuario = await userService.findByEmail(correo);

    const valido = usuario && (await userService.verifyPassword(password, usuario.passwordHash));
    if (!valido) {
      return res.status(401).json({ mensaje: 'Correo o contraseña incorrectos' });
    }

    const token = jwt.sign({ sub: usuario.id }, process.env.JWT_SECRET, { expiresIn: '7d' });
    const { passwordHash, ...usuarioPublico } = usuario;
    res.json({ token, usuario: usuarioPublico });
  } catch (err) {
    next(err);
  }
}

module.exports = { register, login };
