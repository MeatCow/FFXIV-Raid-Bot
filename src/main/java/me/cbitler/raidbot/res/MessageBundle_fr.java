package me.cbitler.raidbot.res;

import java.util.ListResourceBundle;

public class MessageBundle_fr extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return resources;
    }

    private final Object[][] resources = {
            {"raid_name_setup", "Allez c'est parti pour la création d'un petit raid des familles !!\nPendant toute notre petite discussion tu peux taper *cancel* pour annuler, Je ne te cache pas que je suis sensible et que cela me briserai le coeur...\n\n Allez, un petit nom pour cette aventure:"},
            {"waiting_list_query", "Veux-tu une liste d'attente pour ce raid ? [Oui/Non]"},
            {"yes_no_request", "Répondre **oui** ou **non** c'est trop compliqué?!"},
            {"cannot_find_channel", "Please choose a valid channel."},
            {"select_channel", "Quel est le nom du chan que je dois pourrir avec l'annonce du raid?"},
            {"raid_date_query", "Quelle est la date pour ce petit raid? S'il vous plait utiliser le format DD-MM-YYYY HH:MM[AM/PM] TIMEZONE"},
            {"raid_description", "Une petite description du raid:"},
            {"add_minimum_roles", "T'es gentil mais il faut au moins un type de participant ..."},
            {"preformed_setup_info", "Rajoutes des types de participants en précisant chaque grands rôles au format : [nombre max]:[Rôle] ou alors utilises directement des rôles préformatés : \n" +
                    "- *D* pour Donjons (4 joueurs, 1 Tank, 1 Heal, 2 DPS)\n" +
                    "- *R* pour Raids (8 joueurs, 2 Tank, 2 Heal, 4 DPS)\n" +
                    "- pour avoir 8 DPS, tapes *8:dps*\n" +
                    "Quand t'as terminé, signale le moi en tapant *done*"},
            {"preformed_dungeon_added", "BAM ! Une équipe de type Donjon a été ajouté!"},
            {"preformed_raid_added", "BIM ! Une équipe de type Raid a été ajouté!"},
            {"done_query", "Si tu as terminé, tape *done*"},
            {"role_input_error", "Putain tu tapes n'importe quoi la..."},
            {"i_have_added", "J'ai donc ajouté "},
            {"to_your_raid", "dans ton raid!"},
            {"could_not_parse_date", "Format de date non-lisible, s'il vous plait utiliser le format DD-MM-YYYY HH:MM[AM/PM] TIMEZONE"},
            {"raid_reminder_query", "Je peux envoyer un rappel avant le raid, utilise le format XhXX or XXm.\nSi tu ne veux pas de rappel, entrer non."},
            {"raid_cancelled", "Raid annulé."},
            {"raid_erase_error", "C'est la merde, j'arrive pas à effacer le raid..."},
            {"raid_nonexistent", "Dis non à la drogue ! Ce raid n'existe pas!"},
            {"leader_role_updated", "Le nouveau role pour dirigeant de raid est: "},
            {"raid_creation_cancelled", "OK, on abandonne la création du raid, tristesse ..."},
            {"raid_creation_confirmed", "\\o/ Le raid a bien été créé !"},
            {"insufficient_permissions", "ALERTE ! J'ai pas pu créé le raid, je suis visiblement persécuté et je n'ai pas le droit d'écrire dans le chan ?"},
            {"role_selection_cancelled", "Ok, on abandonne la sélection des rôles."},
            {"role_full_error", "Allez, on apprend à compter et on choisi un job dans lequel il reste de la place ?"},
            {"role_already_picked", "Alerte Alzheimer ! Tu as déjà choisi un job... Si tu ne veux plus participer avec ce job utilise le \"X\" rouge."},
            {"created_by", "Créé par : "},
            {"date_time", "Date / Heure"},
            {"roles", "Roles :"},
            {"in_raid", ": tout est ok !\n"},
            {"on_waiting_list", ": en liste d'attente..\n"},
            {"legend", "Légende"},
            {"added_to_raid", "Ajouté au raid !"},
            {"select_a_role", "Choisi un rôle"},
            {"or_hit_cancel", "ou tape *cancel* pour annuler la sélection de rôles."}
    };

}
