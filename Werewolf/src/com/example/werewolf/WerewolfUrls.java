//Keeps track of URLs so that they aren't hard-coded anywhere.

package com.example.werewolf;

public class WerewolfUrls {
	public static String POST_ROOT = "http://ec2-54-235-36-112.compute-1.amazonaws.com/wolves/";
	public static String PING = POST_ROOT + "ping";
	public static String DELETE_ACCOUNT = POST_ROOT + "delete_account";
	public static String CREATE_ACCOUNT = POST_ROOT + "create_account";
	public static String GET_ACCOUNT_INFO = POST_ROOT + "get_account_data";
	public static String GET_GAMES_INFO = POST_ROOT + "get_games_data";
	public static String GET_GAME_INFO = POST_ROOT + "get_game_data";
	public static String CREATE_GAME = POST_ROOT + "create_game";
	public static String JOIN_GAME = POST_ROOT + "join_game";
	public static String RESTART_GAME = POST_ROOT + "restart_game";
	public static String POST_POSITION = POST_ROOT + "post_position";
	public static String PLACE_VOTE = POST_ROOT + "place_vote";
	public static String KILL = POST_ROOT + "kill";
	public static String SMELL = POST_ROOT + "smell";
	public static String GET_HIGHSCORES = POST_ROOT + "get_highscores";
}
