package io.github.jokoroukwu.jndc.field.fieldmeta;

import java.util.Optional;

public final class FieldMetaSkipStrategies {
    public static final FieldMetaSkipStrategy NO_META = buffer -> Optional.empty();
    public static final FieldMetaSkipStrategy ALWAYS_SKIP_FS
            = buffer -> buffer.trySkipFieldSeparator().map(errorMessage -> "no field separator before field");
    public static final FieldMetaSkipStrategy OPTIONALLY_SKIP_GS = buffer
            -> buffer.hasRemaining() && !buffer.hasFollowingFieldSeparator()
            ? buffer.trySkipGroupSeparator().map(errorMessage -> "no group separator before field")
            : Optional.empty();
    public static final FieldMetaSkipStrategy OPTIONALLY_SKIP_FS = buffer
            -> buffer.hasRemaining() && !buffer.hasFollowingGroupSeparator()
            ? buffer.trySkipFieldSeparator().map(errorMessage -> "no field separator before field")
            : Optional.empty();

    private FieldMetaSkipStrategies() {
    }


}
