package org.morgan.bookstore.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Government {

    ALEXANDRIA("Alexandria", "Alexandria", 50),
    ASWAN("Aswan", "Upper Egypt", 70),
    ASYUT("Asyut", "Upper Egypt", 70),
    BENI_SUEF("Beni Suef", "Upper Egypt", 70),
    RED_SEA("Red Sea", "border", 85),
    BEHEIRA("Beheira", "Delta & Canal", 55),
    CAIRO("Cairo", "Cairo & Giza", 45),
    DAKAHLIA("Dakahlia", "Delta & Canal", 55),
    DAMIETTA("Damietta", "Delta & Canal", 55),
    FAIYUM("Faiyum", "Upper Egypt", 70),
    GHARBIA("Gharbia", "Delta & Canal", 55),
    GIZA("Giza", "Cairo & Giza", 45),
    ISMAILIA("Ismailia", "Delta & Canal", 55),
    SOUTH_SINIA("South Sinia", "border", 85),
    QALYUBIA("Qalyubia", "Delta & Canal", 55),
    KAFR_EL_SHEIKH("Kafr el-Sheikh", "Delta & Canal", 55),
    QENA("Qena", "Upper Egypt", 70),
    LUXOR("Luxor", "Upper Egypt", 70),
    MINYA("Minya", "Upper Egypt", 70),
    MOUNFIA("Mounfia", "Delta & Canal", 55),
    MATROUH("Matrouh", "border", 85),
    PORT_SAID("Port Said", "Delta & Canal", 55),
    SOHAG("Sohag", "Upper Egypt", 70),
    AL_SHARQIA("Al Sharqia", "Delta & Canal", 55),
    NORTH_SINAI("North Sinai", "border", 85),
    SUEZ("Suez", "Delta & Canal", 55),
    NEW_VALLEY("New Valley", "border", 85);

    private final String governmentName;
    private final String region;
    private final double shippingPrice;

    Government(String governmentName, String region, double shippingPrice) {
        this.governmentName = governmentName;
        this.region = region;
        this.shippingPrice = shippingPrice;
    }

    @JsonCreator
    public static Government fromValue(String value) {
        for (Government government : Government.values()) {
            if (government.governmentName.equals(value)) {
                return government;
            }
        }
        throw new IllegalArgumentException("Invalid government value: " + value);
    }

    public static List<String> getGovernments() {
        List<String> governments = new ArrayList<>();
        for (Government government : Government.values()) {
            governments.add(government.governmentName);
        }
        return governments;
    }
}

