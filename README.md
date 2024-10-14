# Réponse TP
### Exo 1:

### Exo 2:
- Elle avait été crée avec: Collection<StateMachine> machines = new ArrayList<>();
- La machine recoit ses évènements avec le listener onTouchEvent
- Le picking est géré par une fausse vue 'invisible'. Quand on le met en "true", ca s'affiche la zone d'interaction pour du picking.

### Exo 3:
Note: MOVE RELEASE = CURSOR, PRESS = ITEM

### Exo 4:
## 4.1 Première étape: Resize
- Quelle est selon vous la position du centre de l’homothétie/scale ?
+ Le/les doigts qui ne bouge pas
- Si l’interaction consiste à retailler une carte, qu’un des doigts est sur Toulouse et l’autre sur Marseille, quelle propriété ou invariant l’interaction doit-elle respecter ?
+ Les deux points reste au dessus des deux doigts
- Où devrait se situer le centre de l’homothétie quand un toucher reste immobile pendant que l’autre bouge ?
+ Le centre va etre situer sur le doigt qui ne bouge pas.
- Quand un événement Move est reçu pour un toucher, que pouvez-vous dire de la
  position de l’autre toucher ?
+ La position de l'autre toucher reste sous le doigt
- Quelle est la quantité de différence d’homothétie à appliquer à chaque événement ?
+ La quantité de différence d’homothétie à appliquer est proportionnelle à la variation de distance entre les deux doigts. 

## 4.2 Première étape: Rotate
- Quelle est selon vous la position du centre du rotate ?
+ Le doigt qui ne bouge pas
- Si l’interaction consiste à rotate une carte, qu’un des doigts est sur Toulouse et l’autre sur Marseille, quelle propriété ou invariant l’interaction doit-elle respecter ?
+ Les deux points reste au dessus des deux doigts
- Où devrait se situer le centre de l’homothétie quand un toucher reste immobile pendant que l’autre bouge ?
+ Le centre va etre situer sur le doigt qui ne bouge pas.
- Quand un événement Move est reçu pour un toucher, que pouvez-vous dire de la
  position de l’autre toucher ?
+ La position de l'autre toucher reste sous le doigt
- Quelle est la quantité de différence d’homothétie à appliquer à chaque événement ?
+ La quantité de différence d’homothétie à appliquer est proportionnelle à la variation de distance entre les deux doigts. 
