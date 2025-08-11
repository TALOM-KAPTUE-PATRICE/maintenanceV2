// src/app/models/notification.model.ts

export interface Notification {
    id: number;          // ID de la notification
    message: string;    // Le message de la notification
    date: string;       // Date de création de la notification au format ISO ou autre
    userId: number;     // ID de l'utilisateur qui a reçu la notification
  }