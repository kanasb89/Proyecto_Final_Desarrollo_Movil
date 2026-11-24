const router = require('express').Router();
const { body } = require('express-validator');
const validate = require('../middlewares/validate.middleware');
const { register, login } = require('../controllers/auth.controller');

const correoRule = body('correo')
  .trim().toLowerCase()
  .isEmail().withMessage('Correo electrónico inválido');

router.post(
  '/register',
  [
    body('nombreCompleto').trim().notEmpty().withMessage('El nombre es obligatorio'),
    correoRule,
    body('password').isLength({ min: 6 }).withMessage('La contraseña debe tener al menos 6 caracteres'),
  ],
  validate,
  register
);

router.post(
  '/login',
  [correoRule, body('password').notEmpty().withMessage('La contraseña es obligatoria')],
  validate,
  login
);

module.exports = router;
