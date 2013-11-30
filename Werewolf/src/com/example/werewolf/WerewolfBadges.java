package com.example.werewolf;

public class WerewolfBadges {
	
	static class Badge {
		String name;
		String description;
		int points;
		
		public Badge(String name, String description, int points) {
			this.name = name;
			this.description = description;
			this.points = points;
		}
	}	
	
	public static Badge getBadge(int tag){
		switch (tag){
		case 1:
			return killer;
		case 2:
			return spree;
		case 3:
			return serial_killer;
		case 4: 
			return good_judgement;
		case 5:
			return bad_judgement;
		case 6:
			return survivor;
		case 7:
			return winner;
		case 8:
			return conquerer;
		case 9:
			return champion;
		default:
			return null;
		}
	}

	public static Badge killer = new Badge("Killer", "Get a kill as a wolf", 5);
	public static Badge spree = new Badge("Spree", "Kill 3 villagers in one game", 15);
	public static Badge serial_killer = new Badge("Serial Killer", "Get 20 lifetime kills as a werewolf", 20);
	public static Badge good_judgement = new Badge("Good Judgement", "Vote for a werewolf to be hung", 10);
	public static Badge bad_judgement = new Badge("Bad Judgement", "Vote for a villager to be hung", 5);
	public static Badge survivor = new Badge("Survivor", "Win the game without dying as a villager", 5);
	public static Badge winner = new Badge("Winner", "Win 1 game", 5);
	public static Badge conquerer = new Badge("Conquerer", "Win 5 games", 20);
	public static Badge champion = new Badge("Champion", "Win 25 games", 50);
	
}

