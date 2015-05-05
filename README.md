# LSINF1225-projet
Repository du groupe T pour le projet du cours LSINF1225-projet.

Merci de ne pas push de code contenant des erreurs.

Acitivités
-----------
- ~~MainActivity~~ : page d'accueil de l'application, contient 3 boutons : carte (ShowMenuActivity), 
connexion (LoginActivity), inscription (ResgisterActivity).
- ~~LoginActivity~~ : page de connexion, redirige vers MainLoggedActivity.
- ~~RegisterAcitivity~~ : page d'inscription, une fois l'inscription réussie redirige vers LoginActivity.
- ~~MainLoggedActivity~~ : contient plusieurs boutons selon le type d'utilisateur : carte (ShowMenuActivity), recherche
(SearchActivity), addition (ShowBillsActivity), commande (ShowOrdersActivity)
- ShowMenuActivity : affiche la liste des boissons, cliquer sur une boisson redirige vers ShowDrinkDetailsActivity.
- ShowDrinkDetailsActivity : affiche une boisson en particulier et ces différents attributs ainsi que notes
et commentaires, un bouton permet de rediriger vers AddNoteActivity.
- AddNoteActivity ;
- etc.

Modèles
--------
- ~~User.java~~ : done;
- Drinks.java : (voir classe Song du TP10);
- Bill.java : (idem);
- Order.java : (idem);
