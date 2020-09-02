package org.apache.isis.extensions.commandlog.impl.jdo;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import org.apache.isis.applib.annotation.CommandExecuteIn;
import org.apache.isis.applib.annotation.CommandPersistence;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.OrderPrecedence;
import org.apache.isis.applib.services.command.Command;
import org.apache.isis.applib.services.command.Command.Executor;
import org.apache.isis.applib.services.command.spi.CommandService;
import org.apache.isis.applib.services.factory.FactoryService;
import org.apache.isis.applib.services.repository.RepositoryService;

import lombok.extern.log4j.Log4j2;

@Service
@Named("isisExtensionsCommandLog.CommandServiceJdo")
@Order(OrderPrecedence.MIDPOINT)
@Qualifier("Jdo")
@Log4j2
public class CommandServiceJdo implements CommandService {

    /**
     * {@inheritDoc}
     */
    @Override
    public Command create() {
        CommandJdo command = factoryService.instantiate(CommandJdo.class);
        command.internal().setExecutor(Executor.OTHER);
        command.internal().setPersistence(CommandPersistence.IF_HINTED);
        return command;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void complete(final Command command) {
        final CommandJdo commandJdo = asUserInitiatedCommandJdo(command);
        if(commandJdo == null) {
            return;
        }
        commandServiceJdoRepository.persistIfHinted(commandJdo);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean persistIfPossible(Command command) {
        if(!(command instanceof CommandJdo)) {
            // ought not to be the case, since this service created the object in the #create() method
            return false;
        }
        final CommandJdo commandJdo = (CommandJdo)command;
        repositoryService.persist(commandJdo);
        return true;
    }


    /**
     * Not API, also used by {@link CommandServiceJdoRepository}.
     */
    CommandJdo asUserInitiatedCommandJdo(final Command command) {
        if(!(command instanceof CommandJdo)) {
            // ought not to be the case, since this service created the object in the #create() method
            return null;
        }
        if(command.getExecuteIn() != CommandExecuteIn.FOREGROUND) {
            return null;
        } 
        final CommandJdo commandJdo = (CommandJdo) command;
        return commandJdo.shouldPersist()? commandJdo: null;
    }



    @Inject
    RepositoryService repositoryService;

    @Inject
    CommandServiceJdoRepository commandServiceJdoRepository;

    @Inject
    FactoryService factoryService;

}
