package com.branwilliams.bundi.engine.core;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * @author Brandon
 * @since August 16, 2019
 */
public class Keycode {
    private int keyCode;

    public Keycode(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public static class KeycodeDeserializer implements JsonDeserializer<Keycode> {

        private final Keycodes keycodes;

        public KeycodeDeserializer(Keycodes keycodes) {
            this.keycodes = keycodes;
        }

        @Override
        public Keycode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String keyCode = json.getAsString().toLowerCase();
            return new Keycode(keycodes.getKeycode(keyCode));
        }
    }
}