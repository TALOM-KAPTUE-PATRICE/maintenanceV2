package com.kaptue.dev.maintenance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaptue.dev.maintenance.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}