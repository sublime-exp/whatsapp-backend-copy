package com.sublime.whatsappclone.shared.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class State<T, V> {
    private StatusNotification status;
    private T value;
    private V error;

    public static <T, V> StateBuilder<T, V> builder() {
        return new StateBuilder<>();
    }
}