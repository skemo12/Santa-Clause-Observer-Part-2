package annualchanges;

import enums.Category;
import enums.ElvesType;

import java.util.List;

/**
 * Class that stores the data of a child update from input
 */
public final class ChildUpdate {
    private Integer id;
    private Double niceScore;
    private List<Category> giftsPreferences;
    private ElvesType elf;

    public ChildUpdate(final Integer id, final Double niceScore,
                       final List<Category> giftsPreferences) {
        this.setId(id);
        this.setNiceScore(niceScore);
        this.setGiftsPreferences(giftsPreferences);
    }

    public static final class Builder {
        private final Integer id;
        private final Double niceScore;
        private final List<Category> giftsPreferences;
        private ElvesType elf;

        public Builder(final Integer id, final Double niceScore,
                       final List<Category> giftsPreferences) {
            this.id = id;
            this.niceScore = niceScore;
            this.giftsPreferences = giftsPreferences;
        }

        /**
         * Sets elf from builder
         */
        public Builder elfType(final ElvesType elfSetter) {
            this.elf = elfSetter;
            return this;
        }
        /**
         * build method for Builder
         */
        public ChildUpdate build() {
            return new ChildUpdate(this);
        }
    }

    private ChildUpdate(final Builder builder) {
        this.id = builder.id;
        this.giftsPreferences = builder.giftsPreferences;
        this.niceScore = builder.niceScore;
        this.elf = builder.elf;

    }
    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Double getNiceScore() {
        return niceScore;
    }

    public void setNiceScore(final Double niceScore) {
        this.niceScore = niceScore;
    }

    public List<Category> getGiftsPreferences() {
        return giftsPreferences;
    }

    public void setGiftsPreferences(final List<Category> giftsPreferences) {
        this.giftsPreferences = giftsPreferences;
    }

    public ElvesType getElf() {
        return elf;
    }

    public void setElf(final ElvesType elf) {
        this.elf = elf;
    }
}
