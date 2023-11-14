package Controller

import Model.CandidatoModel
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class ApiUtil {

    static JsonNode readJsonRequestBody(HttpServletRequest req) {
        StringBuilder requestBody = new StringBuilder()

        try (BufferedReader reader = req.getReader()) {
            String line
            while ((line = reader.readLine()) != null) {
                requestBody.append(line)
            }
        }

        ObjectMapper objectMapper = new ObjectMapper()
        return objectMapper.readTree(requestBody.toString())
    }

    static List<String> extractValuesFromJson(JsonNode jsonNode) {
        List<String> values = []
        jsonNode.fieldNames().forEachRemaining { fieldName ->
            values.add(jsonNode.get(fieldName).asText())
        }
        return values
    }

    static String convertListToJson(List<List<String>> listOfLists) {
        ObjectMapper objectMapper = new ObjectMapper()
        return objectMapper.writeValueAsString(listOfLists)
    }

    static void sendJsonResponse(HttpServletResponse resp, String data){
        resp.setContentType("application/json")
        resp.setCharacterEncoding("UTF-8")
        resp.getWriter().write(data)
    }

    private static String convertToJSON(List<CandidatoModel> candidatos) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(candidatos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
