package com.example.inmobiliariazaratemobile.ui.inquilinos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InquilinosViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public InquilinosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Esto es INQUILINOS!!!!!!!!!!!!!!!!!!!! ");
    }

    public LiveData<String> getText() {
        return mText;
    }
}