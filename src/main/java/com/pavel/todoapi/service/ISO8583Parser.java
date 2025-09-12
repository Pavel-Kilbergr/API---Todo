package com.pavel.todoapi.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * ISO 8583 Message Parser Service
 * Parses financial transaction messages according to ISO 8583 standard
 * 
 * @author Pavel  
 * @since 1.0
 */
@Service
public class ISO8583Parser {
    
    /**
     * Field definitions for ISO 8583 data elements
     * Key: Field number, Value: [type, max_length, description]
     */
    private static final Map<Integer, String[]> FIELD_DEFINITIONS = new HashMap<>();
    
    static {
        // Initialize field definitions according to ISO 8583 standard from specification
        FIELD_DEFINITIONS.put(2, new String[]{"LLVAR", "19", "Primary Account Number (PAN)"});
        FIELD_DEFINITIONS.put(3, new String[]{"n", "6", "Processing Code"});
        FIELD_DEFINITIONS.put(4, new String[]{"n", "12", "Transaction Amount"});
        FIELD_DEFINITIONS.put(5, new String[]{"n", "12", "Amount, settlement"});
        FIELD_DEFINITIONS.put(6, new String[]{"n", "12", "Amount, cardholder billing"});
        FIELD_DEFINITIONS.put(7, new String[]{"n", "10", "Transmission Date & Time"});
        FIELD_DEFINITIONS.put(8, new String[]{"n", "8", "Amount, cardholder billing fee"});
        FIELD_DEFINITIONS.put(9, new String[]{"n", "8", "Conversion rate, settlement"});
        FIELD_DEFINITIONS.put(10, new String[]{"n", "8", "Conversion rate, cardholder billing"});
        FIELD_DEFINITIONS.put(11, new String[]{"n", "6", "System Trace Audit Number (STAN)"});
        FIELD_DEFINITIONS.put(12, new String[]{"n", "6", "Local Transaction Time"});
        FIELD_DEFINITIONS.put(13, new String[]{"n", "4", "Local Transaction Date"});
        FIELD_DEFINITIONS.put(14, new String[]{"n", "4", "Expiration Date"});
        FIELD_DEFINITIONS.put(15, new String[]{"n", "4", "Settlement date"});
        FIELD_DEFINITIONS.put(16, new String[]{"n", "4", "Currency conversion date"});
        FIELD_DEFINITIONS.put(17, new String[]{"n", "4", "Capture date"});
        FIELD_DEFINITIONS.put(18, new String[]{"n", "4", "Merchant Category Code"});
        FIELD_DEFINITIONS.put(19, new String[]{"n", "3", "Acquiring institution (country code)"});
        FIELD_DEFINITIONS.put(20, new String[]{"n", "3", "PAN extended (country code)"});
        FIELD_DEFINITIONS.put(21, new String[]{"n", "3", "Forwarding institution (country code)"});
        FIELD_DEFINITIONS.put(22, new String[]{"n", "3", "POS Entry Mode"});
        FIELD_DEFINITIONS.put(23, new String[]{"n", "3", "Application PAN sequence number"});
        FIELD_DEFINITIONS.put(24, new String[]{"n", "3", "Function code"});
        FIELD_DEFINITIONS.put(25, new String[]{"n", "2", "POS condition code"});
        FIELD_DEFINITIONS.put(26, new String[]{"n", "2", "POS capture code"});
        FIELD_DEFINITIONS.put(27, new String[]{"n", "1", "Authorizing identification response length"});
        FIELD_DEFINITIONS.put(32, new String[]{"LLVAR", "11", "Acquiring Institution Code"});
        FIELD_DEFINITIONS.put(33, new String[]{"LLVAR", "11", "Forwarding institution identification code"});
        FIELD_DEFINITIONS.put(34, new String[]{"LLVAR", "28", "Primary account number, extended"});
        FIELD_DEFINITIONS.put(35, new String[]{"LLVAR", "37", "Track 2 Data"});
        FIELD_DEFINITIONS.put(36, new String[]{"LLLVAR", "104", "Track 3 data"});
        FIELD_DEFINITIONS.put(37, new String[]{"an", "12", "Retrieval Reference Number"});
        FIELD_DEFINITIONS.put(38, new String[]{"an", "6", "Authorization Code"});
        FIELD_DEFINITIONS.put(39, new String[]{"an", "2", "Response Code"});
        FIELD_DEFINITIONS.put(40, new String[]{"an", "3", "Service restriction code"});
        FIELD_DEFINITIONS.put(41, new String[]{"ans", "8", "Terminal ID"});
        FIELD_DEFINITIONS.put(42, new String[]{"ans", "15", "Card acceptor identification code"});
        FIELD_DEFINITIONS.put(43, new String[]{"ans", "40", "Card Acceptor Name/Location"});
        FIELD_DEFINITIONS.put(44, new String[]{"LLVAR", "25", "Additional response data"});
        FIELD_DEFINITIONS.put(45, new String[]{"LLVAR", "76", "Track 1 data"});
        FIELD_DEFINITIONS.put(46, new String[]{"LLLVAR", "999", "Additional data (ISO)"});
        FIELD_DEFINITIONS.put(47, new String[]{"LLLVAR", "999", "Additional data (national)"});
        FIELD_DEFINITIONS.put(48, new String[]{"LLLVAR", "999", "Additional data (private)"});
        FIELD_DEFINITIONS.put(49, new String[]{"n", "3", "Currency code, transaction"});
        FIELD_DEFINITIONS.put(50, new String[]{"n", "3", "Currency code, settlement"});
        FIELD_DEFINITIONS.put(51, new String[]{"n", "3", "Currency code, cardholder billing"});
        FIELD_DEFINITIONS.put(53, new String[]{"n", "16", "Security related control information"});
        FIELD_DEFINITIONS.put(54, new String[]{"LLLVAR", "120", "Additional amounts"});
        FIELD_DEFINITIONS.put(55, new String[]{"LLLVAR", "999", "ICC Data"});
        FIELD_DEFINITIONS.put(60, new String[]{"LLLVAR", "999", "Reserved (national)"});
        FIELD_DEFINITIONS.put(61, new String[]{"LLLVAR", "999", "Reserved (private)"});
        FIELD_DEFINITIONS.put(62, new String[]{"LLLVAR", "999", "Reserved (private)"});
        FIELD_DEFINITIONS.put(63, new String[]{"LLLVAR", "999", "Reserved (private)"});
    }
    
    /**
     * Parse ISO 8583 hex message into readable format
     * 
     * @param hexMessage Raw hex string representing ISO 8583 message
     * @return Formatted string with parsed fields or error message
     */
    public String parseMessage(String hexMessage) {
        try {
            if (hexMessage == null || hexMessage.trim().isEmpty()) {
                return "ERROR: Empty ISO 8583 message";
            }
            
            // Remove spaces and convert to uppercase
            hexMessage = hexMessage.replaceAll("\\s", "").toUpperCase();
            
            // Validate hex format
            if (!hexMessage.matches("^[0-9A-F]+$")) {
                return "ERROR: Invalid hex format. Only 0-9 and A-F characters allowed";
            }
            
            if (hexMessage.length() < 20) { // Minimum: 4 MTI + 16 bitmap
                return "ERROR: Message too short. Minimum 20 hex characters required";
            }
            
            StringBuilder result = new StringBuilder();
            
            // Parse MTI (Message Type Indicator) - first 4 hex characters
            String mti = hexMessage.substring(0, 4);
            result.append("MTI: ").append(mti);
            
            // Parse Primary Bitmap - next 16 hex characters (8 bytes)
            String bitmapHex = hexMessage.substring(4, 20);
            result.append(", Bitmap: ").append(bitmapHex);
            
            // Convert bitmap to binary to determine which fields are present
            String bitmapBinary = hexToBinary(bitmapHex);
            
            // Parse data elements starting after MTI and bitmap
            int dataStart = 20;
            int currentPosition = dataStart;
            
            // Check each bit in bitmap (bits 2-64, since bit 1 indicates secondary bitmap)
            for (int bit = 1; bit < 64 && bit < bitmapBinary.length(); bit++) {
                if (bitmapBinary.charAt(bit) == '1') {
                    int fieldNumber = bit + 1; // Field numbers start from 2
                    
                    if (FIELD_DEFINITIONS.containsKey(fieldNumber)) {
                        String[] fieldDef = FIELD_DEFINITIONS.get(fieldNumber);
                        String fieldType = fieldDef[0];
                        int maxLength = Integer.parseInt(fieldDef[1]);
                        
                        if (currentPosition >= hexMessage.length()) {
                            result.append(", DE").append(String.format("%03d", fieldNumber))
                                  .append(": ERROR - Insufficient data");
                            break;
                        }
                        
                        // Parse field based on type
                        String fieldValue = parseField(hexMessage, currentPosition, fieldType, maxLength);
                        
                        if (fieldValue.startsWith("ERROR")) {
                            result.append(", DE").append(String.format("%03d", fieldNumber))
                                  .append(": ").append(fieldValue);
                            break;
                        }
                        
                        result.append(", DE").append(String.format("%03d", fieldNumber))
                              .append(": ").append(fieldValue);
                        
                        // Update position for next field
                        currentPosition = getNextPosition(hexMessage, currentPosition, fieldType, maxLength);
                    }
                }
            }
            
            return result.toString();
            
        } catch (Exception e) {
            return "ERROR: Failed to parse ISO 8583 message - " + e.getMessage();
        }
    }
    
    /**
     * Convert hex string to binary string
     */
    private String hexToBinary(String hex) {
        StringBuilder binary = new StringBuilder();
        for (int i = 0; i < hex.length(); i++) {
            String hexDigit = String.valueOf(hex.charAt(i));
            int decimal = Integer.parseInt(hexDigit, 16);
            String binaryDigit = String.format("%4s", Integer.toBinaryString(decimal)).replace(' ', '0');
            binary.append(binaryDigit);
        }
        return binary.toString();
    }
    
    /**
     * Parse individual field based on its type
     */
    private String parseField(String hexMessage, int startPos, String fieldType, int maxLength) {
        try {
            if (fieldType.equals("LLVAR")) {
                // LLVAR: 2-digit length in DECIMAL + data
                if (startPos + 2 > hexMessage.length()) {
                    return "ERROR - Insufficient data for LLVAR length";
                }
                
                String lengthStr = hexMessage.substring(startPos, startPos + 2);
                int dataLength;
                
                try {
                    // Parse length as decimal (as per specification)
                    dataLength = Integer.parseInt(lengthStr, 10);
                } catch (NumberFormatException e) {
                    return "ERROR - Invalid LLVAR length format: " + lengthStr;
                }
                
                if (startPos + 2 + dataLength > hexMessage.length()) {
                    return "ERROR - Insufficient data for LLVAR content (need " + dataLength + " chars)";
                }
                
                return hexMessage.substring(startPos + 2, startPos + 2 + dataLength);
                
            } else if (fieldType.equals("LLLVAR")) {
                // LLLVAR: 3-digit length in DECIMAL + data
                if (startPos + 3 > hexMessage.length()) {
                    return "ERROR - Insufficient data for LLLVAR length";
                }
                
                String lengthStr = hexMessage.substring(startPos, startPos + 3);
                int dataLength;
                
                try {
                    dataLength = Integer.parseInt(lengthStr, 10);
                } catch (NumberFormatException e) {
                    return "ERROR - Invalid LLLVAR length format: " + lengthStr;
                }
                
                if (startPos + 3 + dataLength > hexMessage.length()) {
                    return "ERROR - Insufficient data for LLLVAR content";
                }
                
                return hexMessage.substring(startPos + 3, startPos + 3 + dataLength);
                
            } else if (fieldType.equals("n")) {
                // Fixed numeric field
                int hexLength = maxLength; // For numeric fields, each digit = 1 hex character
                if (startPos + hexLength > hexMessage.length()) {
                    return "ERROR - Insufficient data for numeric field";
                }
                return hexMessage.substring(startPos, startPos + hexLength);
                
            } else if (fieldType.equals("ans")) {
                // Fixed alphanumeric special field - each character = 1 hex digit for simplicity
                int hexLength = maxLength; 
                if (startPos + hexLength > hexMessage.length()) {
                    return "ERROR - Insufficient data for ans field";
                }
                return hexMessage.substring(startPos, startPos + hexLength);
            }
            
            return "ERROR - Unknown field type: " + fieldType;
            
        } catch (Exception e) {
            return "ERROR - Field parsing failed: " + e.getMessage();
        }
    }
    
    /**
     * Calculate next position after current field
     */
    private int getNextPosition(String hexMessage, int currentPos, String fieldType, int maxLength) {
        try {
            if (fieldType.equals("LLVAR")) {
                String lengthStr = hexMessage.substring(currentPos, currentPos + 2);
                int dataLength = Integer.parseInt(lengthStr, 10); // Parse as decimal
                return currentPos + 2 + dataLength;
            } else if (fieldType.equals("LLLVAR")) {
                String lengthStr = hexMessage.substring(currentPos, currentPos + 3);
                int dataLength = Integer.parseInt(lengthStr, 10); // Parse as decimal
                return currentPos + 3 + dataLength;
            } else if (fieldType.equals("n")) {
                return currentPos + maxLength;
            } else if (fieldType.equals("ans") || fieldType.equals("an")) {
                return currentPos + maxLength;
            }
        } catch (Exception e) {
            // Return current position if there's an error
            return currentPos;
        }
        return currentPos;
    }
}
