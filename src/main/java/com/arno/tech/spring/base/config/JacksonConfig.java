package com.arno.tech.spring.base.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * Springboot内置bean序列化解析器配置
 *
 * @author ArnoFrost
 * @since 2023/03/02
 */
@Configuration
public class JacksonConfig {


    /**
     * String null 返回 ""
     * Array null 返回 []
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = converter.getObjectMapper();
        mapper.setSerializerFactory(mapper.getSerializerFactory().withSerializerModifier(new NullValueSerializerModifier()));
        //如果定义的bean元素不含有任何成员,则在序列化时忽略该bean
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return converter;
    }

    /**
     * 空值序列化解析器Modifier
     */
    private static class NullValueSerializerModifier extends BeanSerializerModifier {

        private final NullStringJsonSerializer _nullStringJsonSerializer = new NullStringJsonSerializer();
        private final NullMapSerializer _nullMapSerializer = new NullMapSerializer();
        private final NullArrayJsonSerializer _nullArrayJsonSerializer = new NullArrayJsonSerializer();
        private final NullToEmptySerializer _nullToEmptySerializer = new NullToEmptySerializer();

        @Override
        public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                         List<BeanPropertyWriter> beanProperties) {
            for (BeanPropertyWriter beanProperty : beanProperties) {

                if (isArrayType(beanProperty)) { // 数组类型
                    beanProperty.assignNullSerializer(this._nullArrayJsonSerializer);
                } else if (isStringType(beanProperty)) { // 字符串类型
                    beanProperty.assignNullSerializer(this._nullStringJsonSerializer);
                } else if (isMapType(beanProperty)) { // Map类型
                    beanProperty.assignNullSerializer(this._nullMapSerializer);
                } else {  //其他情况返回空对象
                    beanProperty.assignNullSerializer(this._nullToEmptySerializer);
                }
            }
            return beanProperties;
        }

        private boolean isArrayType(BeanProperty property) {
            Class<?> clazz = property.getType().getRawClass();
            return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
        }


        private boolean isStringType(BeanProperty property) {
            Class<?> clazz = property.getType().getRawClass();
            return CharSequence.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz);
        }

        private boolean isMapType(BeanProperty property) {
            Class<?> clazz = property.getType().getRawClass();
            return Map.class.isAssignableFrom(clazz);
        }
    }

    private static class NullStringJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {
            if (o == null) {
                jsonGenerator.writeString("");
            }
        }
    }

    private static class NullArrayJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {
            if (o == null) {
                jsonGenerator.writeStartArray();
                jsonGenerator.writeEndArray();
            }
        }
    }

    private static class NullMapSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {
            if (o == null) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeEndObject();
            }
        }
    }

    /**
     * 用于注释用返回空对象接口
     * <p>
     * 示例:
     * </p>
     * <pre>
     * &#064;JsonSerialize(nullsUsing  = JacksonConfig.NullToEmptySerializer.class)
     * private T data;
     * </pre>
     *
     * @author ArnoFrost
     * @date 2022/12/26
     */
    public static class NullToEmptySerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
            if (value == null) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeEndObject();
            } else {
                //使用nullsUsing时理论上不会走到这里， 但为了防止开发人员误用， 这里仍旧做了健壮性处理
                ObjectMapper mapper = new ObjectMapper();
                String res = mapper.writeValueAsString(value);
                jsonGenerator.writeString(res);
            }

        }
    }
}