package org.flameyosflow.greenbank.utils;

public enum Identifiers {
    /*
     * means will not work whatsoever for the current version
     *
     * or the feature has not been implemented
     */
    NOT_IMPLEMENTED,

    /*
     * means will work, but it's not recommended to use this kind of version or such.
     */
    UNRECOMMENDED,

    /*
     * means this will work with nearly no issues.
     */
    IMPLEMENTED,

    /*
     * means this will work with no issues.
     *
     * me and others would recommend anything that uses this way of implementation.
     */
    RECOMMENDED
}
