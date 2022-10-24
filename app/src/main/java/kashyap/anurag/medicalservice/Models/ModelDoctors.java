package kashyap.anurag.medicalservice.Models;

public class ModelDoctors {

    String name, email, phoneNo, availableTime, department, specialization, profileImage, userType, uid;

    public ModelDoctors() {
    }

    public ModelDoctors(String name, String email, String phoneNo, String availableTime, String department, String specialization, String profileImage, String userType, String uid) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.availableTime = availableTime;
        this.department = department;
        this.specialization = specialization;
        this.profileImage = profileImage;
        this.userType = userType;
        this.uid = uid;
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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
