package com.example;
import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MainGenerator {

    private static final String PROJECT_DIR = System.getProperty("user.dir");

    public static void main(String[] args) {

        Schema schema = new Schema(1, "com.amelendez.lgo.storage.daos");
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
        Entity context = addContextDescription(schema);
        Entity meaning = addMeaning(schema, languo, context);
        Entity example = addExample(schema, meaning);
    }

    private static Entity addLanguo(final Schema schema)
    {
        /*
        * Languo -> subject that is to be learned
        * id                : int    :  primary key with auto-increment
        * value             : String :  value of the languo
        * language          : String :  language that is being learned
        * score             : float  :  calculated score
        * hits              : int    :  calculated number of hits
        * type              : int    :  (1) single term, (2) sentence or phrase, (3) idiom
        * insertDate        : Date   :  date item was inserted
        * updateDate        : Date   :  date item was modified
        * meanings          : ref    :  foreign relationship (1 ... *)
        * */
        Entity languo = schema.addEntity("Languo");
        languo.addIdProperty().primaryKey().autoincrement();
        languo.addStringProperty("value").notNull().unique();
        languo.addStringProperty("language").notNull();
        languo.addFloatProperty("score").notNull();
        languo.addIntProperty("hits").notNull();
        languo.addIntProperty("type").notNull();
        languo.addDateProperty("insertDate");
        languo.addDateProperty("updateDate");
        return languo;
    }

    private static Entity addContextDescription(final Schema schema)
    {
        /*
        * Context -> provides a context description to a given Meaning
        * id                : int    : primary key with auto-increment
        * value             : String : value of the context description
        * language          : String : language that user understands
        * */
        Entity context = schema.addEntity("ContextDescription");
        context.addIdProperty().primaryKey().autoincrement();
        context.addStringProperty("value").notNull().unique();
        context.addStringProperty("language").notNull();
        return context;
    }

    private static Entity addMeaning(final Schema schema, Entity languo, Entity contextDescription)
    {
        /*
        * Meaning -> provides the meaning of a Languo
        * id                : int    : primary key with auto-increment
        * value             : String : value of the meaning
        * language          : String : language that the user understands
        * score             : float  : user score (or auto-calculated) for understanding of meaning
        * hits              : int    : total hits to this meaning
        * isDefault         : bool   : indication that it is the default meaning
        * languoId          : ref    : reference to the source languo this meaning pertains to
        * contextDesc...Id  : ref    : reference to the given contextDescription for this meaning
        * */
        Entity meaning = schema.addEntity("Meaning");
        meaning.addIdProperty().primaryKey().autoincrement();
        meaning.addStringProperty("value").notNull();
        meaning.addStringProperty("language").notNull();
        meaning.addFloatProperty("score").notNull();
        meaning.addIntProperty("hits").notNull();
        meaning.addBooleanProperty("isDefault").notNull();

        // Adding languo 1 ... * relation
        Property languoId = meaning.addLongProperty("languoId").notNull().getProperty();
        ToMany languoToMeanings = languo.addToMany(meaning, languoId);
        languoToMeanings.setName("meanings");

        // Adding contextDescription 1 ... 1 relation
        Property contextId = meaning.addLongProperty("contextDescriptionId").getProperty();
        meaning.addToOne(contextDescription, contextId);
        return meaning;
    }

    private static Entity addExample(final Schema schema, Entity meaning)
    {
        /*
        * Example -> provides a use example to the Meaning for a Languo
        * id                : int    : primary key with auto-increment
        * value             : String : value of the example
        * language          : String : language that the user understands
        * isDefault         : bool   : indication that it is the default example
        * meaningId         : ref    : reference to source meaning
        * */
        Entity example = schema.addEntity("Example");
        example.addIdProperty().primaryKey().autoincrement();
        example.addStringProperty("value").notNull();
        example.addStringProperty("language").notNull();
        example.addBooleanProperty("isDefault").notNull();

        // Adding languo 1 ... * relation
        Property meaningId = example.addLongProperty("meaningId").notNull().getProperty();
        ToMany meaningToExamples = meaning.addToMany(example, meaningId);
        meaningToExamples.setName("examples");
        return example;
    }
}
