package yizx.createshortcut;

import java.io.Serializable;

/**
 * Created by yizx on 7/30/15.
 */
public class Contact implements Serializable{

    private byte[] contact_photo;
    private String contact_name;
    private String contact_number;

    public byte[] getContact_photo() {
        return contact_photo;
    }

    public void setContact_photo(byte[] contact_photo) {
        this.contact_photo = contact_photo;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public Contact(){
        contact_photo = null;
        contact_name = null;
        contact_number = null;
    }

}
