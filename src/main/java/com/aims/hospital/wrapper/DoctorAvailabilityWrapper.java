package com.aims.hospital.wrapper;

import com.aims.hospital.model.DoctorAvailability;

import java.util.ArrayList;
import java.util.List;

public class DoctorAvailabilityWrapper {
    private List<DoctorAvailability> availabilities = new ArrayList<>();

    public List<DoctorAvailability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(List<DoctorAvailability> availabilities) {
        this.availabilities = availabilities;
    }
}
