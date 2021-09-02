package by.prus.LabProject.model;

/**
 * Enum represent roles of user using in application.
 * ROLE_USER - represents usual user?
 * ROLE_ADMIN represents user with multiple possibilities more complex than usual user.
 * Admin can add tag to certificate or delete user.
 */
public enum Role {
    ROLE_USER,
    ROLE_ADMIN
}
