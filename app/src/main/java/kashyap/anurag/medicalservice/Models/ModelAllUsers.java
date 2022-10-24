package kashyap.anurag.medicalservice.Models;

public class ModelAllUsers {

    String name, email, profileImage, availableTime, department, specialization, userType;

    public ModelAllUsers() {
    }

    public ModelAllUsers(String name, String email, String profileImage, String availableTime, String department, String specialization, String userType) {
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
        this.availableTime = availableTime;
        this.department = department;
        this.specialization = specialization;
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(String availableTime) {
        this.availableTime = availableTime;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
