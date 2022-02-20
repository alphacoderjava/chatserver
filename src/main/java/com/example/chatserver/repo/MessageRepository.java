package com.example.chatserver.repo;

import com.example.chatserver.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    HashSet<Message> findAllBySenderName(String senderNme);
}
