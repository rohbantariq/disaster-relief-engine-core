public class DisasterArea implements Comparable<DisasterArea> {
    private String areaName;
    private int population;
    private int injuries;
    private int severityScore;
    private String severityType;

    public DisasterArea(String areaName, int population, int injuries, int severityScore) throws Exception{
        if(severityScore < 1 || severityScore > 10){
            throw new Exception("Invalid severity score. Severity must be between 1 and 10!");
        }
        this.areaName = areaName;
        this.population = population;
        this.injuries = injuries;
        this.severityScore = severityScore;
        this.severityType = (severityScore >= 6)?"HIGH" : "NORMAL";

    }

    @Override
    public int compareTo(DisasterArea other) {
        return Integer.compare(other.severityScore, this.severityScore);
    }

    public String getAreaName() { return areaName; }
    public int getPopulation() { return population; }
    public int getInjuries() { return injuries; }
    public int getSeverityScore() { return severityScore; }
    public String getSeverityType() { return severityType; }

    @Override
    public String toString() {
        return String.format("Area: %s | Population: %d | Injuries: %d | Severity: %d",
                areaName, population, injuries, severityScore);
    }
}