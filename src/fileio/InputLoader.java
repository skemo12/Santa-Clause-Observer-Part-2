package fileio;

import common.Constants;
import data.Database;
import child.Child;
import enums.CityStrategyEnum;
import enums.ElvesType;
import santa.Gift;
import annualchanges.AnnualChanges;
import annualchanges.ChildUpdate;
import santa.Santa;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser
                    .parse(new FileReader(inputPath));
            JSONObject initialData = (JSONObject) jsonObject
                    .get(Constants.INITIAL_DATA);
            JSONArray jsonChildren = (JSONArray) initialData
                    .get(Constants.CHILDREN);
            Double santaBudget = ((Long) (jsonObject)
                    .get(Constants.SANTA_BUDGET)).doubleValue();
            Integer numberOfYears = ((Long) (jsonObject)
                    .get(Constants.NUMBER_OF_YEARS)).intValue();
            JSONArray jsonSantaGiftsList = (JSONArray) initialData.
                    get(Constants.SANTA_GIFTS_LIST);
            JSONArray jsonAnnualChanges = (JSONArray) jsonObject
                    .get(Constants.ANNUAL_CHANGES);


            List<Child> children = readChildList(jsonChildren);
            List<Gift> giftList = readGiftsList(jsonSantaGiftsList);
            List<AnnualChanges> annualChanges = readAnnualChanges(
                    jsonAnnualChanges);

            Database.getInstance().setNumberOfYears(numberOfYears);
            Database.getInstance().setChildren(children);
            Database.getInstance().setAnnualChanges(annualChanges);
            Database.getInstance().setSanta(new Santa(santaBudget, giftList));


        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * Creates Children list from jsonArray
     */
    public List<Child> readChildList(final JSONArray jsonChildren) {
        List<Child> children = new ArrayList<>();
        if (jsonChildren != null) {
            for (Object jsonChild : jsonChildren) {
                JSONArray jsonGiftsPreferences = (JSONArray)
                        ((JSONObject) jsonChild)
                                .get(Constants.GIFT_PREFERENCES);
                List<String> giftPreferences = new ArrayList<>();
                for (Object gift : jsonGiftsPreferences) {
                    giftPreferences.add(((String) gift));
                }
                children.add(new Child(
                                ((Long) ((JSONObject) jsonChild)
                                        .get(Constants.ID)).intValue(),
                                ((Long) ((JSONObject) jsonChild)
                                        .get(Constants.AGE)).intValue(),
                                ((Long) ((JSONObject) jsonChild)
                                        .get(Constants.NICE_SCORE)).doubleValue(),
                                (String) ((JSONObject) jsonChild)
                                        .get(Constants.FIRST_NAME),
                                (String) ((JSONObject) jsonChild)
                                        .get(Constants.LAST_NAME),
                                Utils.getInstance().stringToCity(
                                        (String) ((JSONObject) jsonChild)
                                                .get(Constants.CITY)),
                                Utils.getInstance()
                                        .stringListToCategoryList(giftPreferences),
                                Utils.getInstance().stringToElf((String)
                                ((JSONObject) jsonChild).get(Constants.ELF)),
                                ((Long) ((JSONObject) jsonChild)
                                        .get(Constants.NICE_SCORE_BONUS))
                                        .doubleValue())
                        );
            }
        }
        return children;
    }

    /**
     * Creates Gift list from jsonArray
     */
    public List<Gift> readGiftsList(final JSONArray jsonSantaGiftsList) {
        List<Gift> giftList = new ArrayList<>();
        if (jsonSantaGiftsList != null) {
            for (Object jsonGift : jsonSantaGiftsList) {
                Gift gift = new Gift(
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
        List<AnnualChanges> annualChanges = new ArrayList<>();
        Double newSantaBudget = null;
        if (jsonAnnualChanges != null) {
            for (Object jsonAnnualChange : jsonAnnualChanges) {

                if (((JSONObject) jsonAnnualChange)
                        .get(Constants.NEW_SANTA_BUDGET) != null) {
                    newSantaBudget =
                            ((Long) ((JSONObject) jsonAnnualChange)
                                    .get(Constants.NEW_SANTA_BUDGET))
                                    .doubleValue();
                }

                JSONArray jsonNewGifts = (JSONArray)
                        ((JSONObject) jsonAnnualChange)
                                .get(Constants.NEW_GIFTS);
                List<Gift> newGifts = readGiftsList(jsonNewGifts);

                JSONArray jsonNewChildren = (JSONArray)
                        ((JSONObject) jsonAnnualChange)
                                .get(Constants.NEW_CHILDREN);
                List<Child> newChildren = readChildList(jsonNewChildren);

                List<ChildUpdate> childrenUpdates = new ArrayList<>();
                JSONArray jsonChildrenUpdates = (JSONArray)
                        ((JSONObject) jsonAnnualChange)
                                .get(Constants.CHILDREN_UPDATES);
                for (Object jsonChildUpdate : jsonChildrenUpdates) {
                    Integer id = ((Long) ((JSONObject) jsonChildUpdate)
                            .get(Constants.ID)).intValue();
                    Double niceScore = null;
                    if (((JSONObject) jsonChildUpdate)
                            .get(Constants.NICE_SCORE) != null) {
                        niceScore = ((Long) ((JSONObject) jsonChildUpdate)
                                .get(Constants.NICE_SCORE)).doubleValue();
                    }

                    List<String> giftsPreferences = new ArrayList<>();
                    JSONArray jsonGiftsPreferences = (JSONArray)
                            ((JSONObject) jsonChildUpdate)
                                    .get(Constants.GIFT_PREFERENCES);
                    for (Object jsonGiftPreference : jsonGiftsPreferences) {
                        giftsPreferences.add((String) jsonGiftPreference);
                    }

                    ElvesType newElf = Utils.getInstance().stringToElf(
                                    (String)
                                            ((JSONObject) jsonChildUpdate)
                                                    .get(Constants.ELF));
                    childrenUpdates.add(new ChildUpdate(id, niceScore,
                            Utils.getInstance()
                                    .stringListToCategoryList(
                                            giftsPreferences), newElf));
                }
                CityStrategyEnum strategyEnum = CityStrategyEnum.ID;
                if (((JSONObject) jsonAnnualChange)
                        .get(Constants.STRATEGY) != null) {
                    strategyEnum = Utils.getInstance().stringToStrategy(
                            (String) ((JSONObject) jsonAnnualChange)
                                    .get(Constants.STRATEGY) );
                }
                annualChanges.add(new AnnualChanges(newSantaBudget, newGifts,
                        newChildren, childrenUpdates, strategyEnum));
            }
        }
        return annualChanges;
    }
}
