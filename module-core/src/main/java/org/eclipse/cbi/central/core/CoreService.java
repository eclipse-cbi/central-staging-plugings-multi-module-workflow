package org.eclipse.cbi.central.core;

/**
 * Core service with business logic.
 */
public class CoreService {
    
    /**
     * Processes a message and returns a result.
     * 
     * @param message the input message
     * @return the processed result
     */
    public String process(String message) {
        if (message == null || message.isEmpty()) {
            return "No message provided";
        }
        return "Processed: " + message;
    }
    
    /**
     * Calculates a value based on input.
     * 
     * @param value the input value
     * @return the calculated result
     */
    public int calculate(int value) {
        return value * 2;
    }
}
