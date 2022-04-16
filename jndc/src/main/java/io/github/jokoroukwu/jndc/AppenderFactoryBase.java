package io.github.jokoroukwu.jndc;

import io.github.jokoroukwu.jndc.util.ObjectUtils;
import io.github.jokoroukwu.jndc.util.optional.DescriptiveOptional;

import java.util.Map;

public class AppenderFactoryBase<K, T> implements NdcComponentAppenderFactory<K, T> {
    private final Map<K, NdcComponentAppender<T>> readerMap;
    private final String errorMessageTemplate;

    public AppenderFactoryBase(Map<K, NdcComponentAppender<T>> readerMap) {
        this.readerMap = ObjectUtils.validateNotNull(readerMap, "readerMap");
        errorMessageTemplate = "no reader for key '%s': does not match any of the available keys: "
                .concat(readerMap.keySet().toString());
    }

    @Override
    public DescriptiveOptional<NdcComponentAppender<T>> getAppender(K key) {
        ObjectUtils.validateNotNull(key, "key");
        return DescriptiveOptional.ofNullable(readerMap.get(key), () -> String.format(errorMessageTemplate, key));
    }

}
