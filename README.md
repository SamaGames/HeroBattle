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