import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApiClient {
    public static void main(String[] args) {
        String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=12.97&longitude=77.59&current_weather=true";

        // A small sample JSON used when network is not available (or blocked)
        String fallbackJson = "{\"latitude\":12.97,\"longitude\":77.59,\"generationtime_ms\":0.3,"
            + "\"utc_offset_seconds\":0,\"timezone\":\"GMT\",\"timezone_abbreviation\":\"GMT\","
            + "\"elevation\":900.0,\"current_weather\":{\"temperature\":26.7,\"windspeed\":8.5,"
            + "\"winddirection\":140.0,\"weathercode\":0,\"time\":\"2025-09-10T09:00\"}}";

        String json = null;

        // Try to fetch live data; if anything fails, we'll use fallbackJson
        try {
            json = fetchUrl(apiUrl);
            if (json == null || json.isEmpty()) {
                System.out.println("⚠️ Empty response from API — using fallback sample JSON.");
                json = fallbackJson;
            } else {
                System.out.println("✅ Fetched live data from API.");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not fetch live API (network blocked or timed out). Using fallback sample JSON.");
            // Uncomment next line to see the exception in debugging environments:
            // e.printStackTrace();
            json = fallbackJson;
        }

        // Extract the `current_weather` object
        String currentWeatherJson = extractJsonObject(json, "current_weather");
        if (currentWeatherJson == null) {
            System.out.println("❌ Could not find current_weather in JSON.");
            System.out.println("Raw JSON:");
            System.out.println(json);
            return;
        }

        double temperature = extractDouble(currentWeatherJson, "temperature");
        double windspeed = extractDouble(currentWeatherJson, "windspeed");
        String time = extractString(currentWeatherJson, "time");

        System.out.println();
        System.out.println("📍 Location: Bangalore");
        System.out.println("🌡 Temperature: " + (Double.isNaN(temperature) ? "N/A" : temperature + " °C"));
        System.out.println("💨 Wind Speed: " + (Double.isNaN(windspeed) ? "N/A" : windspeed + " km/h"));
        System.out.println("🕒 Time: " + (time == null ? "N/A" : time));
    }

    // Performs a GET request and returns the response body as a String.
    private static String fetchUrl(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000); // 5 seconds
        conn.setReadTimeout(5000);    // 5 seconds
        conn.setRequestProperty("User-Agent", "Java Weather Client");

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP response code: " + responseCode);
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }

    // Extracts a JSON object string for a given key (e.g. current_weather) including braces.
    // Returns null if key not found or braces mismatch.
    private static String extractJsonObject(String json, String key) {
        String quotedKey = "\"" + key + "\"";
        int idx = json.indexOf(quotedKey);
        if (idx == -1) return null;
        int colon = json.indexOf(':', idx);
        if (colon == -1) return null;
        int firstBrace = json.indexOf('{', colon);
        if (firstBrace == -1) return null;

        int count = 0;
        for (int i = firstBrace; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') count++;
            else if (c == '}') count--;
            if (count == 0) {
                return json.substring(firstBrace, i + 1);
            }
        }
        return null; // unmatched braces
    }

    // Extracts a double value inside a JSON object string for a given key (no outer JSON search).
    private static double extractDouble(String jsonObj, String key) {
        String quotedKey = "\"" + key + "\"";
        int idx = jsonObj.indexOf(quotedKey);
        if (idx == -1) return Double.NaN;
        int colon = jsonObj.indexOf(':', idx);
        if (colon == -1) return Double.NaN;
        int end = jsonObj.indexOf(',', colon + 1);
        if (end == -1) end = jsonObj.indexOf('}', colon + 1);
        if (end == -1) return Double.NaN;
        String numStr = jsonObj.substring(colon + 1, end).trim();
        // remove surrounding quotes if any (unlikely for numbers)
        if (numStr.startsWith("\"") && numStr.endsWith("\"")) {
            numStr = numStr.substring(1, numStr.length() - 1);
        }
        try {
            return Double.parseDouble(numStr);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    // Extracts a string value inside a JSON object string for a given key.
    private static String extractString(String jsonObj, String key) {
        String quotedKey = "\"" + key + "\"";
        int idx = jsonObj.indexOf(quotedKey);
        if (idx == -1) return null;
        int colon = jsonObj.indexOf(':', idx);
        if (colon == -1) return null;
        int firstQuote = jsonObj.indexOf('"', colon + 1);
        if (firstQuote == -1) {
            // value is not quoted — read until comma or closing brace
            int end = jsonObj.indexOf(',', colon + 1);
            if (end == -1) end = jsonObj.indexOf('}', colon + 1);
            if (end == -1) return null;
            return jsonObj.substring(colon + 1, end).trim();
        } else {
            int secondQuote = jsonObj.indexOf('"', firstQuote + 1);
            if (secondQuote == -1) return null;
            return jsonObj.substring(firstQuote + 1, secondQuote);
        }
    }
}
