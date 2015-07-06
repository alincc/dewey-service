package no.nb.microservice.dewey.rest.model;

/**
 * Created by raymondk on 6/25/15.
 */
public class Dewey {
    private int level;
    private String classValue;
    private String heading;
    private int count;

    public Dewey() {
    }

    public Dewey(int level, String classValue, String heading, int count) {
        this.level = level;
        this.classValue = classValue;
        this.heading = heading;
        this.count = count;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return the classValue
     */
    public String getClassValue() {
        return classValue;
    }

    /**
     * @param classValue the classValue to set
     */
    public void setClassValue(String classValue) {
        this.classValue = classValue;
    }

    /**
     * @return the heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     * @param heading the heading to set
     */
    public void setHeading(String heading) {
        this.heading = heading;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Dewey{" + "level=" + level + ", classValue=" + classValue + ", heading=" + heading + ", count=" + count + '}';
    }
}
