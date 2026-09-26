package com.sunuhelp.category.config;

import com.sunuhelp.category.entity.Category;
import com.sunuhelp.category.entity.CategoryTranslation;
import com.sunuhelp.category.repository.CategoryRepository;
import com.sunuhelp.category.repository.CategoryTranslationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Peuple category-service avec une taxonomie complete (24 racines,
 * ~120 sous-categories) au demarrage - uniquement en profil dev, jamais
 * en production. Idempotent : verifie l'existence avant de creer, comme
 * DevDataInitializer dans auth-service.
 * createdBy utilise un UUID systeme fixe - simple champ d'audit, jamais
 * de contrainte de cle etrangere entre bases (coherent avec "reference
 * logique, jamais de relation JPA directe" applique partout ailleurs).
 */
@Component
@Profile("dev")
public class CategoryDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CategoryDataInitializer.class);
    private static final UUID SYSTEM_ACCOUNT_ID = new UUID(0, 0);

    private final CategoryRepository categoryRepository;
    private final CategoryTranslationRepository translationRepository;

    public CategoryDataInitializer(CategoryRepository categoryRepository,
                                    CategoryTranslationRepository translationRepository) {
        this.categoryRepository = categoryRepository;
        this.translationRepository = translationRepository;
    }

    private record SubCategory(String slug, String icon, String nameFr, String nameEn) {}

    private record RootCategory(String slug, String icon, boolean requiresValidation,
                                 String nameFr, String nameEn, List<SubCategory> children) {}

    @Override
    public void run(String... args) {
        int order = 1;
        for (RootCategory root : ALL_CATEGORIES) {
            createIfMissing(root, order++);
        }
        log.warn("=== {} categories racines verifiees/creees (profil dev) ===", ALL_CATEGORIES.size());
    }

    private void createIfMissing(RootCategory root, int displayOrder) {
        UUID rootId;
        if (categoryRepository.existsByParentIdAndSlugAndActiveTrue(null, root.slug())) {
            rootId = categoryRepository.findByParentIdIsNullAndActiveTrueOrderByDisplayOrder().stream()
                    .filter(c -> c.getSlug().equals(root.slug()))
                    .findFirst()
                    .map(Category::getId)
                    .orElseThrow();
        } else {
            Category category = Category.createRoot(
                    root.slug(), root.icon(), displayOrder, root.requiresValidation(), SYSTEM_ACCOUNT_ID);
            categoryRepository.save(category);
            rootId = category.getId();
            translationRepository.save(CategoryTranslation.of(rootId, "fr", root.nameFr(), null));
            translationRepository.save(CategoryTranslation.of(rootId, "en", root.nameEn(), null));
        }

        int childOrder = 1;
        for (SubCategory sub : root.children()) {
            if (categoryRepository.existsByParentIdAndSlugAndActiveTrue(rootId, sub.slug())) {
                childOrder++;
                continue;
            }
            Category parent = categoryRepository.findById(rootId).orElseThrow();
            Category child = Category.createChild(
                    sub.slug(), rootId, parent.getDepth(), sub.icon(),
                    childOrder++, root.requiresValidation(), SYSTEM_ACCOUNT_ID);
            categoryRepository.save(child);
            translationRepository.save(CategoryTranslation.of(child.getId(), "fr", sub.nameFr(), null));
            translationRepository.save(CategoryTranslation.of(child.getId(), "en", sub.nameEn(), null));
        }
    }

    private static final List<RootCategory> ALL_CATEGORIES = List.of(

        new RootCategory("sante", "🏥", true, "Santé", "Health", List.of(
            new SubCategory("pharmacie", "💊", "Pharmacie", "Pharmacy"),
            new SubCategory("hopital", "🏨", "Hôpital", "Hospital"),
            new SubCategory("clinique", "⚕️", "Clinique", "Clinic"),
            new SubCategory("dentiste", "🦷", "Dentiste", "Dentist"),
            new SubCategory("opticien", "👓", "Opticien", "Optician"),
            new SubCategory("laboratoire-analyses", "🧪", "Laboratoire d'analyses", "Medical laboratory"),
            new SubCategory("cabinet-medical", "🩺", "Cabinet médical", "Medical office")
        )),

        new RootCategory("commerce", "🛍️", false, "Commerce", "Retail", List.of(
            new SubCategory("epicerie", "🥫", "Épicerie", "Grocery store"),
            new SubCategory("supermarche", "🛒", "Supermarché", "Supermarket"),
            new SubCategory("bijouterie", "💍", "Bijouterie", "Jewelry store"),
            new SubCategory("vetements-mode", "👗", "Vêtements & Mode", "Clothing & Fashion"),
            new SubCategory("electronique", "📱", "Électronique", "Electronics"),
            new SubCategory("librairie-papeterie", "📚", "Librairie-Papeterie", "Bookstore & Stationery"),
            new SubCategory("quincaillerie", "🔩", "Quincaillerie", "Hardware store"),
            new SubCategory("cosmetiques", "💄", "Cosmétiques", "Cosmetics")
        )),

        new RootCategory("restauration", "🍽️", false, "Restauration", "Food & Dining", List.of(
            new SubCategory("restaurant", "🍽️", "Restaurant", "Restaurant"),
            new SubCategory("fast-food", "🍔", "Fast-food", "Fast food"),
            new SubCategory("cafe", "☕", "Café", "Café"),
            new SubCategory("patisserie", "🧁", "Pâtisserie", "Pastry shop"),
            new SubCategory("traiteur", "🍱", "Traiteur", "Catering"),
            new SubCategory("bar", "🍹", "Bar", "Bar")
        )),

        new RootCategory("hebergement", "🏨", false, "Hébergement", "Lodging", List.of(
            new SubCategory("hotel", "🏨", "Hôtel", "Hotel"),
            new SubCategory("auberge", "🛏️", "Auberge", "Inn"),
            new SubCategory("residence-meublee", "🏠", "Résidence meublée", "Furnished residence"),
            new SubCategory("camping", "⛺", "Camping", "Camping")
        )),

        new RootCategory("artisanat-reparation", "🔧", false, "Artisanat & Réparation", "Crafts & Repair", List.of(
            new SubCategory("menuiserie", "🪚", "Menuiserie", "Carpentry"),
            new SubCategory("couture-tailleur", "🧵", "Couture & Tailleur", "Sewing & Tailoring"),
            new SubCategory("cordonnerie", "👞", "Cordonnerie", "Shoe repair"),
            new SubCategory("reparation-electronique", "🔌", "Réparation électronique", "Electronics repair"),
            new SubCategory("plomberie", "🚰", "Plomberie", "Plumbing"),
            new SubCategory("electricite", "⚡", "Électricité", "Electrical work"),
            new SubCategory("mecanique-auto", "🔩", "Mécanique auto", "Auto mechanics"),
            new SubCategory("coiffure-beaute", "💇", "Coiffure & Beauté", "Hair & Beauty")
        )),

        new RootCategory("finance", "💰", true, "Finance", "Finance", List.of(
            new SubCategory("banque", "🏦", "Banque", "Bank"),
            new SubCategory("microfinance", "🪙", "Microfinance", "Microfinance"),
            new SubCategory("assurance", "🛡️", "Assurance", "Insurance"),
            new SubCategory("transfert-argent", "💸", "Transfert d'argent", "Money transfer"),
            new SubCategory("bureau-change", "💱", "Bureau de change", "Currency exchange")
        )),

        new RootCategory("education", "🎓", true, "Éducation", "Education", List.of(
            new SubCategory("ecole", "🏫", "École", "School"),
            new SubCategory("universite", "🎓", "Université", "University"),
            new SubCategory("centre-formation", "📖", "Centre de formation", "Training center"),
            new SubCategory("cours-particuliers", "📝", "Cours particuliers", "Private tutoring"),
            new SubCategory("garderie-creche", "🧸", "Garderie & Crèche", "Daycare & Nursery")
        )),

        new RootCategory("transport", "🚗", false, "Transport", "Transport", List.of(
            new SubCategory("location-vehicules", "🚙", "Location de véhicules", "Vehicle rental"),
            new SubCategory("auto-ecole", "🚦", "Auto-école", "Driving school"),
            new SubCategory("station-service", "⛽", "Station-service", "Gas station"),
            new SubCategory("garage-automobile", "🔧", "Garage automobile", "Auto garage")
        )),

        new RootCategory("services-professionnels", "💼", true, "Services professionnels", "Professional Services", List.of(
            new SubCategory("avocat", "⚖️", "Avocat", "Lawyer"),
            new SubCategory("notaire", "📜", "Notaire", "Notary"),
            new SubCategory("comptable", "🧮", "Comptable", "Accountant"),
            new SubCategory("agence-immobiliere", "🏢", "Agence immobilière", "Real estate agency"),
            new SubCategory("architecte", "📐", "Architecte", "Architect"),
            new SubCategory("agence-voyage", "✈️", "Agence de voyage", "Travel agency")
        )),

        new RootCategory("bien-etre-loisirs", "🧘", false, "Bien-être & Loisirs", "Wellness & Leisure", List.of(
            new SubCategory("salle-sport", "🏋️", "Salle de sport", "Gym"),
            new SubCategory("spa", "💆", "Spa", "Spa"),
            new SubCategory("salon-coiffure", "💇", "Salon de coiffure", "Hair salon"),
            new SubCategory("cinema", "🎬", "Cinéma", "Cinema")
        )),

        new RootCategory("technologie", "💻", false, "Technologie", "Technology", List.of(
            new SubCategory("reparation-informatique", "🖥️", "Réparation informatique", "Computer repair"),
            new SubCategory("cybercafe", "🌐", "Cybercafé", "Internet café"),
            new SubCategory("materiel-informatique", "🖨️", "Vente de matériel informatique", "IT equipment sales"),
            new SubCategory("developpement-web", "👨‍💻", "Développement web", "Web development")
        )),

        new RootCategory("agriculture-elevage", "🌾", false, "Agriculture & Élevage", "Agriculture & Livestock", List.of(
            new SubCategory("vente-intrants", "🌱", "Vente d'intrants", "Agricultural supplies"),
            new SubCategory("veterinaire", "🐄", "Vétérinaire", "Veterinarian"),
            new SubCategory("materiel-agricole", "🚜", "Matériel agricole", "Farm equipment")
        )),

        new RootCategory("administration-publique", "🏛️", true, "Administration & Services publics", "Administration & Public Services", List.of(
            new SubCategory("mairie", "🏛️", "Mairie", "City hall"),
            new SubCategory("poste", "📮", "Poste", "Post office"),
            new SubCategory("prefecture", "🏛️", "Préfecture", "Prefecture"),
            new SubCategory("etat-civil", "📋", "Services d'état civil", "Civil registry")
        )),

        new RootCategory("religion-culte", "🕌", false, "Religion & Culte", "Religion & Worship", List.of(
            new SubCategory("mosquee", "🕌", "Mosquée", "Mosque"),
            new SubCategory("eglise", "⛪", "Église", "Church"),
            new SubCategory("objets-religieux", "📿", "Objets religieux", "Religious goods")
        )),

        new RootCategory("nettoyage-blanchisserie", "🧺", false, "Nettoyage & Blanchisserie", "Cleaning & Laundry", List.of(
            new SubCategory("pressing", "👔", "Pressing", "Dry cleaning"),
            new SubCategory("nettoyage-domicile", "🧹", "Nettoyage à domicile", "Home cleaning"),
            new SubCategory("blanchisserie", "🧺", "Blanchisserie", "Laundry")
        )),

        new RootCategory("services-funeraires", "⚰️", false, "Services funéraires", "Funeral Services", List.of(
            new SubCategory("pompes-funebres", "⚰️", "Pompes funèbres", "Funeral home"),
            new SubCategory("marbrerie-funeraire", "🪦", "Marbrerie funéraire", "Funeral masonry")
        )),

        new RootCategory("securite", "🔒", true, "Sécurité", "Security", List.of(
            new SubCategory("agence-securite", "🛡️", "Agence de sécurité", "Security agency"),
            new SubCategory("serrurier", "🔑", "Serrurier", "Locksmith"),
            new SubCategory("videosurveillance", "📹", "Vidéosurveillance", "Video surveillance")
        )),

        new RootCategory("medias-communication", "📸", false, "Médias & Communication", "Media & Communication", List.of(
            new SubCategory("studio-photo", "📸", "Studio photo", "Photo studio"),
            new SubCategory("imprimerie", "🖨️", "Imprimerie", "Printing shop"),
            new SubCategory("agence-publicite", "📢", "Agence de publicité", "Advertising agency")
        )),

        new RootCategory("sport", "⚽", false, "Sport", "Sports", List.of(
            new SubCategory("articles-sport", "⚽", "Vente d'articles de sport", "Sports equipment sales"),
            new SubCategory("club-sportif", "🏆", "Club sportif", "Sports club"),
            new SubCategory("location-terrain", "🏟️", "Location de terrain", "Field rental")
        )),

        new RootCategory("ong-associations", "🤝", false, "ONG & Associations", "NGOs & Associations", List.of(
            new SubCategory("association-caritative", "❤️", "Association caritative", "Charity"),
            new SubCategory("ong", "🤝", "ONG", "NGO"),
            new SubCategory("organisation-communautaire", "🏘️", "Organisation communautaire", "Community organization")
        )),

        new RootCategory("import-export-grossiste", "🚚", false, "Import-Export & Grossiste", "Import-Export & Wholesale", List.of(
            new SubCategory("grossiste", "📦", "Grossiste", "Wholesaler"),
            new SubCategory("import-export", "🚢", "Import-export", "Import-export"),
            new SubCategory("logistique", "🚚", "Logistique", "Logistics")
        )),

        new RootCategory("telecoms-energie", "📡", false, "Télécoms & Énergie", "Telecom & Energy", List.of(
            new SubCategory("agence-operateur-mobile", "📱", "Agence opérateur mobile", "Mobile operator agency"),
            new SubCategory("agence-eau-electricite", "💡", "Agence eau/électricité", "Water/electricity agency"),
            new SubCategory("fournisseur-internet", "📡", "Fournisseur internet", "Internet provider")
        )),

        new RootCategory("artisanat-art-textile", "🎨", false, "Artisanat d'art & Textile", "Art Craft & Textile", List.of(
            new SubCategory("tissus-wax", "🧶", "Tissus & Wax", "Fabrics & Wax"),
            new SubCategory("poterie-sculpture", "🏺", "Poterie & Sculpture", "Pottery & Sculpture"),
            new SubCategory("maroquinerie", "👜", "Maroquinerie", "Leather goods"),
            new SubCategory("bijoux-artisanaux", "💍", "Bijoux artisanaux", "Handmade jewelry")
        )),

        new RootCategory("evenementiel", "🎉", false, "Événementiel", "Events", List.of(
            new SubCategory("salle-reception", "🎪", "Salle de réception", "Reception hall"),
            new SubCategory("location-materiel", "🎊", "Location de matériel", "Equipment rental"),
            new SubCategory("organisation-evenements", "🎉", "Organisation d'événements", "Event planning")
        ))
    );
}
