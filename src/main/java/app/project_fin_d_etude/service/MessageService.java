package app.project_fin_d_etude.service;

import app.project_fin_d_etude.model.Message;
import app.project_fin_d_etude.repository.MessageRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Async
    public CompletableFuture<Message> save(Message message) {
        return CompletableFuture.completedFuture(messageRepository.save(message));
    }

    @Async
    public CompletableFuture<List<Message>> getAllMessages() {
        return CompletableFuture.completedFuture(messageRepository.findAllByOrderByDateEnvoiDesc());
    }

    @Async
    public CompletableFuture<List<Message>> getUnreadMessages() {
        return CompletableFuture.completedFuture(messageRepository.findByLuOrderByDateEnvoiDesc(false));
    }

    @Async
    public CompletableFuture<Void> markAsRead(Long messageId) {
        messageRepository.findById(messageId).ifPresent(message -> {
            message.setLu(true);
            messageRepository.save(message);
        });
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> delete(Long messageId) {
        messageRepository.deleteById(messageId);
        return CompletableFuture.completedFuture(null);
    }
}
