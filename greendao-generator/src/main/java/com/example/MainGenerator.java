package com.example;
import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MainGenerator {

    private static final String PROJECT_DIR = System.getProperty("user.dir");

    public static void main(String[] args) {

        Schema schema = new Schema(1, "com.amelendez.lgo.storage.dao");
        schema.enableKeepSectionsByDefault();
        addTables(schema);
        try {
            new DaoGenerator().generateAll(schema, PROJECT_DIR + "\\app\\src\\main\\java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(final Schema schema) {
        Entity languo = addLanguo(schema);
    }

    private static Entity addLanguo(final Schema schema)
    {
        Entity languo = schema.addEntity("Languo");
        languo.addIdProperty().primaryKey().autoincrement();
        languo.addStringProperty("term").notNull().unique();
        languo.addStringProperty("definition").notNull();
        languo.addStringProperty("example");
        languo.addIntProperty("hits").notNull();
        languo.addFloatProperty("level").notNull();
        languo.addDateProperty("insertDate");
        languo.addDateProperty("changedDate");
        return languo;
    }
}
