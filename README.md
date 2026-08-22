# SunuHelp

Application de localisation en temps réel de ressources et de services — permet à toute personne de trouver un commerce, un établissement de santé, une école, une administration ou tout autre service à proximité, et à toute structure d'enregistrer et de faire connaître son offre.

## Contexte

Projet réalisé dans le cadre du Master 2 — Section Informatique, Faculté des Sciences et Techniques, Université Cheikh Anta Diop de Dakar.

## Architecture

Architecture microservices en Java / Spring Boot :

| Service | Rôle |
|---|---|
| `auth-service` | Comptes, OTP, JWT, rôles |
| `category-service` | Catégories et sous-catégories hiérarchiques |
| `entity-service` | Entités, points de service, horaires, offres, niveau de confiance |
| `review-service` | Avis, notes, réponses |
| `user-service` | Profils Usager, favoris, historique de recherche |
| `search-service` | Index de recherche (Elasticsearch) |
| `geo-service` | Géocodage d'adresses (cache) |
| `media-service` | Stockage des fichiers (logos, photos, documents) |
| `notification-service` | Envoi de SMS (OTP) et d'emails |

Infrastructure : API Gateway (Spring Cloud Gateway), Eureka (service discovery), Kafka (événements asynchrones), PostgreSQL (une base par service), Elasticsearch (recherche).

## Documentation

- [Cahier des charges complet](docs/cahier-des-charges/Cahier_des_charges_V1_DAOUDA_BA.docx)
- [Diagramme de cas d'utilisation](docs/diagrams/diagramme_cas_utilisation_global.png)

## Statut

🚧 En cours de conception — modélisation des données et diagrammes UML en cours, implémentation à venir.

## Auteur

Daouda Ba
