const userService = require('../services/user.service');

async function getMe(req, res, next) {
  try {
    const usuario = await userService.findById(req.userId);
    if (!usuario) return res.status(404).json({ mensaje: 'Usuario no encontrado' });
    res.json({ usuario });
  } catch (err) {
    next(err);
  }
}

module.exports = { getMe };
