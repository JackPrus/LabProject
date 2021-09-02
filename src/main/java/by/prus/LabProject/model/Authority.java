package by.prus.LabProject.model;


/**
 * The enum represent types of authorities in application.
 * The autorities can be multiplied
 * READ_AUTHORITY - user can get and see subjectes he work with. User can not update or delete it
 * WRITE_AUTHORITY - user can create or update subjects, but can't read and delete it
 * DELETE_AUTHORITY - user can delete subjects he work with
 */
public enum Authority {
    READ_AUTHORITY, WRITE_AUTHORITY, DELETE_AUTHORITY
}
