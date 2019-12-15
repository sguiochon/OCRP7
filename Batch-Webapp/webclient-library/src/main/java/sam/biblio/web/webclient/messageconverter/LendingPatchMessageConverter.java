package sam.biblio.web.webclient.messageconverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import sam.biblio.model.library.Lending;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LendingPatchMessageConverter implements HttpMessageConverter<Lending> {

    private static final List<MediaType> MEDIA_TYPES = new ArrayList();

    static {
        MEDIA_TYPES.add(MediaType.parseMediaType("application/merge-patch+json"));
    }

    @Override
    public boolean canRead(Class<?> aClass, MediaType mediaType) {
        if (null == mediaType) {
            return false;
        } else {
            return Lending.class.isAssignableFrom(aClass) && mediaType.getSubtype().contains("merge-patch+json");
        }
    }

    @Override
    public boolean canWrite(Class<?> aClass, MediaType mediaType) {
        return this.canRead(aClass, mediaType);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return MEDIA_TYPES;
    }

    @Override
    public Lending read(Class<? extends Lending> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    public void write(Lending lending, MediaType mediaType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputMessage.getBody()));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(writer, lending);
        //writer.flush();
    }
}
