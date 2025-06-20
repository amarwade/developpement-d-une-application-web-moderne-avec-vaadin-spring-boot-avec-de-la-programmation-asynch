package app.project_fin_d_etude.service;

import app.project_fin_d_etude.model.Message;
import app.project_fin_d_etude.repository.MessageRepository;
import app.project_fin_d_etude.utils.EntityValidator;
import app.project_fin_d_etude.utils.ExceptionHandler;
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
        // Validation de l'entité avant sauvegarde
        EntityValidator.ValidationResult validationResult = EntityValidator.validateMessage(message);
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException("Message invalide: " + validationResult.getAllErrorsAsString());
        }

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
        if (messageId == null) {
            throw new IllegalArgumentException("L'ID du message ne peut pas être null");
        }

        messageRepository.findById(messageId).ifPresent(message -> {
            message.setLu(true);
            messageRepository.save(message);
        });
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> delete(Long messageId) {
        if (messageId == null) {
            throw new IllegalArgumentException("L'ID du message ne peut pas être null");
        }

        messageRepository.deleteById(messageId);
        return CompletableFuture.completedFuture(null);
    }
}
