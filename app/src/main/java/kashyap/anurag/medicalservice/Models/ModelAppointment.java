package kashyap.anurag.medicalservice.Models;

public class ModelAppointment {

    String problem, specialization, availableTime, department, appointmentId, patientUid, date, time, isDoctorAppointed, doctorUid, appointmentDate, isSuccessful;

    public ModelAppointment() {
    }

    public ModelAppointment(String problem, String specialization, String availableTime, String department, String appointmentId, String patientUid, String date, String time, String isDoctorAppointed, String doctorUid, String appointmentDate, String isSuccessful) {
        this.problem = problem;
        this.specialization = specialization;
        this.availableTime = availableTime;
        this.department = department;
        this.appointmentId = appointmentId;
        this.patientUid = patientUid;
        this.date = date;
        this.time = time;
        this.isDoctorAppointed = isDoctorAppointed;
        this.doctorUid = doctorUid;
        this.appointmentDate = appointmentDate;
        this.isSuccessful = isSuccessful;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
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

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientUid() {
        return patientUid;
    }

    public void setPatientUid(String patientUid) {
        this.patientUid = patientUid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIsDoctorAppointed() {
        return isDoctorAppointed;
    }

    public void setIsDoctorAppointed(String isDoctorAppointed) {
        this.isDoctorAppointed = isDoctorAppointed;
    }

    public String getDoctorUid() {
        return doctorUid;
    }

    public void setDoctorUid(String doctorUid) {
        this.doctorUid = doctorUid;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getIsSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccessful(String isSuccessful) {
        this.isSuccessful = isSuccessful;
    }
}
