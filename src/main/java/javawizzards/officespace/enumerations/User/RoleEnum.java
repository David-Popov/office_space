package javawizzards.officespace.enumerations.User;

public enum RoleEnum {
    USER("USER"),
    ADMIN("ADMIN"),
    MANAGER("MANAGER");

    private final String roleName;

    RoleEnum(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
