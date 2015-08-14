# HeroBattle
Un jeu mêlant **PvP** et **arcade** dans un concept similaire à *Smash Bros*.

Au début du jeu, chaque joueur doit choisir une classe qu'il incarnera durant toute la partie. Chaque classe définit un certain nombre de propriétés telles que le nombre de vies, les dommages au corps à corps ou la résistance aux coups mais aussi des objets spéciaux utilisable durant la partie. Le but du jeu est de faire perdre ses adversaires en les faisant tomber dans le vide ou de le mettre K.O. Chaque dégât que le joueur essuiera se traduit par une augmentation de sa jauge de pourcentage qui commence au début à 0 et qui augmente au fur et à mesure des dommages qu'il se prend. Plus cette jauge est élevée, plus les coups qu'il se prendra le feront reculer loin.

Les objets spéciaux quant à eux permettent au joueur d'activer différents bonus ou malus contre ses adversaires qu'il devra utiliser stratégiquement pour remporter la partie. Attention, leur utilisation possède un certain temps de rechargement propre à chaque objet avant de pouvoir être réutilisés à nouveau !

Des powerups peuvent apparaître durant la partie à des endroits spécifiques. Ils procurent des effets particulier différents de ceux donnés par les objets de classe.

## Contribuer au développement

### Classe joueur

Pour créer une nouvelle classe joueur, créer une nouvelle classe (java) commençant par le nom de votre classe et se terminant par `Class`. Par exemple `SampleClass`. Elle devra hériter de `PlayerClass`. Renseigner toutes les méthodes réécrites avec les valeurs que vous souhaitez (nom, item qui la représente, dommages...).

Pour ajouter des objets spéciaux propres à la classe, vous devrez les enregistrer de la manière suivante `addTool(p.getToolsManager().getTool(Tool.SAMPLE_TOOL));` avec `p` la variable qui correspond à la classe principale et `SAMPLE_TOOL` la constante qui correspond à l'objet souhaité.

Vous devrez enfin enregistrer la classe dans le sélecteur de classe pour qu'elle puisse être utilisée, en ajoutant dans le constructeur de `ClassManager` l'appel à la méthode suivante `registerClass(new SampleClass(p));`.

### Objet spécial

Pour créer un nouvel objet spécial, créer une nouvelle classe commençant par le nom de votre objet et se terminant par `Tool`. Par exemple `SampleTool`. Elle devra hériter de `PlayerTool`. Renseigner la méthode `getToolID()` avec `return "tool.sample";` et les autres méthodes avec les valeurs que vous souhaitez (nom, item qui la représente, dommages...). Ajoutez ensuite la constante `SAMPLE("tool.sample")` dans la classe `Tool`.

Vous devrez enfin enregistrer l'objet pour qu'il puisse être utilisé, en ajoutant dans le constructeur de `ToolsManager` l'appel à la méthode suivante `registerTool(new SampleTool(p));`.

Les méthodes `onRightClick()` et `onLeftClick()` seront appelées respectivement lorsqu'un clic droit ou gauche sera effectué sur l'objet.

Pour ajouter un temps de rechargement (cooldown), instanciez un nouvel objet contenant les arguments suivants `new ItemCooldown(HeroBattle plugin, Player player, PlayerTool tool, int cooldown);`.

### Tâches

Les tâches servent à exécuter une certaine action lorsque un événement est déclenché. Pour créer une nouvelle tâche  créer une nouvelle classe commençant par le nom de votre tâche et se terminant par `Task`. Par exemple `SampleTask`. Elle devra hériter de `Task`. Pour ajouter une nouvelle tâche `p.getGamePlayer(player).addTask(new SampleTask(p, player));`. Elle sera exécutée dès que la méthode `p.getGamePlayer(player).playTask(new SampleTask(p, player));` sera appelée, si le joueur **possède** cette tâche.



### Licence, droit d'exploitation

Le jeu HeroBattle (le Jeu) est publié sous les termes de la Licence CeCILL-C, version 1 (se référer au fichier `LICENSE`).

Le Jeu a été conçu et réalisé par **Amaury Carrade** (également connu sous le nom de AmauryPi) et  **Florian Cassayre** (également connu sous les noms infinity, 6infinity8 et flomine68) (ci-après les Propriétaires).

La permission est donnée, gratuitement, à l'association Elydra Network, d'utiliser, de copier, de modifier et d'exploiter le Jeu, et d'autoriser les personnes auxquelles elle fournit le Jeu de le faire, suivant les conditions suivantes : 
 - l'association ne peut fournir de son propre chef, et sans autorisation écrite des Propriétaires, le Jeu qu'aux contributeurs au développement logiciel, technique ou d'infrastructure de SamaGames (les Développeurs) ; 
 - le Jeu ne peut être utilisé hors du cadre du serveur de jeux SamaGames ; 
 - le Jeu ne peut être redistribué par l'association Elydra Network sans autorisation écrite de l'ensemble des Propriétaires du Jeu ; 
 - les Propriétaires du Jeu se réservent le droit de faire ce que bon leur semble du Jeu, incluant, sans s'y limiter, le droit de l'utiliser, de le copier, de le modifier, de le fusionner à un autre projet, de le publier, de le distribuer, de le sous-licencier et/ou de le vendre, sans l'autorisation de l'association Elydra Network et sans avertissement préalable ; 
 - si les Propriétaires décident de publier le Jeu hors du cadre de l'association Elydra Network et/ou de celui des Développeurs, ils s'engagent à retirer tout contenu propriété de l'association Elydra Network et/ou d'un Développeur qui ne soit pas Propriétaire, tel que (sans s'y limiter) la SamaGames API, de la partie publiée du Jeu, si les Propriétaires ne disposent pas du droit de publication du-dit contenu.

Cette permission peut être retirée par les Propriétaires à tout moment, sans justification, avec un délai minimal de 15 (quinze) jours avant application.


#### Nota Bene

*Ce qui suit ne fait pas partie des termes ci-dessus.*

La permission d'exploitation révocable ci-dessus est placée *au cas où*, pour protéger notre travail en cas de problème (par exemple similaires à ceux rencontrés lors du problème avec zyuiop). Ces termes ne sont en aucun cas a priori contre SamaGames, ce n'est qu'une sécurité. On ne sait jamais.

Le retrait des permissions d'usage ne serait fait qu'en cas extrême où aucun accord amical ne serait trouvé, si jamais une telle situation se reproduit.