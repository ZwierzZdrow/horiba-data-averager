package com.ce2tech.averager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum AcceptableComponents {
    //From horiba file
    DATE("Data"), TIME("Czas"), NO("NO[ppm]"), NOx("NOx[ppm]"),
    CO("CO[ppm]"), CO2("CO2[vol%]"), O2("O2[vol%]"), SO2("SO2[ppm]"),
    TEMP("Temp"),

    //From sick dustmeter
    AMS_TIME("Recording time"),
    K3_SL("DH SB100   (Sensor  1 - K3).MV[0].Phys_reg"),
    K2_SL("DH SB100   (Sensor  1).MV[0].Phys_reg"),
    K3_MA("MCU (K3).ActValueCurrent[0]"),
    K2_MA("MCU (Kanal K2).ActValueCurrent[0]");

    @Getter @Setter String description;

    public static boolean isAcceptableMeasurand(String headerCell) {
        for (AcceptableComponents component : AcceptableComponents.values()) {
            if ( headerCell.contains(component.description) ) return true;
        }
        return false;
    }

}
