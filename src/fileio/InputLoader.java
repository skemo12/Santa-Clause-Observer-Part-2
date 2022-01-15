package fileio;

import annualchanges.AnnualChanges;
import annualchanges.ChildUpdate;
import child.Child;
import common.Constants;
import data.Database;
import enums.CityStrategyEnum;
import enums.ElvesType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import santa.Gift;
import santa.Santa;
import utils.Utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that reads input from JSON file.
 */
public final class InputLoader {
    /**
     * The path to the input file
     */
    private final String inputPath;

    public InputLoader(final String inputPath) {
        this.inputPath = inputPath;
    }

    public String getInputPath() {
        return inputPath;
    }

    /**
     * The method reads the database
     */
    public void readData() {
        final JSONParser jsonParser = new JSONParser();
        try {
            final JSONObject jsonObject = (JSONObject) jsonParser
                    .parse(new FileReader(inputPath));
            final JSONObject initialData = (JSONObject) jsonObject
                    .get(Constants.INITIAL_DATA);
            final JSONArray jsonChildren = (JSONArray) initialData
                    .get(Constants.CHILDREN);
            final Double santaBudget = ((Long) (jsonObject)
                    .get(Constants.SANTA_BUDGET)).doubleValue();
            final Integer numberOfYears = ((Long) (jsonObject)
                    .get(Constants.NUMBER_OF_YEARS)).intValue();
            final JSONArray jsonSantaGiftsList = (JSONArray) initialData.
                    get(Constants.SANTA_GIFTS_LIST);
            final JSONArray jsonAnnualChanges = (JSONArray) jsonObject
                    .get(Constants.ANNUAL_CHANGES);


            final List<Child> children = readChildList(jsonChildren);
            final List<Gift> giftList = readGiftsList(jsonSantaGiftsList);
            final List<AnnualChanges> annualChanges = readAnnualChanges(
                    jsonAnnualChanges);

            Database.getInstance().setNumberOfYears(numberOfYears);
            Database.getInstance().setChildren(children);
            Database.getInstance().setAnnualChanges(annualChanges);
            Database.getInstance().setSanta(new Santa(santaBudget, giftList));


        } catch (final ParseException | IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * Creates Children list from jsonArray
     */
    public List<Child> readChildList(final JSONArray jsonChildren) {
        final List<Child> children = new ArrayList<>();
        if (jsonChildren != null) {
            for (final Object jsonChild : jsonChildren) {
                final JSONArray jsonGiftsPreferences = (JSONArray)
                        ((JSONObject) jsonChild)
                                .get(Constants.GIFT_PREFERENCES);
                final List<String> giftPreferences = new ArrayList<>();
                for (final Object gift : jsonGiftsPreferences) {
                    giftPreferences.add(((String) gift));
                }
                children.add(new Child.Builder(
                                ((Long) ((JSONObject) jsonChild)
                                        .get(Constants.ID)).intValue(),
                                (String) ((JSONObject) jsonChild)
                                    .get(Constants.LAST_NAME),
                                (String) ((JSONObject) jsonChild)
                                        .get(Constants.FIRST_NAME),
                                Utils.getInstance().stringToCity(
                                        (String) ((JSONObject) jsonChild)
                                                .get(Constants.CITY)),
                                ((Long) ((JSONObject) jsonChild)
                                    .get(Constants.AGE)).intValue(),
                                Utils.getInstance()
                                        .stringListToCategoryList(giftPreferences),
                                ((Long) ((JSONObject) jsonChild)
                                    .get(Constants.NICE_SCORE)).doubleValue())
                        .elfType(Utils.getInstance().stringToElf((String)
                                        ((JSONObject) jsonChild).get(Constants.ELF)))
                        .niceScoreBonus(((Long) ((JSONObject) jsonChild)
                                .get(Constants.NICE_SCORE_BONUS))
                                .doubleValue())
                        .build());
            }
        }
        return children;
    }

    /**
     * Creates Gift list from jsonArray
     */
    public List<Gift> readGiftsList(final JSONArray jsonSantaGiftsList) {
        final List<Gift> giftList = new ArrayList<>();
        if (jsonSantaGiftsList != null) {
            for (final Object jsonGift : jsonSantaGiftsList) {
                final Gift gift = new Gift(
                        (String) ((JSONObject) jsonGift)
                                .get(Constants.PRODUCT_NAME),
                        ((Long) ((JSONObject) jsonGift)
                                .get(Constants.PRICE)).doubleValue(),
                        Utils.getInstance().stringToCategory(
                                (String) ((JSONObject) jsonGift)
                                .get(Constants.CATEGORY)),
                        ((Long) ((JSONObject) jsonGift)
                                .get(Constants.QUANTITY)).intValue()

                );
                giftList.add(gift);
            }
        }
        return giftList;
    }
    /**
     * Creates AnnualChanges list from jsonArray
     */
    public List<AnnualChanges> readAnnualChanges(
            final JSONArray jsonAnnualChanges) {
        final List<AnnualChanges> annualChanges = new ArrayList<>();
        Double newSantaBudget = null;
        if (jsonAnnualChanges != null) {
            for (final Object jsonAnnualChange : jsonAnnualChanges) {

                if (((JSONObject) jsonAnnualChange)
                        .get(Constants.NEW_SANTA_BUDGET) != null) {
                    newSantaBudget =
                            ((Long) ((JSONObject) jsonAnnualChange)
                                    .get(Constants.NEW_SANTA_BUDGET))
                                    .doubleValue();
                }

                final JSONArray jsonNewGifts = (JSONArray)
                        ((JSONObject) jsonAnnualChange)
                                .get(Constants.NEW_GIFTS);
                final List<Gift> newGifts = readGiftsList(jsonNewGifts);

                final JSONArray jsonNewChildren = (JSONArray)
                        ((JSONObject) jsonAnnualChange)
                                .get(Constants.NEW_CHILDREN);
                final List<Child> newChildren = readChildList(jsonNewChildren);

                final List<ChildUpdate> childrenUpdates = new ArrayList<>();
                final JSONArray jsonChildrenUpdates = (JSONArray)
                        ((JSONObject) jsonAnnualChange)
                                .get(Constants.CHILDREN_UPDATES);
                for (final Object jsonChildUpdate : jsonChildrenUpdates) {
                    final Integer id = ((Long) ((JSONObject) jsonChildUpdate)
                            .get(Constants.ID)).intValue();
                    Double niceScore = null;
                    if (((JSONObject) jsonChildUpdate)
                            .get(Constants.NICE_SCORE) != null) {
                        niceScore = ((Long) ((JSONObject) jsonChildUpdate)
                                .get(Constants.NICE_SCORE)).doubleValue();
                    }

                    final List<String> giftsPreferences = new ArrayList<>();
                    final JSONArray jsonGiftsPreferences = (JSONArray)
                            ((JSONObject) jsonChildUpdate)
                                    .get(Constants.GIFT_PREFERENCES);
                    for (final Object jsonGiftPreference : jsonGiftsPreferences) {
                        giftsPreferences.add((String) jsonGiftPreference);
                    }

                    final ElvesType newElf = Utils.getInstance().stringToElf(
                                    (String)
                                            ((JSONObject) jsonChildUpdate)
                                                    .get(Constants.ELF));
                    childrenUpdates.add(new ChildUpdate.Builder(id, niceScore,
                            Utils.getInstance()
                                    .stringListToCategoryList(
                                            giftsPreferences))
                            .elfType(newElf)
                            .build());
                }
                CityStrategyEnum strategyEnum = CityStrategyEnum.ID;
                if (((JSONObject) jsonAnnualChange)
                        .get(Constants.STRATEGY) != null) {
                    strategyEnum = Utils.getInstance().stringToStrategy(
                            (String) ((JSONObject) jsonAnnualChange)
                                    .get(Constants.STRATEGY));
                }
                annualChanges.add(new AnnualChanges(newSantaBudget, newGifts,
                        newChildren, childrenUpdates, strategyEnum));
            }
        }
        return annualChanges;
    }
}
