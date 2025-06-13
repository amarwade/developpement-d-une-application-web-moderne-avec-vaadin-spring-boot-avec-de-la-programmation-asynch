package app.project_fin_d_etude.presenter;

import app.project_fin_d_etude.model.Message;
import app.project_fin_d_etude.service.MessageService;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessagePresenter {

    @Setter
    private MessageView view;

    private final MessageService messageService;

    public interface MessageView {

        void afficherMessages(List<Message> messages);

        void afficherMessage(String message);

        void afficherErreur(String erreur);
    }

    public MessagePresenter(MessageService messageService) {
        this.messageService = messageService;
    }

    public void envoyerMessage(Message message) {
        try {
            messageService.save(message);
            if (view != null) {
                view.afficherMessage("Message envoyé avec succès !");
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de l'envoi du message : " + e.getMessage());
            }
            throw e;
        }
    }

    public void chargerMessages() {
        try {
            if (view != null) {
                List<Message> messages = messageService.getAllMessages();
                view.afficherMessages(messages);
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors du chargement des messages : " + e.getMessage());
            }
        }
    }

    public void marquerCommeLu(Long messageId) {
        try {
            messageService.markAsRead(messageId);
            if (view != null) {
                view.afficherMessage("Message marqué comme lu");
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors du marquage du message : " + e.getMessage());
            }
        }
    }

    public void supprimerMessage(Long messageId) {
        try {
            messageService.delete(messageId);
            if (view != null) {
                view.afficherMessage("Message supprimé avec succès");
            }
        } catch (Exception e) {
            if (view != null) {
                view.afficherErreur("Erreur lors de la suppression du message : " + e.getMessage());
            }
        }
    }
}
