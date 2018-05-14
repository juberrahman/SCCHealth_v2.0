package nsf.esarplab.scchealth;
public class Person {
    private String patientID;
    private String gridCode;
    private String diseaseType;

    private String eoi;
    private String time;
    private String algorithm;

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getGridCode() {
        return gridCode;
    }

    public void setGridCode(String gridCode) {
        this.gridCode = gridCode;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }
    public String getEoi() {
        return eoi;
    }

    public void setEoi(String eoi) {
        this.eoi = eoi;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }



    @Override
    public String toString() {
        return "Person [ID=" + patientID + ", GRID=" + gridCode + ", Disease Type="
                + diseaseType +", eoi="+eoi+", datetime="+time+", Algorithm="+algorithm+ "]";
    }



}
