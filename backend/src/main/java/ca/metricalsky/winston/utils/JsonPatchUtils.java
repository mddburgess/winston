package ca.metricalsky.winston.utils;

import ca.metricalsky.winston.api.model.PatchOperation;
import ca.metricalsky.winston.exception.AppException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JsonPatchUtils {

    private final ObjectMapper objectMapper;

    public <T> T applyPatch(T object, List<PatchOperation> patchOperations) {
        var jsonPatch = objectMapper.convertValue(patchOperations, JsonPatch.class);
        var objectNode = objectMapper.convertValue(object, JsonNode.class);

        try {
            var patchedObjectNode = jsonPatch.apply(objectNode);
            return objectMapper.convertValue(patchedObjectNode, (Class<T>) object.getClass());
        } catch (JsonPatchException ex) {
            throw new AppException(ex);
        }
    }
}
