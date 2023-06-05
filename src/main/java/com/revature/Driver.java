package com.revature;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.Javalin;

import java.util.*;

public class Driver {

    public static Map<String, Integer> names = new HashMap<>();



    public static void main(String[] args) {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        names.put("bryan", 1);
        names.put("ben", 10);


        var app = Javalin.create(/*config*/)
                .get("/names", ctx -> {
                    ctx.header("ContentType", "application/json");
                    ctx.status(200);
                    ctx.result(gson.toJson(names.keySet()));
                })
                .post("/names", ctx ->{
                    ctx.header("ContentType", "application/json");
                    String[] text = ctx.body().toLowerCase().split(" ");
                    String firstName = text[0];
                    if (names.keySet().contains(firstName)){
                        names.put(firstName, names.get(firstName) + 1);
                        ctx.result("Added 1 to count for name: " + firstName);
                    } else{
                        names.put(firstName, 1);
                        ctx.result("Added " + firstName + " to our server list with a count of 1");
                    }

                    ctx.status(201);
                })
                .get("/names/{name}", ctx ->{
                    String nameSearch = ctx.pathParam("name");
                    if (names.keySet().contains(nameSearch)){
                        ctx.result(gson.toJson(nameSearch +": " + names.get(nameSearch)));
                    } else{
                        ctx.status(404);
                        ctx.result("Couldn't find that name in our server!");
                    }
                })
                .delete("/names/{name}", ctx -> {
                    String nameSearch = ctx.pathParam("name");
                    if (names.keySet().contains(nameSearch)){
                        names.remove(nameSearch);
                        ctx.result(gson.toJson("Successfully removed name: " + nameSearch));
                    } else{
                        ctx.status(404);
                        ctx.result("Couldn't find that name in our server!");
                    }
                })
                .patch("/adminreset", ctx ->{
                    names = new HashMap<>();
                    names.put("bryan", 1);
                    names.put("ben", 10);
                    ctx.status(200);
                    ctx.result("Set Back to normal");
                })
                .start(7070);
    }
}
