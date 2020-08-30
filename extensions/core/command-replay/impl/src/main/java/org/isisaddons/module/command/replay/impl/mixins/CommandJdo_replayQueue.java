package org.isisaddons.module.command.replay.impl.mixins;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Mixin;

import org.isisaddons.module.command.dom.CommandJdo;
import org.isisaddons.module.command.dom.CommandServiceJdoRepository;
import org.isisaddons.module.command.replay.IsisModuleExtCommandReplayImpl;
import org.isisaddons.module.command.replay.impl.SlaveConfiguration;

@Collection(
        domainEvent = CommandJdo_replayQueue.CollectionDomainEvent.class
)
@CollectionLayout(
        defaultView = "table"
)
@Mixin(method = "coll")
public class CommandJdo_replayQueue {

    public static class CollectionDomainEvent
            extends IsisModuleExtCommandReplayImpl.CollectionDomainEvent<CommandJdo_replayQueue, CommandJdo> { }

    private final CommandJdo commandJdo;
    public CommandJdo_replayQueue(final CommandJdo commandJdo) {
        this.commandJdo = commandJdo;
    }

    @MemberOrder(sequence = "100.100")
    public List<CommandJdo> coll() {
        return commandServiceJdoRepository.findReplayedOnSlave();
    }

    public boolean hideColl() {
        return !slaveConfiguration.isConfigured();
    }

    @Inject SlaveConfiguration slaveConfiguration;
    @Inject CommandServiceJdoRepository commandServiceJdoRepository;

}