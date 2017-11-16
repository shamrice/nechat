package io.github.shamrice.neChat.client.ui.main.buddy;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.Observable;

/**
 * Created by Erik on 11/16/2017.
 */
public class BuddyModel {

    private StringProperty buddyLogin;

    public BuddyModel() {
        this(null);
    }

    public BuddyModel(String buddyLogin) {
        this.buddyLogin = new SimpleStringProperty(buddyLogin);
    }

    public void setBuddyLogin(String buddyLogin) {
        this.buddyLogin.set(buddyLogin);
    }

    public ObservableValue<String> getBuddyLogin() {
        return buddyLogin;
    }
}
