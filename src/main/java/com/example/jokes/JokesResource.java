package com.example.jokes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("")
public class JokesResource {
    @GET
    @Path("/get100")
    @Produces("application/json")
    public JSONArray get100() throws IOException {
        Integer count=1;
        String[] id_url = new String[0];
        JSONArray bashorg = new JSONArray();
        ArrayList<String> joke_lines = null;

        URL url = new URL("http://bash.org.pl/text");
        URLConnection urlConnection = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        String line = br.readLine();
        while ( line != null ) {
            if (count > 100) { break; }
            if (line.matches("^#.*")) {
                id_url = (line.split(" "));
                joke_lines = new ArrayList<String>();
            } else if (line.matches("^%.*")) {
                count++;
                JSONObject joke_obj = new JSONObject();
                JSONArray joke_body = new JSONArray();
                joke_obj.put("link", id_url[1].replaceAll("\\)|\\(", ""));
                joke_obj.put("id", id_url[0].replace("#", ""));

                joke_body.add(joke_lines);
                joke_obj.put("joke", joke_body);

                bashorg.add(joke_obj);
            } else {
                joke_lines.add(line);
            }
            line = br.readLine();
        }
        br.close();

        return bashorg;
    }

    @GET
    @Path("/{id}/get")
    @Produces("application/json")
    public JSONArray getId(@PathParam("id") int id) {
        JSONArray bashorg = new JSONArray();
        JSONObject joke_obj = new JSONObject();
        JSONArray joke_body = new JSONArray();

        joke_obj.put("id", id);
        joke_obj.put("link", "http://bash.org.pl/"+id);
        joke_body.add("he-he-he");
        joke_obj.put("joke", joke_body);
        bashorg.add(joke_obj);

        return bashorg;
    }

    @GET
    @Path("/getAll")
    @Produces("application/json")
    public String getAll() {
        return "Hello";
    }
}