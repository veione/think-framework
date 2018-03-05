package com.think.util;

import java.util.HashMap;

/**
 * ELO Rating calculataor
 * 
 * <pre>
 * Implements two methods 
 * 1. calculateMultiplayer - calculate rating for multiple players 
 * 2. calculate2PlayersRating - for 2 player games
 * </pre>
 * 
 * @author radone@gmail.com
 *
 */
public class EloRating {

	/**
	 * Constructor
	 */
	public EloRating() {
	}

	public HashMap<Integer, Integer> calculateMultiplayer(HashMap<Integer, Integer> usersRating, int userIdWinner) {
		if (usersRating.size() == 0) {
			return usersRating;
		}

		HashMap<Integer, Integer> newUsersPoints = new HashMap<>();

		// K-factor
		int K = 32;

		// Calculate total Q
		double Q = 0.0;
		for (int userId : usersRating.keySet()) {
			Q += Math.pow(10.0, ((double) usersRating.get(userId) / 400));
		}

		// Calculate new rating
		for (int userId : usersRating.keySet()) {
			/**
			 * Expected rating for an user 
			 * E = Q(i) / Q(total) 
			 * Q(i) = 10 ^ (R(i)/400)
			 */
			double expected = (double) Math.pow(10.0, ((double) usersRating.get(userId) / 400)) / Q;
			
			/**
			 * Actual score is
			 * 1 - if player is winner
			 * 0 - if player losses
			 * (another option is to give fractions of 1/number-of-players instead of 0)
			 */
			int actualScore;
			if(userIdWinner == userId) {
				actualScore = 1;
			} else {
				actualScore = 0;
			}
			
			// new rating = R1 + K * (S - E);
			int newRating = (int)Math.round(usersRating.get(userId) + K * (actualScore - expected));
			
			// Add to HashMap
			newUsersPoints.put(userId, newRating);
		}
		return newUsersPoints;
	}
	
	/**
	 * Calculate rating for 2 players
	 * 
	 * @param player1Rating
	 *            The rating of Player1
	 * @param player2Rating
	 *            The rating of Player2
	 * @param outcome
	 *            A string representing the game result for Player1
	 *            "+"	winner
	 *            "="	draw
	 *            "-"	lose
	 * @return New player rating
	 */
	int calculate2PlayersRating(int player1Rating, int player2Rating, String outcome) {
		
		double actualScore;
		
		// winner
		if(outcome.equals("+")) {
			actualScore = 1.0;
			// draw
		} else if (outcome.equals("=")) {
			actualScore = 0.5;
			// lose
		} else if (outcome.equals("-")) {
			actualScore = 0;
			// invalid outcome
		} else {
			return player1Rating;
		}
		
		// calculate expected outcome
		double exponent = (double) (player2Rating - player1Rating) / 400;
		double expectedOutcome = (double) (1/(1+(Math.pow(10, exponent))));
		System.out.println(exponent);
		System.out.println(expectedOutcome);
		
		// K-factor
		int K = 32;
		
		// calculate new rating
		int newRating = (int)Math.round(player1Rating + K * (actualScore - expectedOutcome));
		
		return newRating;
	}
	
	/**
	 * Determine the rating constant k-factor based on current rating
	 * 
	 * @param rating
	 *            Player rating
	 * @return k-factory
	 */
	public int determinek(int rating) {
		int K;
		if (rating < 2000) {
			K = 32;
		} else if (rating >= 2000 && rating < 2400) {
			K = 24;
		} else {
			K = 16;
		}
		return K;
	}
	
	public static void main(String[] args) {
		EloRating rating = new EloRating();
		int score = rating.calculate2PlayersRating(1500, 1500, "+");
		System.out.println(score);
	}
}
