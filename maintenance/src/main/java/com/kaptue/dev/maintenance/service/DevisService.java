package com.kaptue.dev.maintenance.service;

import com.kaptue.dev.maintenance.controller.dto.*;
import com.kaptue.dev.maintenance.controller.request.CreateDevisRequest;
import com.kaptue.dev.maintenance.controller.request.UpdateDevisRequest;
import com.kaptue.dev.maintenance.controller.response.DevisArticleResponseDTO;
import com.kaptue.dev.maintenance.controller.response.DevisResponseDTO;
import com.kaptue.dev.maintenance.entity.Article;
import com.kaptue.dev.maintenance.entity.Categorie;
import com.kaptue.dev.maintenance.entity.Client;
import com.kaptue.dev.maintenance.entity.Devis;
import com.kaptue.dev.maintenance.entity.enums.DevisStatus;
import com.kaptue.dev.maintenance.exception.BadRequestException;
import com.kaptue.dev.maintenance.exception.ResourceNotFoundException;
import com.kaptue.dev.maintenance.repository.ArticleRepository;
import com.kaptue.dev.maintenance.repository.ClientRepository;
import com.kaptue.dev.maintenance.repository.DevisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DevisService {

    @Autowired
    private DevisRepository devisRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ClientRepository clientRepository; // 
    private static final Logger logger = LoggerFactory.getLogger(DevisService.class);
    // Injectez un service pour convertir les nombres en lettres si vous en avez un

    @Transactional
    public DevisResponseDTO createDevis(CreateDevisRequest request) {

        // ▼▼▼ AJOUTEZ CETTE LOGIQUE ▼▼▼
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + request.getClientId()));
        Devis devis = new Devis();

        // Mapper les champs de la requête vers l'entité
        devis.setDescription(request.getDescription());
        devis.setDateValidite(request.getDateValidite());
        devis.setTypeTravaux(request.getTypeTravaux());
        devis.setEffectif(request.getEffectif());
        devis.setSiteIntervention(request.getSiteIntervention());
        devis.setPeinture(request.getPeinture());
        devis.setContrainte(request.getContrainte());
        devis.setLivraison(request.getLivraison());
        devis.setDevise(request.getDevise());
        devis.setClient(client);
        // ... autres mappings

        devis.setDateCreation(LocalDate.now());
        devis.setDateUpdateDevis(LocalDate.now());
        devis.setId(generateNextId()); // Logique d'ID améliorée

        Devis savedDevis = devisRepository.save(devis);
        return DevisResponseDTO.fromEntity(savedDevis);
    }

    @Transactional(readOnly = true)
    public List<DevisResponseDTO> getAllDevis() {
        return devisRepository.findAll().stream()
                .map(DevisResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DevisResponseDTO getDevisById(String id) {
        Devis devis = devisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Devis non trouvé avec l'ID: " + id));
        return DevisResponseDTO.fromEntity(devis);
    }

    // ... méthodes update et delete à refactoriser de manière similaire ...
    @Transactional
    public DevisResponseDTO addArticleToDevis(String devisId, Long articleId) {
        Devis devis = devisRepository.findById(devisId)
                .orElseThrow(() -> new ResourceNotFoundException("Devis non trouvé avec l'ID: " + devisId));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article non trouvé avec l'ID: " + articleId));

        if (devis.getArticles().contains(article)) {
            throw new BadRequestException("L'article " + articleId + " est déjà dans le devis " + devisId);
        }

        devis.getArticles().add(article);
        devisRepository.save(devis); // Pas besoin de sauver l'article, juste la relation

        return DevisResponseDTO.fromEntity(devis);
    }

    @Transactional(readOnly = true)
    public DevisPdfDTO prepareDevisDataForPdf(String id) {
        Devis devis = devisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Devis non trouvé avec l'ID: " + id));

        DevisPdfDTO pdfDto = new DevisPdfDTO();
        pdfDto.setId(devis.getId());
        pdfDto.setDescription(devis.getDescription());
        // ... mapper les autres champs simples ...

        // Grouper les articles par catégorie
        Map<Categorie, List<Article>> articlesByCat = devis.getArticles().stream()
                .collect(Collectors.groupingBy(Article::getCategorie));

        List<CategoryPdfDTO> categoryDtos = new ArrayList<>();
        for (Map.Entry<Categorie, List<Article>> entry : articlesByCat.entrySet()) {
            CategoryPdfDTO catDto = new CategoryPdfDTO();
            catDto.setNomCategorie(entry.getKey().getNomCategorie());

            List<ArticlePdfDTO> articleDtos = entry.getValue().stream().map(article -> {
                ArticlePdfDTO articleDto = new ArticlePdfDTO();
                articleDto.setDesignation(article.getDesignation());
                articleDto.setQuantite(article.getQuantite());
                articleDto.setPrixUnitaire(article.getPrixUnitaire());
                articleDto.setPrixTotal(article.getQuantite() * article.getPrixUnitaire());
                return articleDto;
            }).collect(Collectors.toList());

            catDto.setArticles(articleDtos);
            catDto.setTotalCategorie(articleDtos.stream().mapToDouble(ArticlePdfDTO::getPrixTotal).sum());
            categoryDtos.add(catDto);
        }

        // Trier les catégories par nom
        categoryDtos.sort(Comparator.comparing(CategoryPdfDTO::getNomCategorie));
        pdfDto.setCategories(categoryDtos);

        // Calculer les totaux
        double totalHT = categoryDtos.stream().mapToDouble(CategoryPdfDTO::getTotalCategorie).sum();
        double tva = totalHT * 0.1925; // Exemple de TVA
        double totalTTC = totalHT + tva;

        pdfDto.setTotalHT(totalHT);
        pdfDto.setTva(tva);
        pdfDto.setTotalTTC(totalTTC);
        // pdfDto.setTotalEnLettres(NumberToWordsConverter.convert(totalTTC)); // Si vous avez un convertisseur

        return pdfDto;
    }

    private String generateNextId() {
        LocalDate now = LocalDate.now();
        String prefix = "D-" + now.format(DateTimeFormatter.ofPattern("yyyy-MM")) + "-";

        Optional<Devis> lastDevisOpt = devisRepository.findTopByIdStartingWithOrderByIdDesc(prefix);

        int nextSequence = 1;
        if (lastDevisOpt.isPresent()) {
            String lastId = lastDevisOpt.get().getId();
            try {
                String lastSequenceStr = lastId.substring(prefix.length());
                nextSequence = Integer.parseInt(lastSequenceStr) + 1;
            } catch (Exception e) {
                logger.error("Impossible de parser le dernier ID de devis: {}. Réinitialisation à 1.", lastId, e);
            }
        }
        return prefix + String.format("%05d", nextSequence);
    }
    
    @Transactional
    public DevisResponseDTO updateDevis(String id, UpdateDevisRequest request) {
        Devis devis = devisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Devis non trouvé avec l'ID: " + id));

        // Règle métier : On ne peut modifier un devis que s'il est en rédaction ou soumis.
        if (devis.getStatut() == DevisStatus.VALIDER || devis.getStatut() == DevisStatus.REFUSER) {
            throw new BadRequestException("Impossible de modifier un devis qui a déjà été validé ou refusé. Statut actuel : " + devis.getStatut());
        }

        // Mapper les champs de la requête vers l'entité
        devis.setDescription(request.getDescription());
        devis.setDateValidite(request.getDateValidite());
        devis.setTypeTravaux(request.getTypeTravaux());
        devis.setContrainte(request.getContrainte());
        devis.setPeinture(request.isPeinture());
        devis.setEffectif(request.getEffectif());
        devis.setLivraison(request.getLivraison());
        devis.setDevise(request.getDevise());
        devis.setSiteIntervention(request.getSiteIntervention());

        devis.setDateUpdateDevis(LocalDate.now());

        Devis updatedDevis = devisRepository.save(devis);
        return DevisResponseDTO.fromEntity(updatedDevis);
    }

    @Transactional
    public void deleteDevis(String id) {
        Devis devis = devisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Devis non trouvé avec l'ID: " + id));

        // RÈGLE MÉTIER CRUCIALE : Empêcher la suppression si le devis est lié à des documents en aval.
        // Cela prévient la perte de données critiques (factures, bons de commande, etc.)
        if (!devis.getFactures().isEmpty()) {
            throw new BadRequestException("Impossible de supprimer ce devis car il est déjà lié à " + devis.getFactures().size() + " facture(s).");
        }
        if (!devis.getBonCommandes().isEmpty()) {
            throw new BadRequestException("Impossible de supprimer ce devis car il est déjà lié à " + devis.getBonCommandes().size() + " bon(s) de commande.");
        }
        if (!devis.getDemandeAchats().isEmpty()) {
            throw new BadRequestException("Impossible de supprimer ce devis car il est déjà lié à " + devis.getDemandeAchats().size() + " demande(s) d'achat.");
        }

        // Si tout est bon, on peut supprimer
        devisRepository.delete(devis);
    }

    @Transactional(readOnly = true)
    public List<DevisArticleResponseDTO> getArticlesForDevis(String devisId) {
        Devis devis = devisRepository.findById(devisId)
                .orElseThrow(() -> new ResourceNotFoundException("Devis non trouvé avec l'ID: " + devisId));

        return devis.getArticles().stream()
                .map(DevisArticleResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public DevisResponseDTO updateStatus(String id, DevisStatus newStatus) {
        // 1. Récupérer le devis
        Devis devis = devisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Devis non trouvé avec l'ID: " + id));

        // 2. RÈGLE MÉTIER : S'assurer que le statut n'est pas déjà finalisé.
        // On ne peut changer le statut que d'un devis qui est "EN_ATTENTE".
        if (!"EN_ATTENTE".equals(devis.getStatut())) {
            throw new BadRequestException("Impossible de changer le statut. Le devis a déjà été traité (statut actuel : " + devis.getStatut() + ").");
        }

        // 3. RÈGLE MÉTIER : Vérifier si le nouveau statut est valide.
        // Bien que le contrôleur fasse un premier filtre, le service doit être autonome et robuste.
        if (!"VALIDER".equals(newStatus) && !"REFUSER".equals(newStatus)) {
            throw new BadRequestException("Statut invalide : '" + newStatus + "'. Les statuts autorisés sont 'VALIDER' ou 'REFUSER'.");
        }

        // 4. Appliquer les changements
        devis.setStatut(newStatus);
        devis.setDateUpdateDevis(LocalDate.now());

        // 5. Sauvegarder les changements dans la base de données
        Devis updatedDevis = devisRepository.save(devis);
        logger.info("Le statut du devis ID {} a été mis à jour à '{}'.", updatedDevis.getId(), updatedDevis.getStatut());

        // 6. (Optionnel mais recommandé) Déclencher des actions secondaires
        // Par exemple, envoyer une notification à l'équipe concernée.
        // Pour cela, il faudrait que le Devis soit lié à un utilisateur (créateur).
        // String message = "Le devis N° " + updatedDevis.getId() + " a été " + newStatus.toLowerCase() + ".";
        // if (updatedDevis.getCreator() != null) {
        //     notificationService.createNotification(message, updatedDevis.getCreator());
        // }
        // 7. Retourner le DTO mis à jour
        return DevisResponseDTO.fromEntity(updatedDevis);
    }

    @Transactional
    public DevisResponseDTO updateDevisStatus(String id, String newStatus) {
        Devis devis = devisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Devis non trouvé avec l'ID: " + id));

        DevisStatus statusEnum;

        try {
            // Convertir la chaîne en enum, insensible à la casse
            statusEnum = DevisStatus.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Statut invalide pour un devis: " + newStatus);
        }

        // Ici, vous pourriez ajouter de la logique métier.
        // Par exemple, on ne peut pas passer de REFUSER à VALIDER.
        // if (devis.getStatut() == DevisStatus.REFUSER && statusEnum == DevisStatus.VALIDER) {
        //     throw new BadRequestException("Un devis refusé ne peut pas être validé.");
        // }
        devis.setStatut(statusEnum);
        Devis updatedDevis = devisRepository.save(devis);
        logger.info("Statut du Devis ID {} mis à jour à {}", id, newStatus);

        return DevisResponseDTO.fromEntity(updatedDevis);
    }

}
