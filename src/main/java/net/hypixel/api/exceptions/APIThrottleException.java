package net.hypixel.api.exceptions;

@SuppressWarnings("serial")
public class APIThrottleException extends HypixelAPIException {
    public APIThrottleException() {
        super("You have passed the API throttle limit!");
    }
}
