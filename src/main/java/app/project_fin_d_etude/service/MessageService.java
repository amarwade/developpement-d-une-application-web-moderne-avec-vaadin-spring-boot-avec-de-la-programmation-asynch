package app.project_fin_d_etude.service;

import app.project_fin_d_etude.model.Message;
import app.project_fin_d_etude.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAllByOrderByDateEnvoiDesc();
    }

    public List<Message> getUnreadMessages() {
        return messageRepository.findByLuOrderByDateEnvoiDesc(false);
    }

    public void markAsRead(Long messageId) {
        messageRepository.findById(messageId).ifPresent(message -> {
            message.setLu(true);
            messageRepository.save(message);
        });
    }

    public void delete(Long messageId) {
        messageRepository.deleteById(messageId);
    }
}
