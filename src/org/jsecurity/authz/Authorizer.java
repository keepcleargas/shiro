/*
 * Copyright (C) 2005-2007 Jeremy Haile, Les Hazlewood
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General
 * Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the
 *
 * Free Software Foundation, Inc.
 * 59 Temple Place, Suite 330
 * Boston, MA 02111-1307
 * USA
 *
 * Or, you may view it online at
 * http://www.opensource.org/licenses/lgpl-license.php
 */
package org.jsecurity.authz;

import java.util.Collection;
import java.util.List;

/**
 * An <tt>Authorizer</tt> performs authorization (access control) operations for any given Subject
 * (aka 'application user').
 *
 * <p>Each method requires a subject principal to perform the action for the corresponding Subject/user.
 *
 * <p>This principal argument is usually an object representing a user database primary key or a String username or
 * something similar that uniquely identifies an application user.  The runtime value of the this principal
 * is application-specific and provided by the application's configured Realms.
 *
 * <p>Note that there are many *Permission methods in this interface overloaded to accept String arguments instead of
 * {@link Permission Permission} instances. They are a convenience allowing the caller to use a String representation of
 * a {@link Permission Permission} if desired.  Most implementations of this interface will simply convert these
 * String values to {@link Permission Permission} instances and then just call the corresponding type-safe method.
 * (JSecurity's default implementations do String-to-Permission conversion for these methods using 
 * {@link org.jsecurity.authz.permission.PermissionResolver PermissionResolver}s.)
 *
 * <p>These overloaded *Permission methods <em>do</em> forego type-saftey for the benefit of convenience and simplicity,
 * so you should choose which ones to use based on your preferences and needs.
 *
 * @since 0.1
 * @author Jeremy Haile
 * @author Les Hazlewood
 */
public interface Authorizer {

    /**
     * Returns <tt>true</tt> if the corresponding subject/user is permitted to perform an action or access a resource
     * summarized by the specified permission string.
     *
     * <p>This is an overloaded method for the corresponding type-safe {@link Permission Permission} variant.
     * Please see the class-level JavaDoc for more information on these String-based permission methods.
     *
     * @param subjectPrincipal the application-specific subject/user identifier.
     * @param permission the String representation of a Permission that is being checked.
     * @return true if the corresponding Subject/user is permitted, false otherwise.
     * @since 0.9
     * @see #isPermitted(Object subjectPrincipal,Permission permission)
     */
    boolean isPermitted( Object subjectPrincipal, String permission );

    /**
     * Returns <tt>true</tt> if the corresponding subject/user is permitted to perform an action or access a resource
     * summarized by the specified permission.
     *
     * <p>More specifically, this method determines if any <tt>Permission</tt>s associated
     * with the subject {@link Permission#implies(Permission) imply} the specified permission.
     *
     * @param subjectPrincipal the application-specific subject/user identifier.
     * @param permission the permission that is being checked.
     * @return true if the corresponding Subject/user is permitted, false otherwise.
     */
    boolean isPermitted( Object subjectPrincipal, Permission permission );

    /**
     * Checks if the corresponding Subject implies the given permission strings and returns a boolean array
     * indicating which permissions are implied.
     *
     * <p>This is an overloaded method for the corresponding type-safe {@link Permission Permission} variant.
     * Please see the class-level JavaDoc for more information on these String-based permission methods.
     *
     * @param subjectPrincipal the application-specific subject/user identifier.
     * @param permissions the String representations of the Permissions that are being checked.
     * @return an array of booleans whose indices correspond to the index of the
     * permissions in the given list.  A true value at an index indicates the user is permitted for
     * for the associated <tt>Permission</tt> string in the list.  A false value at an index
     * indicates otherwise.
     * @since 0.9
     */
    boolean[] isPermitted( Object subjectPrincipal, String... permissions );

    /**
     * Checks if the corresponding Subject/user implies the given Permissions and returns a boolean array indicating
     * which permissions are implied.
     *
     * <p>More specifically, this method should determine if each <tt>Permission</tt> in
     * the array is {@link Permission#implies(Permission) implied} by permissions
     * already associated with the subject.
     *
     * <p>This is primarily a performance-enhancing method to help reduce the number of
     * {@link #isPermitted} invocations over the wire in client/server systems.
     *
     * @param subjectPrincipal the application-specific subject/user identifier.
     * @param permissions the permissions that are being checked.
     * @return an array of booleans whose indices correspond to the index of the
     * permissions in the given list.  A true value at an index indicates the user is permitted for
     * for the associated <tt>Permission</tt> object in the list.  A false value at an index
     * indicates otherwise.
     */
    boolean[] isPermitted( Object subjectPrincipal, List<Permission> permissions );

    /**
     * Returns <tt>true</tt> if the corresponding Subject/user implies all of the specified permission strings,
     * <tt>false</tt> otherwise.
     *
     * <p>This is an overloaded method for the corresponding type-safe {@link Permission Permission} variant.
     * Please see the class-level JavaDoc for more information on these String-based permission methods.
     *
     * @param subjectPrincipal the application-specific subject/user identifier.
     * @param permissions the String representations of the Permissions that are being checked.
     * @return true if the user has all of the specified permissions, false otherwise.
     * @see #isPermittedAll(Object,Collection)
     * @since 0.9
     */
    boolean isPermittedAll( Object subjectPrincipal, String... permissions );

    /**
     * Returns <tt>true</tt> if the corresponding Subject/user implies all of the specified permissions, <tt>false</tt>
     * otherwise.
     *
     * <p>More specifically, this method determines if all of the given <tt>Permission</tt>s are
     * {@link Permission#implies(Permission) implied by} permissions already associated with the subject.
     *
     * @param subjectPrincipal the application-specific subject/user identifier.
     * @param permissions the permissions to check.
     * @return true if the user has all of the specified permissions, false otherwise.
     */
    boolean isPermittedAll( Object subjectPrincipal, Collection<Permission> permissions );

    /**
     * Ensures the corresponding Subject/user implies the specified permission String.
     *
     * <p>If the subject's existing associated permissions do not {@link Permission#implies(Permission)} imply}
     * the given permission, an {@link org.jsecurity.authz.AuthorizationException} will be thrown.
     *
     * <p>This is an overloaded method for the corresponding type-safe {@link Permission Permission} variant.
     * Please see the class-level JavaDoc for more information on these String-based permission methods.
     *
     * @param subjectPrincipal the application-specific subject/user identifier.
     * @param permission the String representation of the Permission to check.
     * @throws org.jsecurity.authz.AuthorizationException if the user does not have the permission.
     * @since 0.9
     */
    void checkPermission( Object subjectPrincipal, String permission ) throws AuthorizationException;

    /**
     * Ensures a subject/user {@link Permission#implies(Permission)} implies} the specified <tt>Permission</tt>.
     * If the subject's exisiting associated permissions do not {@link Permission#implies(Permission)} imply}
     * the given permission, an {@link org.jsecurity.authz.AuthorizationException} will be thrown.
     *
     * @param subjectPrincipal the application-specific subject/user identifier.
     * @param permission the Permission to check.
     * @throws org.jsecurity.authz.AuthorizationException if the user does not have the permission.
     */
    void checkPermission( Object subjectPrincipal, Permission permission ) throws AuthorizationException;

    /**
     * Ensures the corresponding Subject/user
     * {@link org.jsecurity.authz.Permission#implies(org.jsecurity.authz.Permission) implies} all of the
     * specified permission strings.
     *
     * If the subject's exisiting associated permissions do not
     * {@link org.jsecurity.authz.Permission#implies(org.jsecurity.authz.Permission) imply} all of the given permissions,
     * an {@link org.jsecurity.authz.AuthorizationException} will be thrown.
     *
     * <p>This is an overloaded method for the corresponding type-safe {@link Permission Permission} variant.
     * Please see the class-level JavaDoc for more information on these String-based permission methods.
     *
     * @param subjectPrincipal the application-specific subject/user identifier.
     * @param permissions the string representations of Permissions to check.
     * @throws AuthorizationException if the user does not have all of the given permissions.
     * @since 0.9
     */
    void checkPermissions( Object subjectPrincipal, String... permissions ) throws AuthorizationException;

    /**
     * Ensures the corresponding Subject/user
     * {@link org.jsecurity.authz.Permission#implies(org.jsecurity.authz.Permission) implies} all of the
     * specified permission strings.
     *
     * If the subject's exisiting associated permissions do not
     * {@link org.jsecurity.authz.Permission#implies(org.jsecurity.authz.Permission) imply} all of the given permissions,
     * an {@link org.jsecurity.authz.AuthorizationException} will be thrown.
     *
     * @param subjectPrincipal the application-specific subject/user identifier.
     * @param permissions the Permissions to check.
     * @throws AuthorizationException if the user does not have all of the given permissions.
     */
    void checkPermissions( Object subjectPrincipal, Collection<Permission> permissions ) throws AuthorizationException;

    /**
     * Returns <tt>true</tt> if the corresponding Subject/user has the specified role, <tt>false</tt> otherwise.
     *
     * @param subjectPrincipal the application-specific subject/user identifier.
     * @param roleIdentifier the application-specific role identifier (usually a role id or role name).
     * @return <tt>true</tt> if the corresponding subject has the specified role, <tt>false</tt> otherwise.
     */
    boolean hasRole( Object subjectPrincipal, String roleIdentifier );

    /**
     * Checks if the corresponding Subject/user has the specified roles, returning a boolean array indicating
     * which roles are associated with the given subject.
     *
     * <p>This is primarily a performance-enhancing method to help reduce the number of
     * {@link #hasRole} invocations over the wire in client/server systems.
     *
     * @param subjectPrincipal the application-specific subject/user identifier.
     * @param roleIdentifiers the application-specific role identifiers to check (usually role ids or role names).
     * @return an array of booleans whose indices correspond to the index of the
     * roles in the given identifiers.  A true value indicates the user has the
     * role at that index.  False indicates the user does not have the role at that index.
     */
    boolean[] hasRoles( Object subjectPrincipal, List<String> roleIdentifiers );

    /**
     * Returns <tt>true</tt> if the corresponding Subject/user has all of the specified roles, <tt>false</tt> otherwise.
     *
     * @param subjectPrincipal the application-specific subject/user identifier.
     * @param roleIdentifiers the application-specific role identifiers to check (usually role ids or role names).
     * @return true if the user has all the roles, false otherwise.
     */
    boolean hasAllRoles( Object subjectPrincipal, Collection<String> roleIdentifiers );

    /**
     * Asserts the corresponding Subject/user has the specified role by returning quietly if they do or throwing an
     * {@link org.jsecurity.authz.AuthorizationException} if they do not.
     *
     * @param subjectPrincipal the application-specific subject/user identifier.
     * @param roleIdentifier the application-specific role identifier (usually a role id or role name ).
     * @throws org.jsecurity.authz.AuthorizationException if the user does not have the role.
     */
    void checkRole( Object subjectPrincipal, String roleIdentifier ) throws AuthorizationException;

    /**
     * Asserts the corresponding Subject/user has all of the specified roles by returning quietly if they do or
     * throwing an {@link org.jsecurity.authz.AuthorizationException} if they do not.
     *
     * @param subjectPrincipal the application-specific subject/user identifier.
     * @param roleIdentifiers the application-specific role identifiers to check (usually role ids or role names).
     * @throws org.jsecurity.authz.AuthorizationException if the user does not have all of the specified roles.
     */
    void checkRoles( Object subjectPrincipal, Collection<String> roleIdentifiers ) throws AuthorizationException;

}

