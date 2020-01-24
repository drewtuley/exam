package com.feedme.exam.queue.write.feed;


import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class FeedmeClient {
    private Socket clientSocket;
    private BufferedReader in;
    private static final Pattern NOT_SLASH_PIPE = Pattern.compile("(?<!\\\\)[|]");
    private Map<String, FeedmeType> typeMap;

    public void startConnection(String host, int feedPort, int typesPort) throws IOException {
        this.clientSocket = new Socket(host, feedPort);
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        FeedmeTypeFactory ftFactory = FeedmeTypeFactory.getInstance();
        URL typesUrl = new URL("http", host, typesPort, "/types");
        this.typeMap = ftFactory.getFeedmeTypes(typesUrl.toString());
    }


    public void stopConnection() throws IOException {
        this.in.close();
        this.clientSocket.close();
    }


    public static void main(String[] args) {
        FeedmeClient client = new FeedmeClient();
        try {
            client.startConnection("localhost", 8282, 8181);
            String message;
            while ((message = client.getNextJson()) != null) {
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonObject convertToJson(List<String> fields, Map<String, FeedmeType> typeMap) {
        String type = fields.get(2);
        JsonObject json = null;
        FeedmeType feedmeType = typeMap.get(type);
        if (feedmeType != null) {
            json = feedmeType.buildJson(fields);

        }
        return json;
    }

    private List<String> parseInput(String inputLine) {
        String sanitised = inputLine.replaceFirst("^\\|", "");
        List<String> fields = Arrays.asList(sanitised.split(NOT_SLASH_PIPE.pattern()));

        return fields;
    }


    public String getNextJson() throws IOException {
        String in_data = this.in.readLine();
        if (in_data != null) {
            return this.convertToJson(parseInput(in_data), this.typeMap).toString();
        }
        return null;
    }
}
