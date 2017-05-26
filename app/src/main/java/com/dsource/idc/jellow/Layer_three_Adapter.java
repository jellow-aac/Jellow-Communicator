package com.dsource.idc.jellow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by Sumeet on 19-04-2016.
 */

public class Layer_three_Adapter extends android.support.v7.widget.RecyclerView.Adapter<Layer_three_Adapter.MyViewHolder>{
    private Context mContext;
    //private int layer_1_id, layer_2_id;
    public static Integer[] mThumbIds = new Integer[100];
    public static String[] belowText = new String[100];
    public static int more = 0;
    private SessionManager session;
CircularImageView img1,img2,img3, img4,img5,img6, img7,img8,img9;
    TextView tv1,tv2,tv3, tv4,tv5,tv6, tv7,tv8,tv9;
    LinearLayout mLinearLayoutIconOne, mLinearLayoutIconTwo, mLinearLayoutIconThree;
    // Keep all Images in array

    public Integer[] greet_feel_greetings = {
            R.drawable.hi, R.drawable.hello,
            R.drawable.bye, R.drawable.goodmorning,
            R.drawable.goodafternoon, R.drawable.goodevening, R.drawable.goodnight, R.drawable.highfive,     R.drawable.nicetomeetyou, R.drawable.howareyou, R.drawable.howwasyourday, R.drawable.howareyou
    };

    public static String[] greet_feel_greetings_text =
            {"Hi", "Hello", "Bye", "Good morning", "Good afternoon", "Good evening", "Good night", "Hi-five",   "Nice to meet you", "How are you?", "How was your day?", "How do you do?"};

    public Integer[] greet_feel_feelings = {
            R.drawable.happy, R.drawable.sad,
            R.drawable.angry, R.drawable.scared,
            R.drawable.surprised, R.drawable.irritated, R.drawable.confused, R.drawable.ashamed,     R.drawable.disappointed, R.drawable.bored,
            R.drawable.worried, R.drawable.stressed,
            R.drawable.tired, R.drawable.hot, R.drawable.cold, R.drawable.sick, R.drawable.hurt
    };

    public static String[] greet_feel_feelings_text =
            {"Happy", "Sad", "Angry", "Afraid", "Amazed", "Irritated", "Confused", "Ashamed",   "Disappointed", "Bored", "Worried", "Stressed", "Tired", "Hot", "Cold", "Sick", "Hurt"};

    public Integer[] greet_feel_requests = {
            R.drawable.please, R.drawable.thankyou,
            R.drawable.welcome, R.drawable.pleasegiveme,
            R.drawable.pleasetellmeagain, R.drawable.pleaseshowme, R.drawable.ineedabreak, R.drawable.iamalldone,     R.drawable.excuseme,
            R.drawable.iamsorry, R.drawable.idontunderstand,
            R.drawable.pleaseshare, R.drawable.pleaseslowdown, R.drawable.ineedhelp, R.drawable.comehere, R.drawable.takeme
    };

    public static String[] greet_feel_requests_text =
            {"Please", "Thank you", "You are welcome", "Please give me", "Please tell me again", "Please show me", "I need a break", "I am all done",   "Excuse me", "I am sorry", "I don’t understand", "Please share", "Please slow down", "I need help", "Please come here", "Please take me"};

    public Integer[] greet_feel_questions = {
            R.drawable.how, R.drawable.when,
            R.drawable.where, R.drawable.why,
            R.drawable.what,R.drawable.who, R.drawable.howmuch, R.drawable.howmany, R.drawable.howlong
    };

    public static String[] greet_feel_questions_text =
            {"How?", "When?", "Where?", "Why?", "What?", "Who?", "How much?", "How many?", "How long?"};

    public Integer[] daily_activities_brushing = {
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient,R.drawable.gradient, R.drawable.gradient, R.drawable.gradient
    };

    public static String[] daily_activities_brushing_text =
            {"Rinse mouth", "Rinse toothbrush", "Put toothpaste on brush", "Brush front teeth", "Brush back teeth", "Brush tongue", "Rinse mouth", "All done"};

    public Integer[] daily_activities_toilet = {
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient,R.drawable.gradient, R.drawable.gradient
    };

    public static String[] daily_activities_toilet_text =
            {"Pull pants down", "Sit on toilet", "Wash bottom", "Flush toilet", "Pull pants up", "Wash hands", "All done"};

    public Integer[] daily_activities_bathing = {
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient,R.drawable.gradient, R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient,
            R.drawable.gradient, R.drawable.gradient, R.drawable.gradient, R.drawable.gradient, R.drawable.gradient
    };

    public static String[] daily_activities_bathing_text =
            {"Remove clothes", "Turn on water", "Get in the shower", "Wet body", "Put soap", "Shampoo hair", "Put face wash", "Wash hair",   "Wash body", "Turn off water", "Dry hair", "Dry face", "Dry body", "Put on clothes", "All done"};

    public Integer[] daily_activities_clothes_access = {
            R.drawable.tshirt_clothing, R.drawable.dress_clothing,
            R.drawable.skirt_clothing, R.drawable.jeans_clothing,
            R.drawable.pants_clothing,R.drawable.leggings, R.drawable.slacks_clothing, R.drawable.shorts,     R.drawable.underwear_clothing, R.drawable.slippers_clothing,
            R.drawable.shoes_clothing, R.drawable.socks_school,
            R.drawable.nightwear_clothing,R.drawable.shirt, R.drawable.tshirt_clothing, R.drawable.dress_clothing,     R.drawable.pants_clothing, R.drawable.slacks_clothing,
            R.drawable.leggings, R.drawable.shorts,
            R.drawable.salwarkhamez_clothing,R.drawable.sweater_clothing, R.drawable.jcket_clothing, R.drawable.scarf_clothing,     R.drawable.cap_clothing, R.drawable.belt_clothing,
            R.drawable.raincoat_clothing, R.drawable.spectacles_clothing,
            R.drawable.watch_clothing,R.drawable.earings_clothing, R.drawable.bracelet_clothing, R.drawable.necklace_clothing,     R.drawable.bindi,R.drawable.slippers_clothing, R.drawable.myclothesaretight, R.drawable.myclothesareloose, R.drawable.ineedhelpremovingclothes, R.drawable.ineedhelpputtingclotheson,
    };

    public static String[] daily_activities_clothes_access_text =
            {"Change t-shirt", "Change frock", "Change skirt", "Change jeans", "Change pants", "Change leggings", "Change slacks", "Change shorts",   "Change innerwear", "Change footwear", "Change shoes", "Change socks", "Wear night clothes", "Shirt", "T-shirt", "Frock",   "Pants", "Slacks", "Leggings", "Shorts", "Salwar kameez", "Sweater", "Jacket", "Scarf",   "Cap", "Belt", "Raincoat", "Spectacles", "Wristwatch", "Earrings", "Bracelet", "Necklace",   "Bindi", "Chappals", "My clothes are tight", "My clothes are loose", "Help remove clothes", "Help put on clothes"};

    public Integer[] daily_activities_get_ready = {
            R.drawable.combhair, R.drawable.facewash,
            R.drawable.cuttingnails, R.drawable.blowingnose,
            R.drawable.soap, R.drawable.shampoo
    };

    public static String[] daily_activities_get_ready_text =
            {"Comb hair", "Face wash", "Cut nails", "Blow nose", "Soap", "Shampoo"};

    public Integer[] daily_activities_sleep = {
            R.drawable.door_homeobjects, R.drawable.fan,
            R.drawable.light, R.drawable.window,
            R.drawable.bed,R.drawable.pilllow, R.drawable.blanket, R.drawable.feelinghot, R.drawable.feelingcold
    };

    public static String[] daily_activities_sleep_text =
            {"Door", "Fan", "Light", "Window", "Bed", "Pillows", "Blanket", "Feeling warm", "Feeling cold"};

    public Integer[] daily_activities_therapy = {
            R.drawable.exercise, R.drawable.swing,
            R.drawable.trampoline, R.drawable.swissball,
            R.drawable.blanket,R.drawable.ballpit, R.drawable.handmovements, R.drawable.legexercises, R.drawable.bodyvest
    };

    public static String[] daily_activities_therapy_text =
            {"Exercises", "Swing", "Trampoline", "Swiss ball", "Blanket", "Ball pit", "Hand activities", "Leg exercises", "Body vests"};

    public Integer[] daily_activities_morning_schedule = {
            R.drawable.wakeup, R.drawable.washface,
            R.drawable.gototoilet, R.drawable.brushteeth,
            R.drawable.removeclothes,R.drawable.haveabath, R.drawable.getdressed, R.drawable.combhair,
            R.drawable.eatbreakfast,R.drawable.packlunch, R.drawable.packbagpack, R.drawable.gradient, R.drawable.haveagreatday
    };

    public static String[] daily_activities_morning_schedule_text =
            {"Wake up", "Wash face", "Go to bathroom", "Brush teeth", "Remove clothes", "Have a bath", "Get dressed", "Comb hair",   "Eat breakfast", "Pack lunchbox", "Pack school bag", "Go to school", "Have a great day!"};

    public Integer[] daily_activities_bedtime_schedule = {
            R.drawable.eatdinner, R.drawable.wearnightsuit,
            R.drawable.brushteeth, R.drawable.readstory,
            R.drawable.saygoodnight,R.drawable.sayprayres, R.drawable.sweetdreams
    };

    public static String[] daily_activities_bedtime_schedule_text =
            {"Eat dinner", "Wear night clothes", "Brush teeth", "Read story", "Say goodnight", "Say prayers", "Sweet dreams!"};

    public Integer[] eating_foods_drinks_breakfast = {
            R.drawable.bread, R.drawable.cornflakes,
            R.drawable.aloopuri, R.drawable.eggs,
            R.drawable.poha,R.drawable.upma, R.drawable.khichdi, R.drawable.idli,     R.drawable.dosa, R.drawable.paratha,
            R.drawable.omelette, R.drawable.meduvada,
            R.drawable.porriage,R.drawable.sandwich, R.drawable.chutney, R.drawable.sambar, R.drawable.uttapam
    };

    public static String[] foods_drinks_breakfast_text =
            {"Bread", "Cornflakes", "Aaloo Poori", "Eggs", "Poha", "Upma", "Khichdi", "Idli",   "Dosa", "Paratha", "Omlette", "Medu wada", "Porridge", "Sandwich", "Chutney", "Sambhar", "Utappam"};

    public Integer[] eating_food_drinks_lunch_dinner = {
            R.drawable.roti, R.drawable.sabzi,
            R.drawable.rice, R.drawable.dal,
            R.drawable.dalkhichdi,R.drawable.raita,
            R.drawable.paratha, R.drawable.curd,     R.drawable.fish, R.drawable.chicken,
            R.drawable.pork, R.drawable.mutton,
            R.drawable.crab,R.drawable.turkey, R.drawable.pizza, R.drawable.salad,     R.drawable.soup, R.drawable.pasta,
            R.drawable.noodles,
            R.drawable.italian,R.drawable.pavbhaji,
            R.drawable.bhakri
    };

    public static String[] food_drinks_lunch_dinner_text =
            {"Roti", "Sabzi", "Rice", "Dal", "Dal khichdi", "Raita", "Paratha", "Curd",   "Fish", "Chicken", "Pork", "Mutton", "Crab meat", "Turkey", "Pizza", "Salad",   "Soup", "Pasta", "Noodles", "Italian", "Pav bhaji", "Bhakri"};

    public Integer[] eating_food_drinks_sweets = {
            R.drawable.cake, R.drawable.icecream,
            R.drawable.gajarhalwa, R.drawable.gulabjamun,
            R.drawable.ladoo,R.drawable.barfi,
            R.drawable.jalebi, R.drawable.fruitsalad,     R.drawable.rasgulla, R.drawable.sheera
    };

    public static String[] food_drinks_sweets_text =
            {"Cake", "Icecream", "Gajar halwa", "Gulab jamun", "Laddoo", "Barfi", "Jalebi", "Fruit salad",   "Rosogulla", "Sheera"};

    public Integer[] eating_food_drinks_snacks = {
            R.drawable.biscuits, R.drawable.chaat,
            R.drawable.chocolate, R.drawable.chips,
            R.drawable.sandwich,R.drawable.noodles,
            R.drawable.cheese, R.drawable.peanuts
    };

    public static String[] food_drinks_snacks_text =
            {"Biscuits", "Chaat", "Chocolate", "Wafers", "Sandwich", "Noodles", "Cheese", "Nuts"};

    public Integer[] eating_food_drinks_fruits = {
            R.drawable.apple, R.drawable.banana,
            R.drawable.grapes, R.drawable.guaua,
            R.drawable.mango,R.drawable.orange,
            R.drawable.pineapple, R.drawable.strawberry,     R.drawable.blueberry, R.drawable.pomogranate,
            R.drawable.watermelon, R.drawable.pear,
            R.drawable.papaya,R.drawable.muskmelon,
            R.drawable.chikoo, R.drawable.jackfruit, R.drawable.cherry
    };

    public static String[] food_drinks_fruits_text =
            {"Apple", "Banana", "Grapes", "Guava", "Mango", "Orange", "Pineapple", "Strawberry",   "Blueberry", "Pomegranate", "Watermelon", "Pear", "Papaya", "Muskmelon", "Chikoo", "Jackfruit", "Cherry"};

    public Integer[] eating_food_drinks_drinks = {
            R.drawable.water, R.drawable.milk,
            R.drawable.bournvita,
            R.drawable.mangojuice, R.drawable.applejuice,
            R.drawable.orangejuice, R.drawable.lemonjuice, R.drawable.pineapplejuice,     R.drawable.pepsi,
            R.drawable.cocacola, R.drawable.mirinda,
            R.drawable.fanta, R.drawable.mazzaa,
            R.drawable.sprite, R.drawable.mountaindew, R.drawable.milkshakes,     R.drawable.chocolatemilkshake,
            R.drawable.strawberrymilkshake, R.drawable.bananamilkshake,
            R.drawable.mangomilkshake, R.drawable.chikoomilkshake,
            R.drawable.tea, R.drawable.coffee, R.drawable.coldcoffee, R.drawable.energydrink
    };

    public static String[] food_drinks_drinks_text =
            {"Water", "Milk", "Bournvita", "Mango juice", "Apple juice", "Orange juice", "Lemon juice", "Pineapple juice",   "Pepsi", "Coca cola", "Mirinda", "Fanta", "Mazaa", "Sprite", "Mountain dew","Milkshake",   "Chocolate milkshake", "Strawberry milkshake", "Banana milkshake", "Mango milkshake", "Chikoo milkshake", "Tea", "Coffee", "Cold coffee", "Energy drink"};

    public Integer[] eating_food_drinks_cutlery = {
            R.drawable.bowl_cutlery, R.drawable.plate_cutlery,
            R.drawable.spoon_cutlery, R.drawable.fork_cutlery,
            R.drawable.knife_cutlery,R.drawable.mug_cutlery, R.drawable.cup_cutlery, R.drawable.glass_cutlery
    };

    public static String[] food_drinks_cutlery_text =
            {"Bowl", "Plate", "Spoon", "Fork", "Knife", "Mug", "Cup", "Glass"};

    public Integer[] eating_food_drinks_add_ons = {
            R.drawable.butter_addons, R.drawable.jam_addons,
            R.drawable.salt_addons, R.drawable.pepper_addons,
            R.drawable.sugar_addons,R.drawable.ketchup_addons,
            R.drawable.pickle, R.drawable.papad_addons, R.drawable.masala_addons
    };

    public static String[] food_drinks_add_ons_text =
            {"Butter", "Jam", "Salt", "Pepper", "Sugar", "Sauce", "Pickle", "Papad", "Masala"};

    public Integer[] fun_indoor_games = {
            R.drawable.puzzles, R.drawable.boradgames,
            R.drawable.blocks, R.drawable.lego,
            R.drawable.chess, R.drawable.snakesandladders, R.drawable.scrabble, R.drawable.videogames,     R.drawable.doll, R.drawable.actionfigure,
            R.drawable.teddy, R.drawable.cars,
            R.drawable.truck, R.drawable.artandcraft_activities, R.drawable.playwithme
    };

    public static String[] fun_indoor_games_text =
            {"Puzzle", "Board game", "Blocks", "Legos", "Chess", "Snakes & Ladders", "Scrabble", "Videogame",   "Doll", "Action figure", "Soft toy", "Car", "Truck", "Art and craft", "Play with me"};

    public Integer[] fun_outdoor_games = {
            R.drawable.level2_places_playground, R.drawable.level2_places_park, R.drawable.swing,
            R.drawable.slide, R.drawable.seesaw, R.drawable.merrygoround, R.drawable.hideandseek, R.drawable.batandball,     R.drawable.statue,
            R.drawable.lockandkey, R.drawable.catchcatch,
            R.drawable.kite, R.drawable.choorpolice, R.drawable.marbles, R.drawable.walking, R.drawable.cycle_transport,     R.drawable.running, R.drawable.swimming

    };

    public static String[] fun_outdoor_games_text =
            {"Playground", "Park", "Swing", "Slide", "See-saw", "Merry-go-round", "Hide and seek", "Bat and ball",   "Statue", "Lock and key", "Catch-catch", "Kite", "Chor-police", "Marbles", "Walk", "Cycle",   "Run", "Swim"};

    public Integer[] fun_sports = {
            R.drawable.cricket_activities, R.drawable.badminton_activities,
            R.drawable.tennis_activities, R.drawable.basketball_activities,
            R.drawable.dodgeball_activities, R.drawable.vollyball_activities, R.drawable.khokho, R.drawable.football_activities,     R.drawable.kabaddi, R.drawable.gymnastics, R.drawable.swimming
    };

    public static String[] fun_sports_text =
            {"Cricket", "Badminton", "Tennis", "Basketball", "Dodgeball", "Volleyball", "Kho-kho", "Football",   "Kabaddi", "Gymnastics", "Swimming"};

    public Integer[] fun_tv = {
            R.drawable.nextchannel, R.drawable.previouschannel,
            R.drawable.volumeup, R.drawable.volumedown
    };

    public static String[] fun_tv_text =
            {"Next channel", "Previous channel", "Volume up", "Volume down"};

    public Integer[] fun_music = {
            R.drawable.changemusic, R.drawable.dancing,
            R.drawable.volumeup, R.drawable.volumedown
    };

    public static String[] fun_music_text =
            {"Change music", "Lets dance", "Volume up", "Volume down"};

    public Integer[] fun_activities = {
            R.drawable.draw_activities, R.drawable.painting_activities,
            R.drawable.read_activities, R.drawable.write_activities,
            R.drawable.artandcraft_activities,R.drawable.drama_activities, R.drawable.dancing, R.drawable.musicalinstrument_activities
    };

    public static String[] fun_activities_text =
            {"Draw", "Paint", "Read", "Write", "Arts and crafts", "Drama", "Dance", "Make music"};

    public Integer[] learning_animals_birds = {
            R.drawable.dog_animals, R.drawable.cat_animals,
            R.drawable.elephant_animals, R.drawable.lion_animals,
            R.drawable.parrot,R.drawable.rabbit_animals, R.drawable.cow_animals,R.drawable.duck_animals,     R.drawable.donkey, R.drawable.ant,
            R.drawable.tiger, R.drawable.monkey,
            R.drawable.pigeon,R.drawable.cockroach, R.drawable.crow,R.drawable.horse,     R.drawable.deer, R.drawable.owl,
            R.drawable.wolf, R.drawable.fox,
            R.drawable.bear,R.drawable.sheep, R.drawable.goat,R.drawable.pig,     R.drawable.fly, R.drawable.giraffee,
            R.drawable.zebra, R.drawable.mosquito,
            R.drawable.buffallo,R.drawable.mouse, R.drawable.snake,R.drawable.crocodile,     R.drawable.honeybee, R.drawable.hippo,
            R.drawable.rhino, R.drawable.fish,
            R.drawable.penguin,R.drawable.seal, R.drawable.dolphin,R.drawable.whale,     R.drawable.shark,R.drawable.tortoise, R.drawable.sparrow,R.drawable.eagle, R.drawable.hawk, R.drawable.vulture
    };

    public static String[] learning_animals_birds_text =
            {"Dog", "Cat", "Elephant", "Lion", "Parrot", "Rabbit", "Cow", "Duck",   "Donkey", "Ant", "Tiger", "Monkey", "Pigeon", "Cockroach", "Crow", "Horse",   "Deer","Owl", "Wolf", "Fox", "Bear", "Sheep", "Goat", "Pig",   "Fly", "Giraffe", "Zebra", "Mosquito", "Buffalo", "Mouse", "Snake", "Crocodile",   "Bee", "Hippopotamus", "Rhinoceros", "Fish", "Penguin", "Seal", "Dolphin", "Whale",   "Shark", "Tortoise", "Sparrow", "Eagle", "Hawk", "Vulture"};

    public Integer[] learning_body_parts = {
            R.drawable.head, R.drawable.hair_bodyparts,
            R.drawable.eye_bodyparts, R.drawable.nose_bodyparts,
            R.drawable.ear_bodyparts,R.drawable.mouth_bodyparts, R.drawable.tongue, R.drawable.neck,     R.drawable.shoulder, R.drawable.elbow,
            R.drawable.wrist, R.drawable.hand_bodyparts,
            R.drawable.fingers,R.drawable.back, R.drawable.stomach_bodyparts, R.drawable.hips,     R.drawable.knee_bodyparts, R.drawable.ankle, R.drawable.legs, R.drawable.toes
    };

    public static String[] learning_body_parts_text =
            {"Head", "Hair", "Eyes", "Nose", "Ears", "Mouth", "Tongue", "Neck",   "Shoulder", "Elbow", "Wrist", "Hands", "Fingers", "Back", "Stomach", "Hip",   "Knee", "Ankle", "Legs", "Toes"};

    public Integer[] learning_books = {
            R.drawable.bedtimebook, R.drawable.comics_books,
            R.drawable.rhymebook, R.drawable.drawingbook_books,
            R.drawable.storybook,R.drawable.picturebook, R.drawable.mysterybook, R.drawable.adventurebook,     R.drawable.notebook_books, R.drawable.maths,
            R.drawable.science_books, R.drawable.history,
            R.drawable.geography_books,R.drawable.socialscience, R.drawable.english, R.drawable.hindi_books,     R.drawable.marathi_books, R.drawable.wordbook, R.drawable.favouritebook
    };

    public static String[] learning_books_text =
            {"Bed time story book", "Comic book", "Rhymes book", "Drawing book", "Story book", "Picture book", "Mystery book", "Adventure book",   "School notebook", "Maths book", "Science book", "History book", "Geography book", "Social studies book", "English book", "Hindi book",   "Marathi book",  "Textbook", "Favorites"};

    public Integer[] learning_colours = {
            R.drawable.black_colour, R.drawable.blue_colour,
            R.drawable.brown_colour, R.drawable.greem_colour,
            R.drawable.redcolour, R.drawable.silvercolour,
            R.drawable.white_colour,R.drawable.yellow_colour,
            R.drawable.golden_colour, R.drawable.pink_colour, R.drawable.orange_colour,R.drawable.purple_colour, R.drawable.grey_colour
    };

    public static String[] learning_colours_text =
            {"Black", "Blue", "Brown", "Green", "Red", "Silver", "White", "Yellow",   "Golden", "Pink", "Orange", "Purple", "Gray"};

    public Integer[] learning_shapes = {
            R.drawable.standingline_shapes, R.drawable.sleepingline_shapes,
            R.drawable.slantingline_shapes, R.drawable.circle_shapes,
            R.drawable.rectangle_shapes,R.drawable.square_shapes, R.drawable.triangle_shapes, R.drawable.star_shapes,     R.drawable.heart_shape, R.drawable.trapezium_shapes,
            R.drawable.cube_shapes, R.drawable.rhombus_shapes,
            R.drawable.hexagon_shapes,R.drawable.oval_shapes, R.drawable.diamond_shapes, R.drawable.pentagon_shapes, R.drawable.freeform_shapes
    };

    public static String[] learning_shapes_text =
            {"Standing line", "Sleeping line", "Slanting line", "Circle", "Rectangle", "Square", "Triangle", "Star",   "Heart", "Trapezium", "Cube", "Rhombus", "Hexagon", "Oval", "Diamond", "Pentagon", "Freeform"};

    public Integer[] learning_stationary = {
            R.drawable.pencil_stationary, R.drawable.pen_stationary,
            R.drawable.ruler_stationary, R.drawable.eraser_stationary,
            R.drawable.sharpner_stationary,R.drawable.crayon_stationary,
            R.drawable.blankpaper_stationary, R.drawable.colouredpaper_stationary,     R.drawable.sissors, R.drawable.pencillead,
            R.drawable.compass, R.drawable.divider,
            R.drawable.stapler,R.drawable.upin,
            R.drawable.cellotape, R.drawable.pencilbox
    };

    public static String[] learning_stationary_text =
            {"Pencil", "Pen", "Scale", "Eraser", "Sharpener", "Crayon", "Blank paper", "Colored paper",   "Scissors", "Pencil lead", "Compass", "Divider", "Stapler", "U-pin", "Cello tape", "Compass box"};

    public Integer[] learning_school_objects = {
            R.drawable.bag_school, R.drawable.lunchbox,
            R.drawable.waterbottle_school, R.drawable.pencilbox,
            R.drawable.homework_school, R.drawable.notebook_books,
            R.drawable.textbooks_school, R.drawable.uniform_school,     R.drawable.shoes_school, R.drawable.socks_school,
            R.drawable.pencil_stationary, R.drawable.pen_stationary,
            R.drawable.ruler_stationary, R.drawable.eraser_stationary,
            R.drawable.sharpner_stationary, R.drawable.chalk
    };

    public static String[] learning_school_objects_text =
            {"Bag", "Lunch box", "Water bottle", "Compass box", "Homework", "School notebooks", "Textbooks", "Uniform",   "Shoes", "Socks", "Pencil", "Pen",  "Scale", "Eraser", "Sharpener", "Chalk"};

    public Integer[] learning_home_objects = {
            R.drawable.window, R.drawable.door_homeobjects,
            R.drawable.fan, R.drawable.lamp_homeobjects,
            R.drawable.desk_homeobjects,R.drawable.cupboard,
            R.drawable.table, R.drawable.chair_homeobjects,     R.drawable.toilet, R.drawable.kitchen,
            R.drawable.livingroom, R.drawable.bedroom,
            R.drawable.playroom,R.drawable.bathroom,
            R.drawable.balcony, R.drawable.studyroom,     R.drawable.bed, R.drawable.level2_fun_tv,
            R.drawable.computer, R.drawable.sofa,
            R.drawable.fridge,R.drawable.microwave,
            R.drawable.washingmachie, R.drawable.vaccumcleaner,     R.drawable.clock_homeobjects, R.drawable.tubelight
    };

    public static String[] learning_home_objects_text =
            {"Window", "Door", "Fan", "Lamp", "Desk", "Cupboard", "Table", "Chair",   "Toilet", "Kitchen", "Living room", "Bedroom", "Play room", "Bathroom", "Balcony", "Study room",   "Bed", "Television", "Computer", "Sofa", "Fridge", "Microwave", "Washing machine", "Vacuum cleaner",   "Clock", "Tube light" };

    public Integer[] learning_transportation = {
            R.drawable.bus_transport, R.drawable.schoolbus_transport,
            R.drawable.car_transport, R.drawable.cycle_transport,
            R.drawable.train_transport,R.drawable.rickshaw_transport,
            R.drawable.motorbike_transport, R.drawable.plane_transport, R.drawable.shop_transport
    };

    public static String[] learning_transportation_text =
            {"Bus", "School bus", "Car", "Bicycle", "Train", "Rickshaw", "Bike", "Aeroplane", "Ship"};

    public Integer[] time_weather_time = {
            R.drawable.currenttime, R.drawable.today,
            R.drawable.yesterday, R.drawable.tomorrow,
            R.drawable.morning,R.drawable.afternoon,
            R.drawable.goodevening, R.drawable.night
    };

    public static String[] time_weather_time_text =
            {"What is the time?", "Today", "Yesterday", "Tomorrow", "Morning", "Afternoon", "Evening", "Night"};

    public Integer[] time_weather_day = {
            R.drawable.currentday, R.drawable.monday,
            R.drawable.tuesday, R.drawable.wednesday,
            R.drawable.thursday,R.drawable.friday,
            R.drawable.saturday, R.drawable.sunday
    };

    public static String[] time_weather_day_text =
            {"What is the day today?", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    public Integer[] time_weather_month = {
            R.drawable.whatisthemonth, R.drawable.january,
            R.drawable.february, R.drawable.march,
            R.drawable.april,R.drawable.may,
            R.drawable.june, R.drawable.july,     R.drawable.august, R.drawable.september,
            R.drawable.october, R.drawable.november,
            R.drawable.december,R.drawable.currentmonth,
            R.drawable.previousmonth, R.drawable.nextmonth
    };

    public static String[] time_weather_month_text =
            {"Current month?", "January", "February", "March", "April", "May", "June", "July",   "August", "September", "October", "November", "December", "This month", "Previous month", "Next month"};

    public Integer[] time_weather_weather = {
            R.drawable.todaysweather, R.drawable.afternoon,
            R.drawable.rainy, R.drawable.cloudy,
            R.drawable.windy,R.drawable.foggy,
            R.drawable.snowy
    };

    public static String[] time_weather_weather_text =
            {"Today’s weather?", "Sunny", "Rainy", "Cloudy", "Windy", "Foggy", "Snowy"};

    public Integer[] time_weather_seasons = {
            R.drawable.currentseason, R.drawable.spring,
            R.drawable.afternoon, R.drawable.rainy,
            R.drawable.autumn,R.drawable.winter
    };

    public static String[] time_weather_seasons_text =
            {"Current season?", "Spring", "Summer", "Rainy", "Autumn", "Winter" };

    public Integer[] time_weather_holidays_festivals = {
            R.drawable.diwali, R.drawable.ganeshchaturthi,
            R.drawable.christmas, R.drawable.duserra,
            R.drawable.mararsankranti,R.drawable.holi,
            R.drawable.eid, R.drawable.goodfriday,    R.drawable.gudipadwa,
            R.drawable.republicday, R.drawable.independance, R.drawable.newyear
    };

    public static String[] time_weather_holidays_festivals_text =
            {"Diwali", "Ganesh chaturthi", "Christmas", "Dussehra", "Makar sankranti", "Holi", "Eid", "Good Friday",   "Gudi padwa", "Republic Day", "Independence Day", "New Year"};

    public Integer[] time_weather_birthdays = {
            R.drawable.mybirthday, R.drawable.momsbirthday,
            R.drawable.dadsbirthday, R.drawable.brothersbirthday,
            R.drawable.sistersbirthday,R.drawable.friendsbirthday,
            R.drawable.grandmomsbirthday, R.drawable.grandfathersbirthday,   R.drawable.unclesbirthday,
            R.drawable.auntsbirthday, R.drawable.cousinsbirthday, R.drawable.teachersbirthday
    };

    public static String[] time_weather_brthdays_text =
            {"My birthday", "Mom’s birthday", "Father’s birthday", "Brother’s birthday", "Sister’s birthday", "Friend’s birthday", "Grandma’s birthday", "Grandpa’s birthday",   "Uncle’s birthday", "Aunt’s birthday", "Cousin’s birthday", "Teacher’s birthday"};

    int k=0,l=0,kk=0,j=0,u=0;
int store=0;
    // Constructor
    public Layer_three_Adapter(Context c, int layer_1_id, int layer_2_id ,int sort[]){
        mContext = c;
        session = new SessionManager(mContext);
        /*this.layer_1_id = layer_1_id;
        this.layer_2_id = layer_2_id;*/
        Log.d("noooLAY",layer_1_id+" "+layer_2_id);

        if(layer_1_id == 0) {
    if (layer_2_id == 0) {

        Integer[] temp1 = new Integer[sort.length];
        String[] temp2 = new String[sort.length];
        for (int j = 0 ; j< greet_feel_greetings.length; j++) {
            temp1[j] = greet_feel_greetings[sort[j]];
            temp2[j] = greet_feel_greetings_text[sort[j]];
            Log.e("printing ",temp2[j]+" ");
            store = j;
        }
        store = store+1;
//       belowText = greet_feel_greetings_text;
//        mThumbIds = greet_feel_greetings;
        Log.d("lengtth",store+"");
        Log.d("lengtth 1",sort.length+"");
        mThumbIds = Arrays.copyOfRange(temp1, 0, store);
        belowText = Arrays.copyOfRange(temp2, 0, store);
    } else if (layer_2_id == 1) {
        Integer[] temp1 = new Integer[sort.length];
        String[] temp2 = new String[sort.length];
        for (int j = 0 ; j< greet_feel_feelings.length; j++) {
            temp1[j] = greet_feel_feelings[sort[j]];
            temp2[j] = greet_feel_feelings_text[sort[j]];
            Log.e("printing ",temp2[j]+" ");
            store = j;
        }
        store = store+1;
        mThumbIds = Arrays.copyOfRange(temp1, 0, store);
        belowText = Arrays.copyOfRange(temp2, 0, store);
    } else if (layer_2_id == 2) {
        Integer[] temp1 = new Integer[sort.length];
        String[] temp2 = new String[sort.length];
        for (int j = 0 ; j< greet_feel_requests.length; j++) {
            temp1[j] = greet_feel_requests[sort[j]];
            temp2[j] = greet_feel_requests_text[sort[j]];
            Log.e("printing ",temp2[j]+" ");
            store = j;
        }
        store = store+1;
        mThumbIds = Arrays.copyOfRange(temp1, 0, store);
        belowText = Arrays.copyOfRange(temp2, 0, store);
    } else if (layer_2_id == 3) {
        Integer[] temp1 = new Integer[sort.length];
        String[] temp2 = new String[sort.length];
        for (int j = 0 ; j< greet_feel_questions.length; j++) {
            temp1[j] = greet_feel_questions[sort[j]];
            temp2[j] = greet_feel_questions_text[sort[j]];
            Log.e("printing ",temp2[j]+" ");
            store = j;
        }
        store = store+1;
        mThumbIds = Arrays.copyOfRange(temp1, 0, store);
        belowText = Arrays.copyOfRange(temp2, 0, store);
    }
}
        else
if(layer_1_id == 1) {
    if (layer_2_id == 0) {
        Integer[] temp1 = new Integer[sort.length];
        String[] temp2 = new String[sort.length];
        for (int j = 0 ; j< daily_activities_brushing.length; j++) {
            temp1[j] = daily_activities_brushing[sort[j]];
            temp2[j] = daily_activities_brushing_text[sort[j]];
            Log.e("printing ",temp2[j]+" ");
            store = j;
        }
        store = store+1;
        mThumbIds = Arrays.copyOfRange(temp1, 0, store);
        belowText = Arrays.copyOfRange(temp2, 0, store);
    } else if (layer_2_id == 1) {
        Integer[] temp1 = new Integer[sort.length];
        String[] temp2 = new String[sort.length];
        for (int j = 0 ; j< daily_activities_toilet.length; j++) {
            temp1[j] = daily_activities_toilet[sort[j]];
            temp2[j] = daily_activities_toilet_text[sort[j]];
            Log.e("printing ",temp2[j]+" ");
            store = j;
        }
        store = store+1;
        mThumbIds = Arrays.copyOfRange(temp1, 0, store);
        belowText = Arrays.copyOfRange(temp2, 0, store);
    } else if (layer_2_id == 2) {
        Integer[] temp1 = new Integer[sort.length];
        String[] temp2 = new String[sort.length];
        for (int j = 0 ; j< daily_activities_bathing.length; j++) {
            temp1[j] = daily_activities_bathing[sort[j]];
            temp2[j] = daily_activities_bathing_text[sort[j]];
            Log.e("printing ",temp2[j]+" ");
            store = j;
        }
        store = store+1;
        mThumbIds = Arrays.copyOfRange(temp1, 0, store);
        belowText = Arrays.copyOfRange(temp2, 0, store);
    } else if (layer_2_id == 3) {
        Integer[] temp1 = new Integer[sort.length];
        String[] temp2 = new String[sort.length];
        for (int j = 0 ; j< daily_activities_clothes_access.length; j++) {
            temp1[j] = daily_activities_clothes_access[sort[j]];
            temp2[j] = daily_activities_clothes_access_text[sort[j]];
            Log.e("printing ",temp2[j]+" ");
            store = j;
        }
        store = store+1;
        mThumbIds = Arrays.copyOfRange(temp1, 0, store);
        belowText = Arrays.copyOfRange(temp2, 0, store);
    } else if (layer_2_id == 4) {
        Integer[] temp1 = new Integer[sort.length];
        String[] temp2 = new String[sort.length];
        for (int j = 0 ; j< daily_activities_get_ready.length; j++) {
            temp1[j] = daily_activities_get_ready[sort[j]];
            temp2[j] = daily_activities_get_ready_text[sort[j]];
            Log.e("printing ",temp2[j]+" ");
            store = j;
        }
        store = store+1;
        mThumbIds = Arrays.copyOfRange(temp1, 0, store);
        belowText = Arrays.copyOfRange(temp2, 0, store);
    } else if (layer_2_id == 5) {
        Integer[] temp1 = new Integer[sort.length];
        String[] temp2 = new String[sort.length];
        for (int j = 0 ; j< daily_activities_sleep.length; j++) {
            temp1[j] = daily_activities_sleep[sort[j]];
            temp2[j] = daily_activities_sleep_text[sort[j]];
            Log.e("printing ",temp2[j]+" ");
            store = j;
        }
        store = store+1;
        mThumbIds = Arrays.copyOfRange(temp1, 0, store);
        belowText = Arrays.copyOfRange(temp2, 0, store);
    } else if (layer_2_id == 6) {
        Integer[] temp1 = new Integer[sort.length];
        String[] temp2 = new String[sort.length];
        for (int j = 0 ; j< daily_activities_therapy.length; j++) {
            temp1[j] = daily_activities_therapy[sort[j]];
            temp2[j] = daily_activities_therapy_text[sort[j]];
            Log.e("printing ",temp2[j]+" ");
            store = j;
        }
        store = store+1;
        mThumbIds = Arrays.copyOfRange(temp1, 0, store);
        belowText = Arrays.copyOfRange(temp2, 0, store);
    } else if (layer_2_id == 7) {
        Integer[] temp1 = new Integer[sort.length];
        String[] temp2 = new String[sort.length];
        for (int j = 0 ; j< daily_activities_morning_schedule.length; j++) {
            temp1[j] = daily_activities_morning_schedule[sort[j]];
            temp2[j] = daily_activities_morning_schedule_text[sort[j]];
            Log.e("printing ",temp2[j]+" ");
            store = j;
        }
        store = store+1;
        mThumbIds = Arrays.copyOfRange(temp1, 0, store);
        belowText = Arrays.copyOfRange(temp2, 0, store);
    }
    else if (layer_2_id == 8) {
        Integer[] temp1 = new Integer[sort.length];
        String[] temp2 = new String[sort.length];
        for (int j = 0 ; j< daily_activities_bedtime_schedule.length; j++) {
            temp1[j] = daily_activities_bedtime_schedule[sort[j]];
            temp2[j] = daily_activities_bedtime_schedule_text[sort[j]];
            Log.e("printing ",temp2[j]+" ");
            store = j;
        }
        store = store+1;
        mThumbIds = Arrays.copyOfRange(temp1, 0, store);
        belowText = Arrays.copyOfRange(temp2, 0, store);
    }
}else
    if(layer_1_id == 2)
    {
 if(layer_2_id == 0)
 {
     Integer[] temp1 = new Integer[sort.length];
     String[] temp2 = new String[sort.length];
     for (int j = 0 ; j< eating_foods_drinks_breakfast.length; j++) {
         temp1[j] = eating_foods_drinks_breakfast[sort[j]];
         temp2[j] = foods_drinks_breakfast_text[sort[j]];
         Log.e("printing ",temp2[j]+" ");
         store = j;
     }
     store = store+1;
     mThumbIds = Arrays.copyOfRange(temp1, 0, store);
     belowText = Arrays.copyOfRange(temp2, 0, store);
 }
        else if(layer_2_id == 1)
        {
            Integer[] temp1 = new Integer[sort.length];
            String[] temp2 = new String[sort.length];
            for (int j = 0 ; j< eating_food_drinks_lunch_dinner.length; j++) {
                temp1[j] = eating_food_drinks_lunch_dinner[sort[j]];
                temp2[j] = food_drinks_lunch_dinner_text[sort[j]];
                Log.e("printing ",temp2[j]+" ");
                store = j;
            }
            store = store+1;
            mThumbIds = Arrays.copyOfRange(temp1, 0, store);
            belowText = Arrays.copyOfRange(temp2, 0, store);
        }
 else if(layer_2_id == 2)
 {
     Integer[] temp1 = new Integer[sort.length];
     String[] temp2 = new String[sort.length];
     for (int j = 0 ; j< eating_food_drinks_sweets.length; j++) {
         temp1[j] = eating_food_drinks_sweets[sort[j]];
         temp2[j] = food_drinks_sweets_text[sort[j]];
         Log.e("printing ",temp2[j]+" ");
         store = j;
     }
     store = store+1;
     mThumbIds = Arrays.copyOfRange(temp1, 0, store);
     belowText = Arrays.copyOfRange(temp2, 0, store);
 }
 else if(layer_2_id == 3)
 {
     Integer[] temp1 = new Integer[sort.length];
     String[] temp2 = new String[sort.length];
     for (int j = 0 ; j< eating_food_drinks_snacks.length; j++) {
         temp1[j] = eating_food_drinks_snacks[sort[j]];
         temp2[j] = food_drinks_snacks_text[sort[j]];
         Log.e("printing ",temp2[j]+" ");
         store = j;
     }
     store = store+1;
     mThumbIds = Arrays.copyOfRange(temp1, 0, store);
     belowText = Arrays.copyOfRange(temp2, 0, store);
 }
 else if(layer_2_id == 4)
 {
     Integer[] temp1 = new Integer[sort.length];
     String[] temp2 = new String[sort.length];
     for (int j = 0 ; j< eating_food_drinks_fruits.length; j++) {
         temp1[j] = eating_food_drinks_fruits[sort[j]];
         temp2[j] = food_drinks_fruits_text[sort[j]];
         Log.e("printing ",temp2[j]+" ");
         store = j;
     }
     store = store+1;
     mThumbIds = Arrays.copyOfRange(temp1, 0, store);
     belowText = Arrays.copyOfRange(temp2, 0, store);
 }
 else if(layer_2_id == 5)
 {
     Integer[] temp1 = new Integer[sort.length];
     String[] temp2 = new String[sort.length];
     for (int j = 0 ; j< eating_food_drinks_drinks.length; j++) {
         temp1[j] = eating_food_drinks_drinks[sort[j]];
         temp2[j] = food_drinks_drinks_text[sort[j]];
         Log.e("printing ",temp2[j]+" ");
         store = j;
     }
     store = store+1;
     mThumbIds = Arrays.copyOfRange(temp1, 0, store);
     belowText = Arrays.copyOfRange(temp2, 0, store);
 }
 else if(layer_2_id == 6)
 {
     Integer[] temp1 = new Integer[sort.length];
     String[] temp2 = new String[sort.length];
     for (int j = 0 ; j< eating_food_drinks_cutlery.length; j++) {
         temp1[j] = eating_food_drinks_cutlery[sort[j]];
         temp2[j] = food_drinks_cutlery_text[sort[j]];
         Log.e("printing ",temp2[j]+" ");
         store = j;
     }
     store = store+1;
     mThumbIds = Arrays.copyOfRange(temp1, 0, store);
     belowText = Arrays.copyOfRange(temp2, 0, store);
 }
 else if(layer_2_id == 7)
 {
     Integer[] temp1 = new Integer[sort.length];
     String[] temp2 = new String[sort.length];
     for (int j = 0 ; j< eating_food_drinks_add_ons.length; j++) {
         temp1[j] = eating_food_drinks_add_ons[sort[j]];
         temp2[j] = food_drinks_add_ons_text[sort[j]];
         Log.e("printing ",temp2[j]+" ");
         store = j;
     }
     store = store+1;
     mThumbIds = Arrays.copyOfRange(temp1, 0, store);
     belowText = Arrays.copyOfRange(temp2, 0, store);
 }

    }else
        if(layer_1_id == 3)
        {
            if(layer_2_id == 0)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< fun_indoor_games.length; j++) {
                    temp1[j] = fun_indoor_games[sort[j]];
                    temp2[j] = fun_indoor_games_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
            else
            if(layer_2_id == 1)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< fun_outdoor_games.length; j++) {
                    temp1[j] = fun_outdoor_games[sort[j]];
                    temp2[j] = fun_outdoor_games_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
            else
            if(layer_2_id == 2)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< fun_sports.length; j++) {
                    temp1[j] = fun_sports[sort[j]];
                    temp2[j] = fun_sports_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
            else
            if(layer_2_id == 3)
        {

            mThumbIds = fun_tv;
            belowText = fun_tv_text;
        }
            else
            if(layer_2_id == 4)
        {

            mThumbIds = fun_music;
            belowText = fun_music_text;
        } else
            if(layer_2_id == 5)
        {
            Integer[] temp1 = new Integer[sort.length];
            String[] temp2 = new String[sort.length];
            for (int j = 0 ; j< fun_activities.length; j++) {
                temp1[j] = fun_activities[sort[j]];
                temp2[j] = fun_activities_text[sort[j]];
                Log.e("printing ",temp2[j]+" ");
                store = j;
            }
            store = store+1;
            mThumbIds = Arrays.copyOfRange(temp1, 0, store);
            belowText = Arrays.copyOfRange(temp2, 0, store);
        }
        }
        else
        if(layer_1_id ==4)
        {
            if(layer_2_id == 0)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_animals_birds.length; j++) {
                    temp1[j] = learning_animals_birds[sort[j]];
                    temp2[j] = learning_animals_birds_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
            else if(layer_2_id == 1)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_body_parts.length; j++) {
                    temp1[j] = learning_body_parts[sort[j]];
                    temp2[j] = learning_body_parts_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else
            if(layer_2_id == 2)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_books.length; j++) {
                    temp1[j] = learning_books[sort[j]];
                    temp2[j] = learning_books_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
            else
            if(layer_2_id == 3)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_colours.length; j++) {
                    temp1[j] = learning_colours[sort[j]];
                    temp2[j] = learning_colours_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }else
            if(layer_2_id == 4)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_shapes.length; j++) {
                    temp1[j] = learning_shapes[sort[j]];
                    temp2[j] = learning_shapes_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
            else

            if(layer_2_id == 5)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_stationary.length; j++) {
                    temp1[j] = learning_stationary[sort[j]];
                    temp2[j] = learning_stationary_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
            else
            if(layer_2_id == 6)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_school_objects.length; j++) {
                    temp1[j] = learning_school_objects[sort[j]];
                    temp2[j] = learning_school_objects_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
            else
            if(layer_2_id == 7)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_home_objects.length; j++) {
                    temp1[j] = learning_home_objects[sort[j]];
                    temp2[j] = learning_home_objects_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }
            else
            if(layer_2_id == 8)
            {
                Integer[] temp1 = new Integer[sort.length];
                String[] temp2 = new String[sort.length];
                for (int j = 0 ; j< learning_transportation.length; j++) {
                    temp1[j] = learning_transportation[sort[j]];
                    temp2[j] = learning_transportation_text[sort[j]];
                    Log.e("printing ",temp2[j]+" ");
                    store = j;
                }
                store = store+1;
                mThumbIds = Arrays.copyOfRange(temp1, 0, store);
                belowText = Arrays.copyOfRange(temp2, 0, store);
            }


        }
        else
            if(layer_1_id == 7) {

        if(layer_2_id == 0)
        {

            mThumbIds = time_weather_time;
            belowText = time_weather_time_text;
        }else if(layer_2_id == 1)
                {

                    mThumbIds = time_weather_day;
                    belowText = time_weather_day_text;
                }
        else if(layer_2_id == 2)
        {

            mThumbIds = time_weather_month;
            belowText = time_weather_month_text;
        }
        else if(layer_2_id == 3)
        {

            mThumbIds = time_weather_weather;
            belowText = time_weather_weather_text;
        }
        else if(layer_2_id == 4)
        {

            mThumbIds = time_weather_seasons;
            belowText = time_weather_seasons_text;
        }
        else if(layer_2_id == 5)
        {
            Integer[] temp1 = new Integer[sort.length];
            String[] temp2 = new String[sort.length];
            for (int j = 0 ; j< time_weather_holidays_festivals.length; j++) {
                temp1[j] = time_weather_holidays_festivals[sort[j]];
                temp2[j] = time_weather_holidays_festivals_text[sort[j]];
                Log.e("printing ",temp2[j]+" ");
                store = j;
            }
            store = store+1;
            mThumbIds = Arrays.copyOfRange(temp1, 0, store);
            belowText = Arrays.copyOfRange(temp2, 0, store);
        }

        else if(layer_2_id == 6)
        {
            Integer[] temp1 = new Integer[sort.length];
            String[] temp2 = new String[sort.length];
            for (int j = 0 ; j< time_weather_birthdays.length; j++) {
                temp1[j] = time_weather_birthdays[sort[j]];
                temp2[j] = time_weather_brthdays_text[sort[j]];
                Log.e("printing ",temp2[j]+" ");
                store = j;
            }
            store = store+1;
            mThumbIds = Arrays.copyOfRange(temp1, 0, store);
            belowText = Arrays.copyOfRange(temp2, 0, store);
        }
            }

}
    public class MyViewHolder extends RecyclerView.ViewHolder  {
int position=-1;
        Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(),  "fonts/Mukta-Regular.ttf");

        public MyViewHolder(final View view) {

            super(view);
    if(session.getGridSize()==0) {
        img1 = (CircularImageView) view.findViewById(R.id.icon1);
        img2 = (CircularImageView) view.findViewById(R.id.icon2);
        img3 = (CircularImageView) view.findViewById(R.id.icon3);
        mLinearLayoutIconOne = (LinearLayout) view.findViewById(R.id.linearlayout_icon1);
        mLinearLayoutIconTwo = (LinearLayout) view.findViewById(R.id.linearlayout_icon2);
        mLinearLayoutIconThree = (LinearLayout) view.findViewById(R.id.linearlayout_icon3);

        tv1 = (TextView)view.findViewById(R.id.te1);
        tv2 = (TextView)view.findViewById(R.id.te2);
        tv3 = (TextView)view.findViewById(R.id.te3);

    tv1.setTypeface(custom_font);
    tv1.setTextColor(Color.rgb(64,64,64));
    tv2.setTypeface(custom_font);
    tv2.setTextColor(Color.rgb(64,64,64));
    tv3.setTypeface(custom_font);
    tv3.setTextColor(Color.rgb(64,64,64));
}
    if(session.getGridSize()==1)
{
    img1 = (CircularImageView)view.findViewById(R.id.icon1);
    img2 = (CircularImageView)view.findViewById(R.id.icon2);
    img3 = (CircularImageView)view.findViewById(R.id.icon3);
    img4 = (CircularImageView)view.findViewById(R.id.icon4);
    img5 = (CircularImageView)view.findViewById(R.id.icon5);
    img6 = (CircularImageView)view.findViewById(R.id.icon6);
    img7 = (CircularImageView)view.findViewById(R.id.icon7);
    img8 = (CircularImageView)view.findViewById(R.id.icon8);
    img9 = (CircularImageView)view.findViewById(R.id.icon9);
    tv1 = (TextView)view.findViewById(R.id.te1);
    tv2 = (TextView)view.findViewById(R.id.te2);
    tv3 = (TextView)view.findViewById(R.id.te3);

    tv1.setTypeface(custom_font);
    tv1.setTextColor(Color.rgb(64,64,64));
    tv2.setTypeface(custom_font);
    tv2.setTextColor(Color.rgb(64,64,64));
    tv3.setTypeface(custom_font);
    tv3.setTextColor(Color.rgb(64,64,64));

    tv4 = (TextView)view.findViewById(R.id.te4);
    tv5 = (TextView)view.findViewById(R.id.te5);
    tv6 = (TextView)view.findViewById(R.id.te6);
    tv7 = (TextView)view.findViewById(R.id.te7);
    tv8 = (TextView)view.findViewById(R.id.te8);
    tv9 = (TextView)view.findViewById(R.id.te9);
    tv4.setTypeface(custom_font);
    tv4.setTextColor(Color.rgb(64,64,64));
    tv5.setTypeface(custom_font);
    tv5.setTextColor(Color.rgb(64,64,64));
    tv6.setTypeface(custom_font);
    tv6.setTextColor(Color.rgb(64,64,64));
    tv7.setTypeface(custom_font);
    tv7.setTextColor(Color.rgb(64,64,64));
    tv8.setTypeface(custom_font);
    tv8.setTextColor(Color.rgb(64,64,64));
    tv9.setTypeface(custom_font);
    tv9.setTextColor(Color.rgb(64,64,64));
}

            //setIsRecyclable(false);
        }
    }

    @Override
    public Layer_three_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView;
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        if (session.getGridSize()==0){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myscrolllist2, parent, false);
        }else if (dpHeight >= 720 && session.getGridSize()==1)
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.myscrollist233, parent, false);
        else if (dpWidth >640 && dpWidth <=1024 && session.getGridSize()==1)
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.myscrollist033, parent, false);
        else if (dpWidth > 600 && dpWidth <=640 && session.getGridSize()==1) {
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.myscrolllist33, parent, false);
        }else {
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.myscrolllist33, parent, false);
        }
        return new Layer_three_Adapter.MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position1) {

        int position;
        j=mThumbIds.length;
       // Log.d("Possss1",position+"");
        position=holder.getAdapterPosition();
        Log.d("Possss",position+"");
       /* if(holder.equals(R.layout.myscrollist033))
        {
            k=position*3;
        }
        else*/
       if (session.getGridSize()==0) {
           k = position;

           if (k > 0) {
               j = j - position * 3;
               Log.d("kvalueold", k + " ");
               k = k + 3 * position - position;
               Log.d("kvalue", k + " ");

           }


           if (j > 0) {
               tv1.setText(belowText[k]);
               img1.setImageResource(mThumbIds[k]);
               j = j - 1;
           } else {
               tv1.setText(" ");
               //   img1.setImageResource(0);
               img1.setImageResource(R.drawable.background_circle);
               img1.setEnabled(false);

           }
           if (j > 0) {
               tv2.setText(belowText[k + 1]);
               img2.setImageResource(mThumbIds[k + 1]);
               j = j - 1;
           } else {
               tv2.setText(" ");
               //img2.setImageResource(0);
               img2.setImageResource(R.drawable.background_circle);
               img2.setEnabled(false);

           }

           if (j > 0) {
               tv3.setText(belowText[k + 2]);

               img3.setImageResource(mThumbIds[k + 2]);
               j = j - 1;
           } else {
               tv3.setText(" ");
               //img3.setImageResource(0);
               img3.setImageResource(R.drawable.background_circle);
               img3.setEnabled(false);
           }

       }

       if (session.getGridSize()==1)
       {
           k = position;
           if (k > 0) {
               j = j - position * 9;
               Log.d("kvalueold", k + " ");
               k = k + 9 * position - position;
               Log.d("kvalue", k + " ");

           }

           if (j > 0) {
               tv1.setText(belowText[k]);
               img1.setImageResource(mThumbIds[k]);
               j = j - 1;
           } else {
               tv1.setText(" ");
               //   img1.setImageResource(0);
               img1.setImageResource(R.drawable.background_circle);
               img1.setEnabled(false);

           }
           if (j > 0) {
               tv2.setText(belowText[k + 1]);
               img2.setImageResource(mThumbIds[k + 1]);
               j = j - 1;
           } else {
               tv2.setText(" ");
               //img2.setImageResource(0);
               img2.setImageResource(R.drawable.background_circle);
               img2.setEnabled(false);

           }

           if (j > 0) {
               tv3.setText(belowText[k + 2]);

               img3.setImageResource(mThumbIds[k + 2]);
               j = j - 1;
           } else {
               tv3.setText(" ");
               //img3.setImageResource(0);
               img3.setImageResource(R.drawable.background_circle);
               img3.setEnabled(false);
           }
           if (j > 0) {
               tv4.setText(belowText[k + 3]);

               img4.setImageResource(mThumbIds[k + 3]);
               j = j - 1;
           } else {
               tv4.setText(" ");
               //img3.setImageResource(0);
               img4.setImageResource(R.drawable.background_circle);
               img4.setEnabled(false);
           }
           if (j > 0) {
           tv5.setText(belowText[k + 4]);

           img5.setImageResource(mThumbIds[k + 4]);
           j = j - 1;
       } else {
           tv5.setText(" ");
           //img3.setImageResource(0);
           img5.setImageResource(R.drawable.background_circle);
           img5.setEnabled(false);
       }if (j > 0) {
           tv6.setText(belowText[k + 5]);

           img6.setImageResource(mThumbIds[k + 5]);
           j = j - 1;
       } else {
           tv6.setText(" ");
           //img3.setImageResource(0);
           img6.setImageResource(R.drawable.background_circle);
           img6.setEnabled(false);
       }if (j > 0) {
           tv7.setText(belowText[k + 6]);

           img7.setImageResource(mThumbIds[k + 6]);
           j = j - 1;
       } else {
           tv7.setText(" ");
           //img3.setImageResource(0);
           img7.setImageResource(R.drawable.background_circle);
           img7.setEnabled(false);
       }if (j > 0) {
           tv8.setText(belowText[k + 7]);

           img8.setImageResource(mThumbIds[k + 7]);
           j = j - 1;
       } else {
           tv8.setText(" ");
           //img3.setImageResource(0);
           img8.setImageResource(R.drawable.background_circle);
           img8.setEnabled(false);
       }if (j > 0) {
           tv9.setText(belowText[k + 8]);

           img9.setImageResource(mThumbIds[k + 8]);
           j = j - 1;
       } else {
           tv9.setText(" ");
           //img3.setImageResource(0);
           img9.setImageResource(R.drawable.background_circle);
           img9.setEnabled(false);
       }
       }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        System.out.println("MTHUMBSID"+(mThumbIds.length));
if(session.getGridSize()==1)
{
    kk = (mThumbIds.length % 9);
    System.out.println("kkkkkkkk" + kk);
    l = (mThumbIds.length / 9);
    System.out.println("llllll" + l);
    if (kk > 0) {
        l++;
        System.out.println("COOUT" + l);
    }
    System.out.println("DOOUT" + l);

    Log.d("valueeeee",l+":");
}
          if(session.getGridSize()==0) {
              kk = (mThumbIds.length % 3);
              System.out.println("kkkkkkkk" + kk);
              l = (mThumbIds.length / 3);
              System.out.println("llllll" + l);
              if (kk > 0) {
                  l++;
                  System.out.println("COOUT" + l);
              }
              System.out.println("DOOUT" + l);

              Log.d("valueeeee", l + ":");
          }
        return l;
    }


    private void handleItemClick(String position1, int local) {
        System.out.print("CHECCCC "+position1);
        ((Layer3Activity) mContext).location=local;

    }

    private String putpos(String s) {
        String position="";
        position+=s;
        System.out.println("ppppp "+position);
        return position;
    }

}