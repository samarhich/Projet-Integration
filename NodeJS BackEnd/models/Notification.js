const mongoose = require('mongoose');

const notificationSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: false },
  providerId: { type: mongoose.Schema.Types.ObjectId, ref: 'Provider', required: true },
  message: { type: String, required: true },
  type: { type: String, required: true }, // Exemple : 'NEW_RESERVATION', 'RESERVATION_RESPONSE'
  isRead: { type: Boolean, default: false },
}, {
  timestamps: true, // Ajouter createdAt et updatedAt
});

module.exports = mongoose.model('Notification', notificationSchema);
