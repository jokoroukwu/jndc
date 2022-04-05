package io.github.jokoroukwu.jndc.field.fieldindicator;

public final class FieldPresenceIndicators {

    /**
     * The field is considered present when buffer has remaining data and next character
     * is not a FS character.
     */
    public static final FieldPresenceIndicator PRESENT_UNLESS_FS_FOLLOWS = (buffer, deviceConfig)
            -> buffer.hasRemaining() && !buffer.hasFollowingFieldSeparator();
    /**
     * The field is mandatory and should always be present.
     */
    public static final FieldPresenceIndicator MANDATORY = (buffer, deviceConfig) -> true;
    /**
     * The field is considered present when:
     * <ul>
     *     <li>MAC is disabled and buffer has any remaining data</li>
     *     <li>MAC is enabled and buffer has more than 9 characters remaining</li>
     * </ul>
     */
    public static final FieldPresenceIndicator REMAINING = (buffer, deviceConfig) -> {
        if (deviceConfig.isMacEnabled()) {
            return buffer.remaining() > 9;
        }
        return buffer.hasRemaining();
    };
    /**
     * The field is considered present if the following character is not a separator character.
     */
    public static final FieldPresenceIndicator FIELD_DATA_REMAINING = (buffer, deviceConfig) -> buffer.hasFieldDataRemaining();

    private FieldPresenceIndicators() {
    }
}