package in.plasmado.model;

import static in.plasmado.helper.ParentHelper.convertMongodbObjToString;
import static in.plasmado.helper.ParentHelper.decrypt;

public class RequestModel {

    String _id;
    String name;
    String phone;
    String email;
    String age;
    String pin;
    String city;
    String district;
    String landmark;
    String state;
    String gender;
    String bloodgroup;
    String datetime;

    public RequestModel() {
    }

    public RequestModel(String _id, String name, String phone, String email, String age, String pin, String city, String district, String landmark, String state, String gender, String bloodgroup, String datetime) {
        this._id = _id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.age = age;
        this.pin = pin;
        this.city = city;
        this.district = district;
        this.landmark = landmark;
        this.state = state;
        this.gender = gender;
        this.bloodgroup = bloodgroup;
        this.datetime = datetime;
    }

    public String get_id() {
        return convertMongodbObjToString(_id);
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return decrypt(email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return decrypt(age);
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPin() {
        return decrypt(pin);
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getCity() {
        return decrypt(city);
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return decrypt(district);
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLandmark() {
        return decrypt(landmark);
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getState() {
        return decrypt(state);
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGender() {
        return decrypt(gender);
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
