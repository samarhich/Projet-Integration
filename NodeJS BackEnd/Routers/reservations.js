const express = require('express');
const mongoose = require('mongoose');
const Reservation = require('../models/Reservation'); // Modèle Reservation
const Notification = require('../models/Notification'); // Modèle Notification
const User = require('../models/User'); // Modèle User
const Provider = require('../models/Provider'); // Modèle Provider
const Service = require('../models/Service'); // Modèle Service

const router = express.Router();


/**
 * 2. Route : Faire une réservation.
 * - Paramètres : userId, providerId, serviceId, date (via req.body)
 */
router.post('/reserve', async (req, res) => {
  try {
    const { userId, providerId, serviceId, date } = req.body;

    // Créer une nouvelle réservation
    const reservation = new Reservation({
      userId,
      providerId,
      serviceId,
      date,
      status: 'PENDING', // Par défaut
    });

    // Sauvegarder la réservation dans la base de données
    const savedReservation = await reservation.save();

    // Créer une notification pour le fournisseur
    const notification = new Notification({
      providerId,
      message: `Nouvelle réservation de l'utilisateur ${userId} pour le service ${serviceId}.`,
      type: 'NEW_RESERVATION',
      isRead: false,
    });
    await notification.save();

    res.status(201).json({
      success: true,
      message: "Réservation effectuée avec succès et notification envoyée au fournisseur.",
      reservation: savedReservation,
    });
  } catch (error) {
    res.status(500).json({ success: false, message: "Erreur lors de la création de la réservation.", error });
  }
});

/**
 * 3. Route : Répondre à une réservation (CONFIRMER ou ANNULER).
 * - Paramètres : reservationId, status (via req.body)
 */
router.put('/:reservationId/respond', async (req, res) => {
  try {
    const { reservationId } = req.params;
    const { status } = req.body; // "CONFIRMED" ou "CANCELLED"

    if (!['CONFIRMED', 'CANCELLED'].includes(status)) {
      return res.status(400).json({
        success: false,
        message: 'Le statut doit être "CONFIRMED" ou "CANCELLED".',
      });
    }

    // Mettre à jour le statut de la réservation
    const updatedReservation = await Reservation.findByIdAndUpdate(
      reservationId,
      { status },
      { new: true }
    );

    if (!updatedReservation) {
      return res.status(404).json({
        success: false,
        message: "Réservation introuvable.",
      });
    }

    // Créer une notification pour l'utilisateur
    const notification = new Notification({
      userId: updatedReservation.userId,
      message: `Votre réservation pour le service ${updatedReservation.serviceId} a été ${status.toLowerCase()}.`,
      type: 'RESERVATION_RESPONSE',
      isRead: false,
    });
    await notification.save();

    res.status(200).json({
      success: true,
      message: `Réservation ${status.toLowerCase()} avec succès.`,
      reservation: updatedReservation,
    });
  } catch (error) {
    res.status(500).json({ success: false, message: "Erreur lors de la mise à jour de la réservation.", error });
  }
});

/**
 * 4. Route : Récupérer les notifications pour un provider.
 * - Paramètre : providerId (via req.params.providerId)
 */
router.get('/provider/:providerId/notifications', async (req, res) => {
  try {
    const providerId = req.params.providerId;

    // Récupérer les notifications du fournisseur
    const notifications = await Notification.find({ providerId })
      .sort({ createdAt: -1 }) // Trier par date de création
      .exec();

    res.status(200).json({
      success: true,
      message: "Notifications récupérées avec succès.",
      notifications,
    });
  } catch (error) {
    res.status(500).json({ success: false, message: "Erreur lors de la récupération des notifications.", error });
  }
});

module.exports = router;
