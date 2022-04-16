package io.github.jokoroukwu.jndc.central.datacommand.extendedencyrptionkeychange;

import io.github.jokoroukwu.jndc.NdcComponent;
import io.github.jokoroukwu.jndc.central.CentralMessageClass;
import io.github.jokoroukwu.jndc.central.datacommand.DataCommandSubClass;
import io.github.jokoroukwu.jndc.util.NdcStringBuilder;
import io.github.jokoroukwu.jndc.util.ObjectUtils;

import java.util.Objects;
import java.util.StringJoiner;

public class ExtendedEncryptionKeyChangeCommand implements NdcComponent {
    public static final String COMMAND_NAME = CentralMessageClass.DATA_COMMAND + ": " + DataCommandSubClass.EXTENDED_ENCRYPTION_KEY_INFO;
    private final Modifier modifier;
    private final String keyData;


    public ExtendedEncryptionKeyChangeCommand(Modifier modifier, String keyData) {
        this.modifier = ObjectUtils.validateNotNull(modifier, "'Modifier'");
        this.keyData = ObjectUtils.validateNotNull(keyData, "'New Key Data'");
    }


    public Modifier getModifier() {
        return modifier;
    }


    public String getKeyData() {
        return keyData;
    }


    @Override
    public String toNdcString() {
        final NdcStringBuilder builder = new NdcStringBuilder(5 + keyData.length())
                .appendComponent(modifier);
        if (!keyData.isEmpty()) {
            builder.appendZeroPaddedHex(keyData.length(), 3)
                    .append(keyData);
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ExtendedEncryptionKeyChangeCommand.class.getSimpleName() + ": {", "}")
                .add("modifier: " + modifier)
                .add("keyData: '" + keyData + '\'')
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtendedEncryptionKeyChangeCommand that = (ExtendedEncryptionKeyChangeCommand) o;
        return modifier == that.modifier && keyData.equals(that.keyData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modifier, keyData);
    }
}
