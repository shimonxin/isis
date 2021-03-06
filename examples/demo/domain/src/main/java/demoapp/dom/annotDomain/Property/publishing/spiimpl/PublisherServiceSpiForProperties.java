package demoapp.dom.annotDomain.Property.publishing.spiimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import org.apache.isis.applib.services.iactn.Interaction;
import org.apache.isis.applib.services.publish.PublishedObjects;
import org.apache.isis.applib.services.publish.PublisherService;
import org.apache.isis.applib.util.schema.InteractionDtoUtils;
import org.apache.isis.schema.ixn.v2.InteractionDto;

import lombok.val;

//tag::class[]
@Service
public class PublisherServiceSpiForProperties implements PublisherService {

    private final List<InteractionDto> executions = new ArrayList<>();

    @Override
    public void publish(Interaction.Execution<?, ?> execution) {
        val dto = InteractionDtoUtils.newInteractionDto(            // <.>
                    execution, InteractionDtoUtils.Strategy.DEEP);
        executions.add(dto);
    }
    // ...
//end::class[]

//tag::demo[]
    public Stream<InteractionDto> streamInteractionDtos() {
        return executions.stream();
    }

    public void clear() {
        executions.clear();
    }
//end::demo[]

    @Override
    public void publish(PublishedObjects publishedObjects) {
    }

//tag::class[]
}
//end::class[]
