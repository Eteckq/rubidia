package me.pmilon.RubidiaCore.utils;

import java.util.Arrays;
import java.util.List;

import de.slikey.effectlib.util.ParticleEffect;

public class Settings {
	
	public static int LEVEL_JOB = 10;
	public static int LEVEL_MAX = 150;

	public static double STRENGTH_FACTOR_MELEE_DAMAGES_ON_MELEE = .0075;
	public static double STRENGTH_FACTOR_MELEE_RANGE_DAMAGES_ON_MELEE = .005;
	public static double STRENGTH_FACTOR_HAND_DAMAGES_ON_MELEE = .2;
	public static double ENDURANCE_FACTOR_MAXHEALTH = .75;
	public static double ENDURANCE_FACTOR_NRJREGEN = .02;
	public static double ENDURANCE_FACTOR_DEFENSE = .005;
	public static double ENDURANCE_FACTOR_ABILITY_DEF = .005;
	public static double AGILITY_FACTOR_RANGE_DAMAGES_ON_RANGE = .01;
	public static double AGILITY_FACTOR_ATTACK_SPEED = .01;
	public static double AGILITY_FACTOR_CRITICAL_STRIKE_DAMAGES = .015;
	public static double AGILITY_FACTOR_CRITICAL_STRIKE_CHANCE = .005;
	public static double INTELLIGENCE_FACTOR_MAGIC_DAMAGES_ON_MAGIC = .0075;
	public static double INTELLIGENCE_FACTOR_ABILITY_DAMAGES = .0075;
	public static double INTELLIGENCE_FACTOR_MAXNRJ = 5;
	public static double PERCEPTION_FACTOR_BLOCK_CHANCE = .0025;
	public static double PERCEPTION_FACTOR_LOOT_CHANCE = .0025;
	public static double PERCEPTION_FACTOR_LIFT_COST = .005;

	public static int SWORDS_DMG_RANGE_MAX = 29;
	public static int AXES_DMG_RANGE_MAX = 61;
	public static int BOWS_DMG_RANGE_MAX = 18;
	public static int WANDS_DMG_RANGE_MAX = 43;
	public static int WEAPONS_DMG_MAX = 390;
	public static double WEAPONS_SPD_MAX = 1.6;
	public static double WEAPONS_SPD_MIN = .3334;
	public static int HELMETS_DEF_MAX = 160;
	public static int HELMETS_DEF_RANGE_MAX = 35;
	public static int ARMORS_DEF_MAX = 210;
	public static int ARMORS_DEF_RANGE_MAX = 56;
	public static int GAUNTLETS_DEF_MAX = 90;
	public static int GAUNTLETS_DEF_RANGE_MAX = 19;
	public static int BOOTS_DEF_MAX = 130;
	public static int BOOTS_DEF_RANGE_MAX = 27;
	public static int SHIELDS_DEF_MAX = 120;
	public static int SHIELDS_DEF_RANGE_MAX = 47;
	public static double PALADIN_DEF_FACTOR = 1.31;
	public static double RANGER_DEF_FACTOR = 1.12;
	public static double MAGE_DEF_FACTOR = 1.04;
	public static double ASSASSIN_DEF_FACTOR = .93;
	public static double PALADIN_DMG_FACTOR = 1.12;
	public static double RANGER_DMG_FACTOR = 1.47;
	public static double MAGE_DMG_FACTOR = 1.01;
	public static double ASSASSIN_DMG_FACTOR = 1.21;
	public static double PALADIN_SPD_FACTOR = .83;
	public static double RANGER_SPD_FACTOR = 1.03;
	public static double MAGE_SPD_FACTOR = .91;
	public static double ASSASSIN_SPD_FACTOR = 1.08;
	
	public static double ENCHANTMENT_DAMAGE_ALL_FACTOR = .025;
	public static double ENCHANTMENT_SPECIFIC_DAMAGES_FACTOR = .025;
	public static double ENCHANTMENT_POWER_FACTOR = .025;
	public static double ENCHANTMENT_PROTECTION_FACTOR = .04;
	public static double ENCHANTMENT_LOOT_BONUS_FACTOR = .6;
	public static double ENCHANTMENT_LUCK_FACTOR = .01;
	public static double POTIONEFFECT_DAMAGES_FACTOR = .025;
	
	public static double GLOBAL_WEAPON_DROP_REDUCTION = 1.15;
	public static double WEAPON_RARITY_MIN = .001;
	public static double WEAPON_RARITY_MAX = .02;

	public final static int DUEL_REQUEST_TIME = 30;
	public final static int DUEL_TIMEOUT = 3;
	public final static int COMPETITIVE_DUEL_LEVEL_SHIFT_MAX = 15;
	public final static int COMPETITIVE_DUEL_DELAY_MAX = 30;
	public final static int COMPETITIVE_DUEL_FORFAIT_RENOM = 10;
	public final static int COMPETITIVE_DUEL_TIE_RENOM = 15;
	public final static int COMPETITIVE_DUEL_WIN_RENOM_MIN = 10;
	public final static int COMPETITIVE_DUEL_WIN_RENOM_MAX = 50;
	public final static double COMPETITIVE_DUEL_FACTOR_POWER = .8;
	public final static double COMPETITIVE_DUEL_FACTOR_BONUS = .01;
	public final static ParticleEffect DUEL_WALL_PARTICLE = ParticleEffect.DRAGON_BREATH;
	
	public static int SHOUT_LIMIT = 5;
	
	public static long TIME_BEFORE_WEDDING_PROPOSAL = 24*60*60*1000L;
	
	public final static List<String> HELP_FR = Arrays.asList(
			"         �8�lCOMMANDES PRINCIPALES",
			"�4�l /tutorial �c(�l/tuto�c)�l �6- �eSe t�l�porter au tutoriel",
			"�4�l /skilltree �c(�l/skt�c)�l �6- �eOuvrir l'arbre des comp�tences",
			"�4�l /characteristics �c(�l/crc�c)�l �6- �eOuvrir le menu des caract�ristiques",
			"�4�l /guild �c(�l/g�c)�l �6- �eOuvrir le menu de gestion de votre guilde",
			"�4�l /guilds �c(�l/gs�c)�l �6- �eOuvrir la liste des guildes",
			"�4�l /quests �c(�l/q�c)�l �6- �eOuvrir la liste des qu�tes actives",
			"�4�l /friend �c(�l/frd�c)�l �6- �eG�rer ses amis",
			"�4�l /friends �c(�l/frds�c)�l �6- �eObtenir sa liste d'amis",
			"�4�l /spawn �6- �eSe t�l�porter � Mearwood",
			"�4�l /suggest �c(�l/sgt�c)�l �6- �eSugg�rer une id�e",
			"�4�l /suggests �c(�l/sgts�c)�l �6- �eObtenir la liste des suggestions",
			"�4�l /money �c(�l/m�c)�l �6- �eG�rer son argent (banque et inventaire)",
			"�4�l /howmanyplayers �c(�l/hmp�c)�l �6- �eObtenir le nombre de joueurs enregistr�s",
			"�4�l /prefs �6- �eModifier les pr�f�rences de jeu",
			"�4�l /shop �6- �eOuvrir une boutique",
			"�4�l /shout �c(�l/s�c)�l �6- �eCrier un message",
			"�4�l /whisper �c(�l/w�c)�l �6- �eChuchoter un message � un joueur",
			"�4�l /answer �c(�l/r�c)�l �6- �eR�pondre au dernier message chuchot�",
			"�4�l /guild �c(�l/g�c)�l �6- �eEnvoyer un message � votre guilde",
			"�4�l /play �6- �eOuvrir le menu de s�lection du personnage",
			"�4�l /bienvenue �c(�l/b�c)�l �6- �eSouhaiter la bienvenue au dernier nouveau joueur",
			"�4�l /profile �c(�l/prof�c)�l �6- �eOuvrir le profil de joueur",
			"         �8�lRACCOURCIS",
			"�4�l SNEAK �c�lClic droit �7sur �c�lJOUEUR �6- �eOuvrir le menu du joueur",
			"�4�l SNEAK �c�lClic droit �7sur �c�lCOMPAGNON �6- �eOuvrir le menu du compagnon",
			"�4�l SNEAK �c�lClic droit �7sur �c�lBIBLIOTHEQUE �6- �eOuvrir le menu du personnage");
	public final static List<String> HELP_EN = Arrays.asList(
			"         �8�lMAIN COMMANDS",
			"�4�l /tutorial �c(�l/tuto�c)�l �6- �eTeleport to tutorial",
			"�4�l /skilltree �c(�l/skt�c)�l �6- �eOpen skilltree",
			"�4�l /characteristics �c(�l/crc�c)�l �6- �eOpen distinctions menu",
			"�4�l /guild �c(�l/g�c)�l �6- �eOpen guild management menu",
			"�4�l /guilds �c(�l/gs�c)�l �6- �eOpen guild list menu",
			"�4�l /quests �c(�l/q�c)�l �6- �eOpen active quests list",
			"�4�l /friend �c(�l/frd�c)�l �6- �eManage friends",
			"�4�l /friends �c(�l/frds�c)�l �6- �eGet friendlist",
			"�4�l /spawn �6- �eTeleport to Mearwood",
			"�4�l /suggest �c(�l/sgt�c)�l �6- �eSuggest an idea",
			"�4�l /suggests �c(�l/sgts�c)�l �6- �eGet suggests list",
			"�4�l /money �c(�l/m�c)�l �6- �eManage money (bank and inventory)",
			"�4�l /howmanyplayers �c(�l/hmp�c)�l �6- �eGet registered players amount",
			"�4�l /prefs �6- �eModify game preferences",
			"�4�l /shop �6- �eOpen a shop",
			"�4�l /shout �c(�l/s�c)�l �6- �eShout a message",
			"�4�l /whisper �c(�l/w�c)�l �6- �eWhisper a message to a player",
			"�4�l /answer �c(�l/r�c)�l �6- �eAnswer to the last whispered message",
			"�4�l /guild �c(�l/g�c)�l �6- �eSend a message to the guild",
			"�4�l /play �6- �eOpen character selection menu",
			"�4�l /bienvenue �c(�l/b�c)�l �6- �eWelcome the last new player",
			"�4�l /profile �c(�l/prof�c)�l �6- �eOpen player's profile menu",
			"         �8�lRACCOURCIS",
			"�4�l SNEAK �c�lRight click �7on �c�lPLAYER �6- �eOpen player menu",
			"�4�l SNEAK �c�lRight click �7on �c�lPET �6- �eOpen pet management menu",
			"�4�l SNEAK �c�lRight click �7on �c�lBOOKSHELF �6- �eOpen character menu");
	
}
