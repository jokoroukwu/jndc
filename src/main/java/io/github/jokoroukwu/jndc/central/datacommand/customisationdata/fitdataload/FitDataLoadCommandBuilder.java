package io.github.jokoroukwu.jndc.central.datacommand.customisationdata.fitdataload;

import io.github.jokoroukwu.jndc.mac.MacAcceptor;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.text.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FitDataLoadCommandBuilder implements MacAcceptor<FitDataLoadCommandBuilder> {
    private List<Fit> fits;
    private String mac;

    public FitDataLoadCommandBuilder() {
        this.fits = Collections.emptyList();
        this.mac = Strings.EMPTY_STRING;
    }

    public FitDataLoadCommandBuilder withFits(List<Fit> fits) {
        this.fits = fits;
        return this;
    }

    public FitDataLoadCommandBuilder addFit(Fit fit) {
        ObjectUtils.validateNotNull(fit, "fit");
        lazyInitFits();
        fits.add(fit);
        return this;
    }

    private void lazyInitFits() {
        if (this.fits == Collections.EMPTY_LIST) {
            this.fits = new ArrayList<>();
        }
    }

    @Override
    public FitDataLoadCommandBuilder withMac(String mac) {
        this.mac = ObjectUtils.validateNotNull(mac, "mac");
        return this;
    }

    public FitDataLoadCommand build() {
        return new FitDataLoadCommand(fits, mac);
    }

}
