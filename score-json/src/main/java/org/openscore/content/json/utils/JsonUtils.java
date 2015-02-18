package org.openscore.content.json.utils;

import java.util.Map;

import static org.openscore.content.json.utils.ActionsEnum.add;
import static org.openscore.content.json.utils.ActionsEnum.insert;
import static org.openscore.content.json.utils.ActionsEnum.update;

/**
 * Created by ioanvranauhp
 * Date 2/9/2015.
 */
public class JsonUtils {

    public static Map<String, String> populateResult(Map<String, String> returnResult, String value, Exception exception) {
        returnResult.put(Constants.OutputNames.RETURN_RESULT, value);
        if (exception != null) {
            returnResult.put(Constants.OutputNames.EXCEPTION, exception.getMessage());
            returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_FAILURE);
        } else {
            returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
        }
        return returnResult;
    }

    public static Map<String, String> populateResult(Map<String, String> returnResult, Exception exception) {
        return populateResult(returnResult, exception.getMessage(), exception);
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().equals(Constants.EMPTY_STRING);
    }

    public static void validateEditJsonInputs(String jsonObject, String jsonPath, String action, String propertyName, String propertyValue) throws Exception {
        if (isBlank(jsonObject)) {
            throw new Exception("Empty jsonObject provided!");
        }
        if (isBlank(jsonPath)) {
            throw new Exception("Empty jsonPath provided!");
        }
        if (isBlank(action)) {
            throw new Exception("Empty action provided!");
        }

        final String actionString = action.toLowerCase().trim();
        checkForNullPropertyValue(actionString, propertyValue, update);
        checkForNullPropertyValue(actionString, propertyValue, add);
        checkForNullPropertyValue(actionString, propertyValue, insert);

        if (actionString.equals(insert.getValue())) {
            if (isBlank(propertyName)) {
                throw new Exception("Empty propertyName provided for insert action!");
            }
        }

        boolean exists = false;
        String actionEnumValues = "";
        for (ActionsEnum value : ActionsEnum.values()) {
            final String actionEnumValue = value.getValue();
            actionEnumValues += actionEnumValue + " ";
            if (actionString.equals(actionEnumValue)) {
                exists = true;
            }
        }
        if (!exists) {
            throw new Exception("Invalid action provided! Action should be one of the values: " + actionEnumValues);
        }
    }

    private static void checkForNullPropertyValue(String actionString, String propertyValue, ActionsEnum actionEnum) throws Exception {
        if (actionString.equals(actionEnum.getValue())) {
            if (propertyValue == null) {
                throw new Exception("Null propertyValue provided for " + actionEnum.getValue() + " action!");
            }
        }
    }
}
