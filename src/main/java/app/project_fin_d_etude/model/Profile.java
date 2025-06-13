package app.project_fin_d_etude.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité représentant le profil d'un utilisateur. Chaque utilisateur possède un
 * et un seul profil.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Courte biographie de l'utilisateur.
     */
    @Column(length = 500) // Exemple de limite de longueur
    private String bio;

    /**
     * Description détaillée du profil ou des intérêts de l'utilisateur.
     */
    @Column(length = 2000) // Exemple de limite de longueur
    private String description;

    /**
     * L'utilisateur associé à ce profil. Lien One-to-One: un profil est associé
     * à un utilisateur unique, et vice-versa.
     */
    @OneToOne
    @JoinColumn(name = "utilisateur_id", nullable = false, unique = true)
    private Utilisateur utilisateur;

    /**
     * Constructeur pour créer un profil avec un utilisateur associé.
     *
     * @param utilisateur L'utilisateur auquel ce profil est lié.
     */
    public Profile(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}
