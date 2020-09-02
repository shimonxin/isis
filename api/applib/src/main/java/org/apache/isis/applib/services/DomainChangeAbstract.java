/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.isis.applib.services;

import java.sql.Timestamp;
import java.util.UUID;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.bookmark.Bookmark;
import org.apache.isis.applib.services.bookmark.BookmarkService;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.metamodel.BeanSort;
import org.apache.isis.applib.services.metamodel.MetaModelService;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;


/**
 * An abstraction of some sort of recorded change to a domain object: commands, audit entries or published events.
 */
@Log4j2
public abstract class DomainChangeAbstract
        implements HasUniqueId, HasUsername {

    public static enum ChangeType {
        COMMAND,
        AUDIT_ENTRY,
        PUBLISHED_INTERACTION;
        @Override
        public String toString() {
            return name().replace("_", " ");
        }
    }
    public DomainChangeAbstract(final ChangeType changeType) {
        this.type = changeType;
    }

    /**
     * Distinguishes commands from audit entries from published events/interactions (when these are shown mixed together in a (standalone) table).
     */
    @Property
    @PropertyLayout(
            hidden = Where.ALL_EXCEPT_STANDALONE_TABLES
    )
    @MemberOrder(name="Identifiers", sequence = "1")
    @Getter
    private final ChangeType type;



    /**
     * The user that caused the change.
     *
     * <p>
     * This dummy implementation is a trick so that Isis will render the property in a standalone table.  Each of the
     * subclasses override with the &quot;real&quot; implementation.
     */
    @Property
    @MemberOrder(name="Identifiers", sequence = "10")
    public String getUser() {
        return null;
    }



    /**
     * The time that the change occurred.
     *
     * <p>
     * This dummy implementation is a trick so that Isis will render the property in a standalone table.  Each of the
     * subclasses override with the &quot;real&quot; implementation.
     */
    @Property
    @MemberOrder(name="Identifiers", sequence = "20")
    public Timestamp getTimestamp() {
        return null;
    }


    /**
     * The unique identifier (a GUID) of the transaction in which this change occurred.
     *
     * <p>
     * This dummy implementation is a trick so that Isis will render the property in a standalone table.  Each of the
     * subclasses override with the &quot;real&quot; implementation.
     */
    @Property
    @MemberOrder(name="Identifiers",sequence = "50")
    public UUID getTransactionId() {
        return null;
    }


    /**
     * The class of the domain object being changed.
     *
     * <p>
     * This dummy implementation is a trick so that Isis will render the property in a standalone table.  Each of the
     * subclasses override with the &quot;real&quot; implementation.
     */
    @Property
    @PropertyLayout(named="Class")
    @MemberOrder(name="Target", sequence = "10")
    public String getTargetClass() {
        return null;
    }



    @Programmatic
    public Bookmark getTarget() {
        final String str = getTargetStr();
        return Bookmark.parse(str).orElse(null);
    }

    @Programmatic
    public void setTarget(Bookmark target) {
        final String targetStr = target != null ? target.toString() : null;
        setTargetStr(targetStr);
    }


    /**
     * The member interaction (ie action invocation or property edit) which caused the domain object to be changed.
     *
     * <p>
     *     Populated for commands and for published events that represent action invocations or property edits.
     * </p>
     *
     * <p>
     * This dummy implementation is a trick so that Isis will render the property in a standalone table.  Each of the
     * subclasses override with the &quot;real&quot; implementation.
     * </p>
     *
     * <p>
     *     NB: commands and published events applied only to actions, hence the name of this field.  In a future release
     *     the name of this field may change to &quot;TargetMember&quot;.  Note that the {@link PropertyLayout} already uses
     *     &quot;Member&quot; this as a name hint.
     * </p>
     *
     */
    @Property(optionality = Optionality.OPTIONAL)
    @PropertyLayout(
            named="Member",
            hidden = Where.ALL_EXCEPT_STANDALONE_TABLES
    )
    @MemberOrder(name="Target", sequence = "20")
    @Getter
    private String targetAction;



    /**
     * The (string representation of the) {@link Bookmark} identifying the domain object that has changed.
     *
     * <p>
     * This dummy implementation is a trick so that Isis will render the property in a standalone table.  Each of the
     * subclasses override with the &quot;real&quot; implementation.
     */
    @Property
    @PropertyLayout(named="Object")
    @MemberOrder(name="Target", sequence="30")
    public String getTargetStr() {
        return null;
    }

    /**
     * For {@link #setTarget(Bookmark)} to delegate to.
     */
    public abstract void setTargetStr(final String targetStr);



    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(named = "Open")
    @MemberOrder(name="TargetStr", sequence="1")
    public Object openTargetObject() {
        try {
            return bookmarkService != null
                    ? bookmarkService.lookup(getTarget())
                    : null;
        } catch(RuntimeException ex) {
            if(ex.getClass().getName().contains("ObjectNotFoundException")) {
                messageService.warnUser("Object not found - has it since been deleted?");
                return null;
            }
            throw ex;
        }
    }

    public boolean hideOpenTargetObject() {
        return getTarget() == null;
    }

    public String disableOpenTargetObject() {
        final Object targetObject = getTarget();
        if (targetObject == null) {
            return null;
        }
        final BeanSort sortOfObject = metaModelService.sortOf(getTarget(), MetaModelService.Mode.RELAXED);
        return !(sortOfObject.isViewModel() || sortOfObject.isEntity())
                ? "Can only open view models or entities"
                : null;
    }



    /**
     * The property of the object that was changed.
     *
     * <p>
     * Populated only for audit entries.
     *
     * <p>
     * This dummy implementation is a trick so that Isis will render the property in a standalone table.  Each of the
     * subclasses override with the &quot;real&quot; implementation.
     */
    @Property(optionality = Optionality.OPTIONAL)
    @PropertyLayout(hidden = Where.ALL_EXCEPT_STANDALONE_TABLES)
    @MemberOrder(name="Target",sequence = "21")
    public String getPropertyId() {
        return null;
    }


    /**
     * The value of the property prior to it being changed.
     *
     * <p>
     * Populated only for audit entries.
     *
     * <p>
     * This dummy implementation is a trick so that Isis will render the property in a standalone table.  Each of the
     * subclasses override with the &quot;real&quot; implementation.
     */
    @Property(optionality = Optionality.OPTIONAL)
    @PropertyLayout(hidden = Where.ALL_EXCEPT_STANDALONE_TABLES)
    @MemberOrder(name="Detail",sequence = "6")
    public String getPreValue() {
        return null;
    }


    /**
     * The value of the property after it has changed.
     *
     * <p>
     * Populated only for audit entries.
     *
     * <p>
     * This dummy implementation is a trick so that Isis will render the property in a standalone table.  Each of the
     * subclasses override with the &quot;real&quot; implementation.
     */
    @Property(optionality = Optionality.MANDATORY)
    @PropertyLayout(hidden = Where.ALL_EXCEPT_STANDALONE_TABLES)
    @MemberOrder(name="Detail",sequence = "7")
    public String getPostValue() {
        return null;
    }


    @javax.inject.Inject
    protected BookmarkService bookmarkService;

    @javax.inject.Inject
    protected MessageService messageService;

    @javax.inject.Inject
    protected MetaModelService metaModelService;

}
