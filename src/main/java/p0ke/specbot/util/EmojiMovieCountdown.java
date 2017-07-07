package p0ke.specbot.util;

import org.joda.time.Duration;

public class EmojiMovieCountdown {
	
	public static final long EMOJI_MOVIE_DATE = 1501225200000L;
	
	public static String getCountdown(){
		
		Duration countdown = new Duration(EMOJI_MOVIE_DATE - System.currentTimeMillis());
		
		return (countdown.getStandardDays() + " days, " + countdown.getStandardHours() % 24 + " hours, " + countdown.getStandardMinutes() % 60 + " minutes and " + countdown.getStandardSeconds() % 60 + " seconds until July 28th, the release of the Emoji Movie in theaters worldwide!");
		
		
	}

}
