package demoapp.dom.types.javalang.integers.jdo;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.testing.fixtures.applib.fixturescripts.FixtureScript;

import demoapp.dom._infra.seed.SeedServiceAbstract;
import demoapp.dom.types.Samples;

@Service
public class WrapperIntegerJdoSeedService extends SeedServiceAbstract {

    public WrapperIntegerJdoSeedService() {
        super(WrapperIntegerJdoEntityFixture::new);
    }

    static class WrapperIntegerJdoEntityFixture extends FixtureScript {

        @Override
        protected void execute(ExecutionContext executionContext) {
            samples.stream()
                    .map(WrapperIntegerJdo::new)
                    .forEach(domainObject -> {
                        repositoryService.persist(domainObject);
                        executionContext.addResult(this, domainObject);
                    });
        }

        @Inject
        RepositoryService repositoryService;

        @Inject
        Samples<Integer> samples;
    }
}
