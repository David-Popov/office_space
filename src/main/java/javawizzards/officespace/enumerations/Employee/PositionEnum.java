package javawizzards.officespace.enumerations.Employee;

public enum PositionEnum {
    MANAGER("Manager"),
    TEAM_LEAD("Team Lead"),
    PRODUCT_MANAGER("Product Manager"),
    SENIOR_DEVELOPER("Senior Developer"),
    JUNIOR_DEVELOPER("Junior Developer"),
    INTERN("Intern"),
    UX_UI_DESIGNER("UX/UI Designer"),
    QA_ENGINEER("QA Engineer"),
    TECH_SUPPORT_SPECIALIST("Tech Support Specialist"),
    SYSTEM_ADMINISTRATOR("System Administrator"),
    HR("HR"),
    ACCOUNTANT("Accountant");

    private final String positionName;

    PositionEnum(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionName() {
        return positionName;
    }
}
