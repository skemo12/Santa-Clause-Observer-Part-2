package utils;

import child.Child;
import data.Database;
import enums.Category;
import enums.Cities;
import enums.CityStrategyEnum;
import enums.ElvesType;
import interfaces.StrategyInterface;
import strategies.IdStrategy;
import strategies.NiceCityScoreStrategy;
import strategies.NiceScoreStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Utils {
    private Utils() {
        //constructor for checkstyle
    }

    /**
     * Returns the child who has the id from parameters.
     *
     * @param id wanted id to find
     */
    public static Child getChildById(final Integer id) {
        for (final Child child : Database.getInstance().getChildren()) {
            if (child.getId().equals(id)) {
                return child;
            }
        }
        return null;
    }
    /**
     * Returns the index of a child in list, searched by id, not Child object.
     * @param child child whose id we want to find the index of
     */
    public static int getIndexOfChild(final Child child) {
        for (int i = 0; i < Database.getInstance().getChildren().size(); i++) {
            final Child childCurrent = Database.getInstance().getChildren().get(i);
            if (Objects.equals(childCurrent.getId(), child.getId())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Converts string to Category ENUM.
     * @param category string to be converted
     */
    public static Category stringToCategory(final String category) {
        return switch (category) {
            case "Board Games" -> Category.BOARD_GAMES;
            case "Books" -> Category.BOOKS;
            case "Clothes" -> Category.CLOTHES;
            case "Sweets" -> Category.SWEETS;
            case "Technology" -> Category.TECHNOLOGY;
            case "Toys" -> Category.TOYS;
            default -> null;
        };
    }

    /**
     * Converts string to Cities ENUM.
     * @param city string to be converted
     */
    public static Cities stringToCity(final String city) {
        return switch (city) {
            case "Bucuresti" -> Cities.BUCURESTI;
            case "Constanta" -> Cities.CONSTANTA;
            case "Buzau" -> Cities.BUZAU;
            case "Timisoara" -> Cities.TIMISOARA;
            case "Cluj-Napoca" -> Cities.CLUJ;
            case "Iasi" -> Cities.IASI;
            case "Craiova" -> Cities.CRAIOVA;
            case "Brasov" -> Cities.BRASOV;
            case "Braila" -> Cities.BRAILA;
            case "Oradea" -> Cities.ORADEA;
            default -> null;
        };
    }

    /**
     * Converts List to List Category ENUM.
     * @param list list to be converted
     */
    public static List<Category> stringListToCategoryList(
            final List<String> list) {
        final List<Category> categories = new ArrayList<>();
        for (final String element : list) {
            categories.add(Utils.stringToCategory(element));
        }
        return categories;
    }

    /**
     * Converts string to Elf ENUM.
     * @param elf string to be converted
     */
    public static ElvesType stringToElf(final String elf) {
        return switch (elf) {
            case "yellow" -> ElvesType.YELLOW;
            case "black" -> ElvesType.BLACK;
            case "pink" -> ElvesType.PINK;
            case "white" -> ElvesType.WHITE;
            default -> null;
        };
    }

    /**
     * Converts string to Elf ENUM.
     * @param strategy string to be converted
     */
    public static CityStrategyEnum stringToStrategy(final String strategy) {
        return switch (strategy) {
            case "niceScoreCity" -> CityStrategyEnum.NICE_SCORE_CITY;
            case "id" -> CityStrategyEnum.ID;
            case "niceScore" -> CityStrategyEnum.NICE_SCORE;
            default -> null;
        };
    }

    /**
     * Converts enum Strategy to StrategyInterface.
     * @param strategyEnum enum to be converted
     */
    public static StrategyInterface convertStrategyEnumToStrategy(
            final CityStrategyEnum strategyEnum) {

        return switch (strategyEnum) {
            case ID -> new IdStrategy();
            case NICE_SCORE -> new NiceScoreStrategy();
            case NICE_SCORE_CITY -> new NiceCityScoreStrategy();
        };
    }
}
