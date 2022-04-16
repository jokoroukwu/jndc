package io.github.jokoroukwu.jndc.mac;

import io.github.jokoroukwu.jndc.NdcCharBuffer;
import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.Optional;

public enum MacReaderBase implements MacReader {
    INSTANCE;

    private static boolean isMacCharRangeValid(String value) {
        for (int i = 0, k = value.length(); i < k; ) {
            final char nextChar = value.charAt(i++);
            if ((nextChar > '9' && nextChar < 'A') || (nextChar < '0' || nextChar > 'F')) {
                return false;
            }
        }
        return true;
    }

    public static String validateMac(String mac) {
        ObjectUtils.validateNotNull(mac, "'Message Authentication Code (MAC)'");
        if (mac.isEmpty()) {
            return mac;
        }
        if (mac.length() != 8) {
            final String message = "'Message Authentication Code (MAC)' length should be exactly 8 characters but was %d for value '%s'";
            throw new IllegalArgumentException(String.format(message, mac.length(), mac));
        }
        if (!isMacCharRangeValid(mac)) {
            throw new IllegalArgumentException("value must be in character range [0-9A-F] but was: " + mac);
        }
        return mac;
    }

    @Override
    public DescriptiveOptional<String> tryReadMac(NdcCharBuffer ndcCharBuffer) {
        ObjectUtils.validateNotNull(ndcCharBuffer, "ndcCharBuffer");
        final Optional<String> optionalError = ndcCharBuffer.trySkipFieldSeparator();
        if (optionalError.isPresent()) {
            return DescriptiveOptional.empty(() -> "no field separator before field: " + optionalError.get());
        }
        return ndcCharBuffer.tryReadCharSequence(8)
                .filter(MacReaderBase::isMacCharRangeValid, mac -> ()
                        -> String.format("MAC should match [0-9A-Z] but was: '%s'", mac));
    }

}
